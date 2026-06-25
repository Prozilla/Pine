package dev.prozilla.pine.core.rendering;

import dev.prozilla.pine.common.asset.image.TextureAsset;
import dev.prozilla.pine.common.asset.pool.AssetPoolEvent;
import dev.prozilla.pine.common.asset.pool.AssetPools;
import dev.prozilla.pine.common.asset.text.Font;
import dev.prozilla.pine.common.lifecycle.Destructible;
import dev.prozilla.pine.common.lifecycle.Initializable;
import dev.prozilla.pine.common.logging.Logger;
import dev.prozilla.pine.common.math.MathUtils;
import dev.prozilla.pine.common.math.vector.Vector2f;
import dev.prozilla.pine.common.math.vector.Vector2i;
import dev.prozilla.pine.common.system.Color;
import dev.prozilla.pine.common.system.Platform;
import dev.prozilla.pine.core.Application;
import dev.prozilla.pine.core.state.Tracker;
import dev.prozilla.pine.core.state.config.Config;
import dev.prozilla.pine.core.state.config.RenderConfig;
import org.jetbrains.annotations.Contract;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

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
	
	// State
	private FloatBuffer vertices;
	private int numVertices;
	private boolean isRendering;
	private TextureAsset activeTexture;
	
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
	
	// Constants
	private final static int STRIDE_LENGTH = 11;
	/** The amount of strides to fit into a single vertex buffer. */
	private final static int VERTEX_BUFFER_SIZE = 32;
	
	// Config options
	private Color fallbackColor;
	private RenderMode renderMode;
	private boolean snapPixels;
	
	// Paths
	private final static String VERTEX_SHADER_PATH = "/shaders/default.vert";
	private final static String FRAGMENT_SHADER_PATH = "/shaders/default.frag";
	private final static String FONT_PATH = "/fonts/Inconsolata.ttf";
	
	private final Application application;
	private final Tracker tracker;
	private final Logger logger;
	
	public Renderer(Application application) {
		this.application = application;
		tracker = application.getTracker();
		logger = application.getLogger();
	}
	
	@Override
	public void init() {
		setupShaderProgram();
		
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
		
		setupShaderProgram();
		createFont();
		reset();
	}
	
	private void createFont() {
		try {
			defaultFont = new Font(getClass().getResourceAsStream(FONT_PATH), 16);
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
			specifyVertexAttributes();
		}
		program.use();
		
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
		
		drawTextureRegion(null, x, y, z, x2, y2, z, 0, 0, 0, 0, c);
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
		
		drawTextureRegion(texture, x, y, z, x2, y2, z, s1, t1, s2, t2, c);
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
	public void drawTextureRegion(TextureAsset texture, float x, float y, float z, float regX, float regY, float regWidth, float regHeight) {
		drawTextureRegion(texture, x, y, z, regX, regY, regWidth, regHeight, fallbackColor);
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
	public void drawTextureRegion(TextureAsset texture, float x, float y, float z, float regX, float regY, float regWidth, float regHeight, Color c) {
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
		
		drawTextureRegion(texture, x, y, z, x2, y2, z, s1, t1, s2, t2, c);
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
	public void drawTextureRegion(TextureAsset texture, float x1, float y1, float z1, float x2, float y2, float z2, float s1, float t1, float s2, float t2) {
		drawTextureRegion(texture, x1, y1, z1, x2, y2, z2, s1, t1, s2, t2, fallbackColor);
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
	public void drawTextureRegion(TextureAsset texture,
	                              float x1, float y1, float z1,
	                              float x2, float y2, float z2,
	                              float s1, float t1, float s2, float t2,
	                              Color c) {
		float z3 = (z1 + z2) / 2;
		drawTextureRegion(texture, x1, y1, z1, x1, y2, z3, x2, y2, z2, x2, y1, z3, s1, t1, s2, t2, c);
	}
	
	/**
	 * Draws a texture region on specified coordinates.
	 */
	public void drawTextureRegion(TextureAsset texture,
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
		if (vertices.remaining() < STRIDE_LENGTH * 6 || (texture != null && activeTexture != null && !texture.hasEqualLocation(activeTexture))) {
			flush();
		}
		
		// Get color components
		float r = c.getRed();
		float g = c.getGreen();
		float b = c.getBlue();
		float a = c.getAlpha();
		
		// Get texture ID and type
		int texId = -1;
		float texType = 0f;
		if (texture != null) {
			texId = texture.getId();
			texType = texture.isInArray() ? 1f : 0f;
		}
		
		// Handle depth render mode
		if (renderMode == RenderMode.DEPTH) {
			float depth = MathUtils.square((z1 + z2 + z3 + z4) / 4);
			r = depth;
			g = depth;
			b = depth;
			a = 1f;
			texId = -1;
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
		vertices.put(x1).put(y1).put(z1).put(r).put(g).put(b).put(a).put(s1).put(t1).put(texId).put(texType);
		vertices.put(x2).put(y2).put(z2).put(r).put(g).put(b).put(a).put(s1).put(t2).put(texId).put(texType);
		vertices.put(x3).put(y3).put(z3).put(r).put(g).put(b).put(a).put(s2).put(t2).put(texId).put(texType);
		
		vertices.put(x1).put(y1).put(z1).put(r).put(g).put(b).put(a).put(s1).put(t1).put(texId).put(texType);
		vertices.put(x3).put(y3).put(z3).put(r).put(g).put(b).put(a).put(s2).put(t2).put(texId).put(texType);
		vertices.put(x4).put(y4).put(z4).put(r).put(g).put(b).put(a).put(s2).put(t1).put(texId).put(texType);
		
		numVertices += 6;
		
		replaceActiveTexture(texture);
	}
	
	/**
	 * Draws multiple textured triangles using the given vertex and UV arrays.
	 *
	 * <p>
	 * Each triangle is defined by 3 vertices (6 floats: x1, y1, x2, y2, x3, y3) and corresponding
	 * texture coordinates (u1, v1, u2, v2, u3, v3) from the UV array. The number of triangles drawn
	 * is determined by dividing the length of the vertex array by 6.
	 * </p>
	 * @param vertices A flat float array containing x and y coordinates for each triangle vertex
	 * @param uvArray A flat float array containing the corresponding u and v texture coordinates. Must be the same length as {@code vertices}.
	 * @throws IllegalArgumentException if the length of {@code vertices} does not match {@code uvArray}
	 */
	public void drawTriangles(TextureAsset texture, float[] vertices, float[] uvArray, Color c) {
		if (vertices.length * 2 != uvArray.length * 3) {
			throw new IllegalArgumentException("UV array must have an entry for each entry in the vertex array");
		}
		
		int triangleCount = vertices.length / 9;
		if (triangleCount == 0) {
			return;
		}
		
		for (int i = 0; i < triangleCount; i++) {
			float x1 = vertices[i * 9];
			float y1 = vertices[i * 9 + 1];
			float z1 = vertices[i * 9 + 2];
			float x2 = vertices[i * 9 + 3];
			float y2 = vertices[i * 9 + 4];
			float z2 = vertices[i * 9 + 5];
			float x3 = vertices[i * 9 + 6];
			float y3 = vertices[i * 9 + 7];
			float z3 = vertices[i * 9 + 8];
			
			float u1 = uvArray[i * 6];
			float v1 = uvArray[i * 6 + 1];
			float u2 = uvArray[i * 6 + 2];
			float v2 = uvArray[i * 6 + 3];
			float u3 = uvArray[i * 6 + 4];
			float v3 = uvArray[i * 6 + 5];
			
			drawTriangle(texture, x1, y1, z1, x2, y2, z2, x3, y3, z3, u1, v1, u2, v2, u3, v3, c);
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
	                         float u1, float v1, float u2, float v2, float u3, float v3,
	                         Color c) {
		totalVertices += 3;
		
		// Discard draw call if object is outside the viewport bounds
		if (outOfBounds(x1, y1, x2, y2, x3, y3)) {
			return;
		}
		
		// Check if previous batch should be finished first
		if (vertices.remaining() < STRIDE_LENGTH * 3 || (texture != null && activeTexture != null && !texture.hasEqualLocation(activeTexture))) {
			flush();
		}
		
		// Get color components
		float r = c.getRed();
		float g = c.getGreen();
		float b = c.getBlue();
		float a = c.getAlpha();
		
		// Get texture ID and type
		int texId = -1;
		float texType = 0f;
		if (texture != null) {
			texId = texture.getId();
			texType = texture.isInArray() ? 1f : 0f;
		}
		
		// Handle depth render mode
		if (renderMode == RenderMode.DEPTH) {
			float depth = MathUtils.square((z1 + z2 + z3) / 3);
			r = depth;
			g = depth;
			b = depth;
			a = 1f;
			texId = -1;
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
		vertices.put(x1).put(y1).put(z1).put(r).put(g).put(b).put(a).put(u1).put(v1).put(texId).put(texType);
		vertices.put(x2).put(y2).put(z2).put(r).put(g).put(b).put(a).put(u2).put(v2).put(texId).put(texType);
		vertices.put(x3).put(y3).put(z3).put(r).put(g).put(b).put(a).put(u3).put(v3).put(texId).put(texType);
		
		numVertices += 3;
		
		replaceActiveTexture(texture);
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
		
		// Dispose of shader program
		Destructible.destroy(vertexArrayObject, vertexBufferObject, program, frameBufferObject);
		
		// Dispose of fonts
		Destructible.destroy(defaultFont, debugFont);
	}
	
	/**
	 * Initializes the default shader program.
	 */
	private void setupShaderProgram() {
		if (vertexArrayObject != null || vertexBufferObject != null) {
			throw new IllegalStateException("shader program has already been set up");
		}
		
		// Generate Vertex Array Object
		vertexArrayObject = new VertexArrayObject();
		vertexArrayObject.bind();
		
		// Generate Vertex Buffer Object
		vertexBufferObject = new VertexBufferObject();
		vertexBufferObject.bind(VertexBufferObject.Target.ARRAY_BUFFER);
		
		// Create FloatBuffer
		vertices = MemoryUtil.memAllocFloat(VERTEX_BUFFER_SIZE * STRIDE_LENGTH);
		
		// Upload null data to allocate storage for the VBO
		long size = (long) vertices.capacity() * Float.BYTES;
		vertexBufferObject.uploadData(VertexBufferObject.Target.ARRAY_BUFFER, size, VertexBufferObject.Usage.DYNAMIC_DRAW);
		
		// Initialize variables */
		numVertices = 0;
		isRendering = false;
		
		// Load shaders
		AssetPools.shaders.addListener(AssetPoolEvent.Type.FAILED, this::handleShaderLoadingError);
		Shader vertexShader = AssetPools.shaders.loadVertexShader(VERTEX_SHADER_PATH);
		Shader fragmentShader = AssetPools.shaders.loadFragmentShader(FRAGMENT_SHADER_PATH);
		AssetPools.shaders.removeListener(AssetPoolEvent.Type.FAILED, this::handleShaderLoadingError);
		
		// Create shader program
		program = new ShaderProgram();
		program.attachShader(vertexShader);
		program.attachShader(fragmentShader);
		if (GL.getCapabilities().OpenGL32) {
			program.bindFragmentDataLocation(0, "color");
		}
		program.link();
		program.use();
		
		// Delete linked shaders */
		vertexShader.destroy();
		fragmentShader.destroy();
		
		// Specify Vertex Pointers
		specifyVertexAttributes();
		
		// Set uniforms
		program.setUniform("uTexture", 0);
		if (Platform.get() != Platform.MACOS) {
			program.setUniform("uTextureArray", 0);
		}

		resize();
		setProjectionMatrix(new Matrix4f());
		setViewMatrix(new Matrix4f());
		setModelMatrix(new Matrix4f());
	}
	
	private void handleShaderLoadingError(AssetPoolEvent<Shader> event) {
		String message = String.format("Failed to load shader: %s", event.getPath());
		String error = event.getError();
		if (error != null) {
			message += System.lineSeparator() + error;
		}
		logger.error(message, event.getException());
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
		program.setUniform("uProjection", projectionMatrix);
	}
	
	public void setViewMatrix(Matrix4f viewMatrix) {
		flush();
		program.setUniform("uView", viewMatrix);
	}
	
	public void resetModelMatrix() {
		setModelMatrix(new Matrix4f());
	}
	
	public void setModelMatrix(Matrix4f modelMatrix) {
		flush();
		program.setUniform("uModel", modelMatrix);
	}
	
	/**
	 * Specifies the vertex pointers.
	 */
	private void specifyVertexAttributes() {
		program.setVertexAttributes(
			new CharSequence[]{"vPosition", "vColor", "vTexCoords", "vTexId", "vIsArrayTexture"},
			new int[]{3, 4, 2, 1, 1},
			STRIDE_LENGTH
		);
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
