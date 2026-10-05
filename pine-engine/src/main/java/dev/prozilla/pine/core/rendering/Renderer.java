package dev.prozilla.pine.core.rendering;

import dev.prozilla.pine.common.asset.image.TextureAsset;
import dev.prozilla.pine.common.asset.text.Font;
import dev.prozilla.pine.common.lifecycle.Destructible;
import dev.prozilla.pine.common.lifecycle.Initializable;
import dev.prozilla.pine.common.logging.Logger;
import dev.prozilla.pine.common.math.MathUtils;
import dev.prozilla.pine.common.math.vector.Vector2f;
import dev.prozilla.pine.common.math.vector.Vector2i;
import dev.prozilla.pine.common.system.Color;
import dev.prozilla.pine.common.util.checks.Checks;
import dev.prozilla.pine.core.Application;
import dev.prozilla.pine.core.rendering.material.Material;
import dev.prozilla.pine.core.rendering.material.UnlitMaterial;
import dev.prozilla.pine.core.rendering.shader.DepthShaderProgram;
import dev.prozilla.pine.core.rendering.shader.ShaderProgram;
import dev.prozilla.pine.core.state.Tracker;
import dev.prozilla.pine.core.state.config.Config;
import dev.prozilla.pine.core.state.config.RenderConfig;
import org.jetbrains.annotations.Contract;
import org.joml.Matrix4f;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Objects;

import static org.lwjgl.glfw.GLFW.glfwGetCurrentContext;
import static org.lwjgl.glfw.GLFW.glfwGetFramebufferSize;
import static org.lwjgl.opengl.GL11.*;

// TODO: Split drawing methods into separate helpers
/**
 * Handles the rendering process.
 */
public class Renderer implements Initializable, Destructible {
	
	private VertexArrayObject vertexArrayObject;
	private VertexBufferObject vertexBufferObject;
	private ShaderProgram program;
	private FrameBufferObject frameBufferObject;
	private Material<?> defaultMaterial;
	
	// State
	private FloatBuffer vertices;
	private final Vertex vertex;
	private int numVertices;
	private boolean isRendering;
	private TextureAsset activeTexture;
	private Matrix4f projectionMatrix;
	private Matrix4f viewMatrix;
	private Matrix4f modelMatrix;
	
	// Render stats
	private int renderedVertices;
	private int totalVertices;
	
	// Camera
	private int viewWidth;
	private int viewHeight;
	
	// Fonts
	private Font defaultFont;
	private Font debugFont;
	
	// Transformation
	private boolean isRenderRegionEnabled;
	
	// Config options
	private Color fallbackColor;
	private RenderMode renderMode;
	private boolean snapPixels;
	
	private final Application application;
	private final Tracker tracker;
	private final Logger logger;
	
	// Constants
	/** The amount of vertices that fit into a single batch. */
	public final static int BATCH_VERTEX_CAPACITY = 1024;
	/** The maximum amount of floats a single vertex of any material can use. */
	public final static int MAX_VERTEX_FLOATS = 16;
	public final static String DEFAULT_FONT_PATH = "/fonts/Inconsolata.ttf";
	
	public Renderer(Application application) {
		this.application = application;
		tracker = application.getTracker();
		logger = application.getLogger();
		vertex = new Vertex();
		projectionMatrix = new Matrix4f();
		viewMatrix = new Matrix4f();
		modelMatrix = new Matrix4f();
	}
	
	@Override
	public void init() {
		setupBuffers();
		
		// Optimization: discard triangles with opacity < 0.1
//		glEnable(GL_ALPHA_TEST);
//		glAlphaFunc(GL_GREATER, 0.1f);
		
		// Read config options
		RenderConfig config = getConfig();
		config.enableBlend.read((enableBlend) -> {
			if (enableBlend) {
				glEnable(GL_BLEND);
				glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
			}
		});
		config.enableDepthTest.read((enableDepthTest) -> {
			if (enableDepthTest) {
				// TO DO: improve depth handling to avoid sorting renderers and use only depth test instead
				glEnable(GL_DEPTH_TEST);
				glDepthFunc(GL_LEQUAL);
			}
		});
		config.snapPixels.read((snapPixels) -> {
			this.snapPixels = snapPixels;
		});
		config.renderMode.addObserver((renderMode) -> {
			if (renderMode == RenderMode.DEPTH) {
				DepthShaderProgram program = ShaderProgram.getDepth();
				program.init();
				program.setLogger(logger);
				program.setProjectionMatrix(projectionMatrix);
				program.setViewMatrix(viewMatrix);
				program.setModelMatrix(modelMatrix);
			} else {
				program.setProjectionMatrix(projectionMatrix);
				program.setViewMatrix(viewMatrix);
				program.setModelMatrix(modelMatrix);
			}
		});
		
		createFont();
		reset();
	}
	
	public void initPreview(int width, int height) {
		try {
			frameBufferObject = new FrameBufferObject(width, height);
			frameBufferObject.init();
		} catch (Exception e) {
			logger.error("Failed to create frame buffer", e);
		}
		
		setupBuffers();
		createFont();
		reset();
	}
	
	private void createFont() {
		try {
			defaultFont = new Font(getClass().getResourceAsStream(DEFAULT_FONT_PATH), 16);
		} catch (IOException e) {
			logger.error("Failed to create font", e);
			defaultFont = new Font(12);
		}
		debugFont = new Font(12);
	}
	
	private void reset() {
		resetTransform();
		
		// Reset statistics
		renderedVertices = 0;
		totalVertices = 0;
		
		// Read config options
		Config config = application.getConfig();
		
		// Listen to config option changes
		config.rendering.fallbackRenderColor.read((fallbackRenderColor) -> {
			fallbackColor = fallbackRenderColor;
		});
		config.rendering.renderMode.read((renderMode) -> {
			this.renderMode = renderMode;
			updateRenderMode();
		});
	}
	
	private void updateRenderMode() {
		switch (renderMode) {
			case NORMAL, DEPTH -> glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
			case WIREFRAME -> glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
		}
	}
	
	/**
	 * Clears the color and depth buffers.
	 */
	public void clear() {
		if (isRendering) {
			flush();
		}
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	}
	
	public void clearColorBuffer() {
		if (isRendering) {
			flush();
		}
		glClear(GL_COLOR_BUFFER_BIT);
	}
	
	public void clearDepthBuffer() {
		if (isRendering) {
			flush();
		}
		glClear(GL_DEPTH_BUFFER_BIT);
	}
	
	/**
	 * Begin rendering.
	 */
	public void begin() throws IllegalStateException {
		if (isRendering) {
			throw new IllegalStateException("Renderer is already drawing!");
		}
		
		if (frameBufferObject != null) {
			frameBufferObject.bind();
		}
		
		isRendering = true;
		numVertices = 0;
		renderedVertices = 0;
		totalVertices = 0;
	}
	
	/**
	 * End rendering.
	 */
	public void end() throws IllegalStateException {
		if (!isRendering) {
			throw new IllegalStateException("Renderer isn't drawing!");
		}
		isRendering = false;
		flush();
		
		if (frameBufferObject != null) {
			frameBufferObject.unbind();
		}
		
		tracker.setVertices(renderedVertices, totalVertices);
	}
	
	/**
	 * Flushes the data to the GPU to let it get rendered.
	 */
	public void flush() {
		if (numVertices <= 0) {
			return;
		}
		
		vertices.flip();
		
		if (vertexArrayObject != null) {
			vertexArrayObject.bind();
		} else {
			vertexBufferObject.bind(VertexBufferObject.Target.ARRAY_BUFFER);
			getProgram().init();
		}
		getProgram().bind();
		
		// Bind the active texture
		if (activeTexture != null) {
			activeTexture.bind();
		}
		
		// Upload the new vertex data
		vertexBufferObject.bind(VertexBufferObject.Target.ARRAY_BUFFER);
		vertexBufferObject.uploadSubData(VertexBufferObject.Target.ARRAY_BUFFER, 0, vertices);
		
		// Draw batch
		glDrawArrays(GL_TRIANGLES, 0, numVertices);
		
		// Clear vertex data for next batch
		vertices.clear();
		renderedVertices += numVertices;
		numVertices = 0;
	}
	
	//region --- Transformation state ---
	
	public void resetTransform() {
		resetRegion();
	}
	
	/**
	 * Limits the rendering to the given region.
	 */
	public void setRegion(float x, float y, float width, float height) {
		setRegion(Math.round(x), Math.round(y), Math.round(width), Math.round(height));
	}
	
	/**
	 * Limits the rendering to the given region.
	 */
	public void setRegion(int x, int y, int width, int height) {
		flush();
		if (!isRenderRegionEnabled) {
			glEnable(GL_SCISSOR_TEST);
		}
		glScissor(x, y, width, height);
		isRenderRegionEnabled = true;
	}
	
	public void resetRegion() {
		if (!isRenderRegionEnabled) {
			return;
		}
		
		flush();
		glDisable(GL_SCISSOR_TEST);
		isRenderRegionEnabled = false;
	}
	
	//endregion Transformation state
	
	//region --- Calculations ---
	
	/**
	 * Calculates total width of a debug text.
	 * @param text The text
	 * @return Total width of the text
	 */
	public int getDebugTextWidth(CharSequence text) {
		return (int)debugFont.getWidth(text);
	}
	
	/**
	 * Calculates total height of a debug text.
	 * @param text The text
	 * @return Total width of the text
	 */
	public int getDebugTextHeight(CharSequence text) {
		return (int)debugFont.getHeight(text);
	}
	
	/**
	 * Draw debug text at the specified position.
	 * @param text TextRenderer to draw
	 * @param x    X coordinate of the text position
	 * @param y    Y coordinate of the text position
	 */
	public void drawDebugText(CharSequence text, float x, float y) {
		debugFont.drawText(this, text, x, y, 0);
	}
	
	/**
	 * Draw debug text at the specified position and color.
	 * @param text TextRenderer to draw
	 * @param x    X coordinate of the text position
	 * @param y    Y coordinate of the text position
	 * @param c    Color to use
	 */
	public void drawDebugText(CharSequence text, float x, float y, Color c) {
		debugFont.drawText(this, text, x, y, 0, c);
	}
	
	public Vector2i getTextSize(CharSequence text) {
		return getTextSize(defaultFont, text);
	}
	
	public Vector2i getTextSize(Font font, CharSequence text) {
		return new Vector2i(getTextWidth(font, text), getTextHeight(font, text));
	}
	
	public int getTextWidth(CharSequence text) {
		return getTextWidth(defaultFont, text);
	}
	
	/**
	 * Calculates total width of a text.
	 * @param text The text
	 * @return Total width of the text
	 */
	public int getTextWidth(Font font, CharSequence text) {
		return (int)font.getWidth(text);
	}
	
	public int getTextHeight(CharSequence text) {
		return getTextHeight(defaultFont, text);
	}
	
	/**
	 * Calculates total height of a text.
	 * @param text The text
	 * @return Total width of the text
	 */
	public int getTextHeight(Font font, CharSequence text) {
		return (int)font.getHeight(text);
	}
	
	//endregion Calculations
	
	//region --- Drawing ---
	
	public void drawText(CharSequence text, float x, float y, float z) {
		drawText(defaultFont, text, x, y, z);
	}
	
	/**
	 * Draw text at the specified position.
	 * @param text TextRenderer to draw
	 * @param x    X coordinate of the text position
	 * @param y    Y coordinate of the text position
	 */
	public void drawText(Font font, CharSequence text, float x, float y, float z) {
		font.drawText(this, text, x, y, z);
	}
	
	public void drawText(CharSequence text, float x, float y, float z, Color c) {
		drawText(defaultFont, text, x, y, z, c);
	}
	
	/**
	 * Draw text at the specified position and color.
	 * @param text TextRenderer to draw
	 * @param x    X coordinate of the text position
	 * @param y    Y coordinate of the text position
	 * @param c    Color to use
	 */
	public void drawText(Font font, CharSequence text, float x, float y, float z, Color c) {
		font.drawText(this, text, x, y, z, c);
	}
	
	/**
	 * Draws a white rectangle at the given position.
	 * @param x X position
	 * @param y Y position
	 * @param width Width of the rectangle
	 * @param height Height of the rectangle
	 */
	public void drawRect(float x, float y, float z, float width, float height) {
		drawRect(x, y, z, width, height, fallbackColor);
	}
	
	/**
	 * Draws a colored rectangle at the given position.
	 * @param x X position
	 * @param y Y position
	 * @param width Width of the rectangle
	 * @param height Height of the rectangle
	 * @param c Color
	 */
	public void drawRect(float x, float y, float z, float width, float height, Color c) {
		float x2 = x + width;
		float y2 = y + height;
		
		drawQuad(null, x, y, z, x2, y2, z, 0, 0, 0, 0, c);
	}
	
	/**
	 * Draws a texture on specified coordinates.
	 * @param texture Used for getting width and height of the texture
	 * @param x       X position of the texture
	 * @param y       Y position of the texture
	 */
	public void drawTexture(TextureAsset texture, float x, float y, float z) {
		drawTexture(texture, x, y, z, fallbackColor);
	}
	
	/**
	 * Draws a texture on specified coordinates and with
	 * specified color.
	 * @param texture Used for getting width and height of the texture
	 * @param x       X position of the texture
	 * @param y       Y position of the texture
	 * @param c       The color to use
	 */
	public void drawTexture(TextureAsset texture, float x, float y, float z, Color c) {
		// Vertex positions
		float x2 = x + texture.getWidth();
		float y2 = y + texture.getHeight();
		
		// Texture coordinates
		float s1 = 0f;
		float t1 = 0f;
		float s2 = 1f;
		float t2 = 1f;
		
		drawQuad(texture, x, y, z, x2, y2, z, s1, t1, s2, t2, c);
	}
	
	/**
	 * Draws a texture region on specified coordinates.
	 * @param texture   Used for getting width and height of the texture
	 * @param x         X position of the texture
	 * @param y         Y position of the texture
	 * @param regX      X position of the texture region
	 * @param regY      Y position of the texture region
	 * @param regWidth  Width of the texture region
	 * @param regHeight Height of the texture region
	 */
	public void drawQuad(TextureAsset texture, float x, float y, float z, float regX, float regY, float regWidth, float regHeight) {
		drawQuad(texture, x, y, z, regX, regY, regWidth, regHeight, fallbackColor);
	}
	
	/**
	 * Draws a texture region on specified coordinates.
	 * @param texture   Used for getting width and height of the texture
	 * @param x         X position of the texture
	 * @param y         Y position of the texture
	 * @param regX      X position of the texture region
	 * @param regY      Y position of the texture region
	 * @param regWidth  Width of the texture region
	 * @param regHeight Height of the texture region
	 * @param c         The color to use
	 */
	public void drawQuad(TextureAsset texture, float x, float y, float z, float regX, float regY, float regWidth, float regHeight, Color c) {
		// Vertex positions
		float x2 = x + regWidth;
		float y2 = y + regHeight;
		
		if (outOfBounds(x, y, x, y2, x2, y2, x2, y)) {
			totalVertices += 6;
			return;
		}
		
		// Texture coordinates
		float s1 = regX / texture.getWidth();
		float t1 = regY / texture.getHeight();
		float s2 = (regX + regWidth) / texture.getWidth();
		float t2 = (regY + regHeight) / texture.getHeight();
		
		drawQuad(texture, x, y, z, x2, y2, z, s1, t1, s2, t2, c);
	}
	
	/**
	 * Draws a texture region on specified coordinates.
	 * @param x1 Bottom left x position
	 * @param y1 Bottom left y position
	 * @param x2 Top right x position
	 * @param y2 Top right y position
	 * @param s1 Bottom left s coordinate
	 * @param t1 Bottom left t coordinate
	 * @param s2 Top right s coordinate
	 * @param t2 Top right t coordinate
	 */
	public void drawQuad(TextureAsset texture, float x1, float y1, float z1, float x2, float y2, float z2, float s1, float t1, float s2, float t2) {
		drawQuad(texture, x1, y1, z1, x2, y2, z2, s1, t1, s2, t2, fallbackColor);
	}
	
	/**
	 * Draws a texture region on specified coordinates.
	 * @param x1 Bottom left x position
	 * @param y1 Bottom left y position
	 * @param x2 Top right x position
	 * @param y2 Top right y position
	 * @param s1 Bottom left s coordinate
	 * @param t1 Bottom left t coordinate
	 * @param s2 Top right s coordinate
	 * @param t2 Top right t coordinate
	 * @param c  The color to use
	 */
	public void drawQuad(TextureAsset texture,
	                     float x1, float y1, float z1,
	                     float x2, float y2, float z2,
	                     float s1, float t1, float s2, float t2,
	                     Color c) {
		float z3 = (z1 + z2) / 2;
		drawQuad(texture, x1, y1, z1, x1, y2, z3, x2, y2, z2, x2, y1, z3, s1, t1, s2, t2, c);
	}
	
	/**
	 * Draws a texture region on specified coordinates.
	 */
	public void drawQuad(TextureAsset texture,
	                     float x1, float y1, float z1,
	                     float x2, float y2, float z2,
	                     float x3, float y3, float z3,
	                     float x4, float y4, float z4,
	                     float s1, float t1, float s2, float t2,
	                     Color c) {
		requireRendering();
		totalVertices += 6;
		
		// Discard draw call if object is outside the viewport bounds
		if (outOfBounds(x1, y1, x2, y2, x3, y3, x4, y4)) {
			return;
		}
		
		// Check if previous batch should be finished first
		if (vertices.remaining() < getProgram().getStrideLength() * 6 || (texture != null && activeTexture != null && !texture.hasEqualLocation(activeTexture))) {
			flush();
		}
		
		vertex.setColor(c);
		vertex.setTexture(texture);
		
		if (vertex.color.getAlpha() <= 0) {
			return;
		}
		
		// Avoid subpixel issues by snapping to nearest pixel
		if (snapPixels) {
			x1 = Math.round(x1);
			x2 = Math.round(x2);
			x3 = Math.round(x3);
			x4 = Math.round(x4);
			
			y1 = Math.round(y1);
			y2 = Math.round(y2);
			y3 = Math.round(y3);
			y4 = Math.round(y4);
		}
		
		// Push the vertices to the buffer
		drawVertex(x1, y1, z1, s1, t1);
		drawVertex(x2, y2, z2, s1, t2);
		drawVertex(x3, y3, z3, s2, t2);
		
		drawVertex(x1, y1, z1, s1, t1);
		drawVertex(x3, y3, z3, s2, t2);
		drawVertex(x4, y4, z4, s2, t1);
		
		replaceActiveTexture(texture);
	}
	
	/**
	 * Draws multiple textured triangles using vertex, triangle and UV arrays.
	 * @param vertices Array of vertex positions (x, y, z)
	 * @param uvArray Array of texture coordinates (u, v)
	 * @param triangles Array of triangles (every triangle is made up of 3 vertex indices)
	 * @throws IllegalArgumentException if any array has an invalid length
	 */
	public void drawTriangles(TextureAsset texture, float[] vertices, int[] triangles, float[] normals, float[] uvArray, Color c) {
		if (vertices.length % 3 != 0) {
			throw new IllegalArgumentException("Vertex array length must be a multiple of 3");
		}
		if (vertices.length * 2 != uvArray.length * 3) {
			throw new IllegalArgumentException("UV array length must correspond to vertex array length");
		}
		if (triangles.length % 3 != 0) {
			throw new IllegalArgumentException("Triangle array length must be a multiple of 3");
		}
		
		int triangleCount = triangles.length / 3;
		if (triangleCount == 0) {
			return;
		}
		
		for (int i = 0; i < triangleCount; i++) {
			int i1 = triangles[i * 3];
			int i2 = triangles[i * 3 + 1];
			int i3 = triangles[i * 3 + 2];
			
			float x1 = vertices[i1 * 3];
			float y1 = vertices[i1 * 3 + 1];
			float z1 = vertices[i1 * 3 + 2];
			float x2 = vertices[i2 * 3];
			float y2 = vertices[i2 * 3 + 1];
			float z2 = vertices[i2 * 3 + 2];
			float x3 = vertices[i3 * 3];
			float y3 = vertices[i3 * 3 + 1];
			float z3 = vertices[i3 * 3 + 2];
			
			float u1 = uvArray[i1 * 2];
			float v1 = uvArray[i1 * 2 + 1];
			float u2 = uvArray[i2 * 2];
			float v2 = uvArray[i2 * 2 + 1];
			float u3 = uvArray[i3 * 2];
			float v3 = uvArray[i3 * 2 + 1];
			
			float a1 = 0, b1 = 0, c1 = 0;
			float a2 = 0, b2 = 0, c2 = 0;
			float a3 = 0, b3 = 0, c3 = 0;
			if (normals != null && normals.length > 0) {
				if (normals.length == vertices.length) {
					a1 = normals[i1 * 3];
					b1 = normals[i1 * 3 + 1];
					c1 = normals[i1 * 3 + 2];
					a2 = normals[i2 * 3];
					b2 = normals[i2 * 3 + 1];
					c2 = normals[i2 * 3 + 2];
					a3 = normals[i3 * 3];
					b3 = normals[i3 * 3 + 1];
					c3 = normals[i3 * 3 + 2];
				} else {
					a1 = normals[i * 3];
					b1 = normals[i * 3 + 1];
					c1 = normals[i * 3 + 2];
					a2 = a1;
					b2 = b1;
					c2 = c1;
					a3 = a1;
					b3 = b1;
					c3 = c1;
				}
			}
			
			drawTriangle(texture, x1, y1, z1, x2, y2, z2, x3, y3, z3, a1, b1, c1, a2, b2, c2, a3, b3, c3, u1, v1, u2, v2, u3, v3, c);
		}
	}
	
	/**
	 * Draws a single textured triangle with the given vertex coordinates, texture coordinates,
	 * depth value, and color.
	 *
	 * <p>
	 * The triangle is defined by three points: (x1, y1), (x2, y2), and (x3, y3), with corresponding
	 * texture coordinates (u1, v1), (u2, v2), and (u3, v3).
	 * </p>
	 * @param x1 The x-coordinate of the first vertex
	 * @param y1 The y-coordinate of the first vertex
	 * @param z1 The z-coordinate of the first vertex
	 * @param x2 The x-coordinate of the second vertex
	 * @param y2 The y-coordinate of the second vertex
	 * @param z2 The z-coordinate of the second vertex
	 * @param x3 The x-coordinate of the third vertex
	 * @param y3 The y-coordinate of the third vertex
	 * @param z3 The z-coordinate of the third vertex
	 * @param u1 The u texture coordinate for the first vertex
	 * @param v1 The v texture coordinate for the first vertex
	 * @param u2 The u texture coordinate for the second vertex
	 * @param v2 The v texture coordinate for the second vertex
	 * @param u3 The u texture coordinate for the third vertex
	 * @param v3 The v texture coordinate for the third vertex
	 */
	public void drawTriangle(TextureAsset texture,
	                         float x1, float y1, float z1,
	                         float x2, float y2, float z2,
	                         float x3, float y3, float z3,
							 float a1, float b1, float c1,
							 float a2, float b2, float c2,
							 float a3, float b3, float c3,
	                         float u1, float v1, float u2, float v2, float u3, float v3,
	                         Color c) {
		totalVertices += 3;
		
		// Discard draw call if object is outside the viewport bounds
		if (outOfBounds(x1, y1, x2, y2, x3, y3)) {
			return;
		}
		
		// Check if previous batch should be finished first
		if (vertices.remaining() < getProgram().getStrideLength() * 3 || (texture != null && activeTexture != null && !texture.hasEqualLocation(activeTexture))) {
			flush();
		}
		
		// Handle render mode
		if (renderMode == RenderMode.DEPTH) {
			float depth = MathUtils.square((z1 + z2 + z3) / 3);
			vertex.color.set(depth, depth, depth, 1f);
			vertex.resetTexture();
		} else {
			vertex.setColor(c);
			vertex.setTexture(texture);
		}
		
		if (vertex.color.getAlpha() <= 0) {
			return;
		}
		
		// Avoid subpixel issues by snapping to nearest pixel
		if (snapPixels) {
			x1 = Math.round(x1);
			x2 = Math.round(x2);
			x3 = Math.round(x3);
			
			y1 = Math.round(y1);
			y2 = Math.round(y2);
			y3 = Math.round(y3);
		}
		
		// Push the vertices to the buffer
		drawVertex(x1, y1, z1, a1, b1, c1, u1, v1);
		drawVertex(x2, y2, z2, a2, b2, c2, u2, v2);
		drawVertex(x3, y3, z3, a3, b3, c3, u3, v3);
		
		replaceActiveTexture(texture);
	}
	
	public void drawVertex(float x, float y, float z, float u, float v) {
		drawVertex(x, y, z, 0, 0, 0, u, v);
	}
	
	public void drawVertex(float x, float y, float z, float a, float b, float c, float u, float v) {
		vertex.position.set(x, y, z);
		vertex.normal.set(a, b, c);
		vertex.textureCoordinates.set(u, v);
		getProgram().writeVertex(vertices, vertex);
		numVertices++;
	}
	
	private void replaceActiveTexture(TextureAsset newTexture) {
		if (newTexture != null && (activeTexture == null || !activeTexture.hasEqualLocation(newTexture))) {
			if (activeTexture != null) {
				activeTexture.unbind();
			}
			activeTexture = newTexture;
		}
	}
	
	//endregion Drawing
	
	/**
	 * Checks if a quad is outside the screen bounds.
	 */
	public boolean outOfBounds(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {
		return false;
//		return MathUtils.max(x1, x2, x3, x4) < 0
//			|| MathUtils.min(x1, x2, x3, x4) >= viewWidth
//			|| MathUtils.max(y1, y2, y3, y4) < 0
//	        || MathUtils.min(y1, y2, y3, y4) >= viewHeight;
	}
	
	public boolean outOfBounds(float x1, float y1, float x2, float y2, float x3, float y3) {
		return false;
//		return MathUtils.max(x1, x2, x3) < 0
//			|| MathUtils.min(x1, x2, x3) >= viewWidth
//			|| MathUtils.max(y1, y2, y3) < 0
//			|| MathUtils.min(y1, y2, y3) >= viewHeight;
	}
	
	/**
	 * Checks if a line is outside the screen bounds.
	 */
	public boolean outOfBounds(float x1, float y1, float x2, float y2) {
		return Math.max(x1, x2) < 0
			|| Math.min(x1, x2) >= viewWidth
			|| Math.max(y1, y2) < 0
			|| Math.min(y1, y2) >= viewHeight;
	}
	
	/**
	 * Checks if coordinates are outside the screen bounds.
	 * @param x X position
	 * @param y Y position
	 * @return True if the coordinate is outside of bounds
	 */
	public boolean outOfBounds(float x, float y) {
		return x < 0
			|| x >= viewWidth
			|| y < 0
			|| y >= viewHeight;
	}
	
	/**
	 * Disposes renderer and cleans up its used data.
	 */
	@Override
	public void destroy() {
		MemoryUtil.memFree(vertices);
		
		// Dispose of shader programs
		ShaderProgram.destroyAll();
		Destructible.destroy(vertexArrayObject, vertexBufferObject, frameBufferObject);
		
		// Dispose of fonts
		Destructible.destroy(defaultFont, debugFont);
	}
	
	private void setupBuffers() {
		if (vertexArrayObject != null || vertexBufferObject != null) {
			throw new IllegalStateException("renderer has already been set up");
		}
		
		// Generate Vertex Array Object
		vertexArrayObject = new VertexArrayObject();
		vertexArrayObject.bind();
		
		// Generate Vertex Buffer Object
		vertexBufferObject = new VertexBufferObject();
		vertexBufferObject.bind(VertexBufferObject.Target.ARRAY_BUFFER);
		
		// Create FloatBuffer
		vertices = MemoryUtil.memAllocFloat(BATCH_VERTEX_CAPACITY * MAX_VERTEX_FLOATS);
		
		// Upload null data to allocate storage for the VBO
		long size = (long)vertices.capacity() * Float.BYTES;
		vertexBufferObject.uploadData(VertexBufferObject.Target.ARRAY_BUFFER, size, VertexBufferObject.Usage.DYNAMIC_DRAW);
		
		// Initialize variables */
		numVertices = 0;
		isRendering = false;
		
		resize();
		
		// Reset matrices
		projectionMatrix.identity();
		viewMatrix.identity();
		modelMatrix.identity();
		
		// Set default material
		defaultMaterial = new UnlitMaterial();
		setMaterial(defaultMaterial);
	}
	
	/**
	 * Updates the projection matrix according to the window's dimensions.
	 */
	public void resize() {
		int width, height;
		
		if (frameBufferObject == null) {
			// Get width and height of frame buffer
			long window = glfwGetCurrentContext();
			try (MemoryStack stack = MemoryStack.stackPush()) {
				IntBuffer widthBuffer = stack.mallocInt(1);
				IntBuffer heightBuffer = stack.mallocInt(1);
				glfwGetFramebufferSize(window, widthBuffer, heightBuffer);
				width = widthBuffer.get();
				height = heightBuffer.get();
			}
		} else {
			width = frameBufferObject.getWidth();
			height = frameBufferObject.getHeight();
		}
		
		if (width == viewWidth && height == viewHeight) {
			return;
		}
		
		glViewport(0, 0, width, height);
		viewWidth = width;
		viewHeight = height;
	}
	
	public void setProjectionMatrix(Matrix4f projectionMatrix) {
		flush();
		this.projectionMatrix = projectionMatrix;
		getProgram().setProjectionMatrix(projectionMatrix);
	}
	
	public void setViewMatrix(Matrix4f viewMatrix) {
		flush();
		this.viewMatrix = viewMatrix;
		getProgram().setViewMatrix(viewMatrix);
	}
	
	public void resetModelMatrix() {
		setModelMatrix(new Matrix4f());
	}
	
	public void setModelMatrix(Matrix4f modelMatrix) {
		flush();
		this.modelMatrix = modelMatrix;
		getProgram().setModelMatrix(modelMatrix);
	}
	
	public void resetMaterial() {
		setMaterial(defaultMaterial);
	}
	
	public void setMaterial(Material<?> material) {
		material.bind(this);
	}
	
	protected ShaderProgram getProgram() {
		if (renderMode == RenderMode.DEPTH) {
			return ShaderProgram.getDepth();
		} else {
			return program;
		}
	}
	
	public void setProgram(ShaderProgram program) {
		Checks.isNotNull(program, "program");
		if (Objects.equals(this.program, program)) {
			return;
		}
		
		flush();
		
		this.program = program;
		program.init();
		program.setLogger(logger);
		program.setProjectionMatrix(projectionMatrix);
		program.setViewMatrix(viewMatrix);
		program.setModelMatrix(modelMatrix);
	}
	
	public int getWidth() {
		return this.viewWidth;
	}
	
	public int getHeight() {
		return this.viewHeight;
	}
	
	private void requireRendering() throws IllegalStateException {
		if (!isRendering) {
			throw new IllegalStateException("rendering is not allowed in the current state");
		}
	}
	
	public boolean isRendering() {
		return isRendering;
	}
	
	public FrameBufferObject getFrameBufferObject() {
		return frameBufferObject;
	}
	
	public RenderConfig getConfig() {
		return application.getConfig().rendering;
	}
	
	public Color getFallbackColor() {
		return fallbackColor;
	}
	
	/**
	 * Creates a new {@link Vector2f} that represents the center of the viewport.
	 */
	@Contract("-> new")
	public Vector2f getViewportCenter() {
		return new Vector2f(viewWidth / 2f, viewHeight / 2f);
	}
	
}
