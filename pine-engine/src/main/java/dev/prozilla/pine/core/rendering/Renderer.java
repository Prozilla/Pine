package dev.prozilla.pine.core.rendering;

import dev.prozilla.pine.common.Lifecycle;
import dev.prozilla.pine.common.math.matrix.Matrix4f;
import dev.prozilla.pine.common.math.vector.Vector2i;
import dev.prozilla.pine.common.system.resource.Color;
import dev.prozilla.pine.common.system.resource.Texture;
import dev.prozilla.pine.common.system.resource.text.Font;
import dev.prozilla.pine.core.state.Tracker;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import java.awt.*;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.stream.IntStream;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_DYNAMIC_DRAW;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL31.GL_INVALID_INDEX;

/**
 * Handles rendering process.
 */
public class Renderer implements Lifecycle {

    private VertexArrayObject vao;
    private VertexBufferObject vbo;
    private ShaderProgram program;
    private FrameBufferObject fbo;

    // Vertex buffer
    private FloatBuffer vertices;
    private int numVertices;
    private boolean drawing;
    
    // Render stats
    private int renderedVertices;
    private int totalVertices;

    // View dimensions
    private int viewWidth;
    private int viewHeight;
    
    // Fonts
    private Font defaultFont;
    private Font debugFont;
    
    private float renderScale;
    
    // Constants
    private final static int STRIDE_LENGTH = 10;
    /** Matches the length of <code>uTextures</code> in the fragment shader. */
    public final static int MAX_TEXTURES = 32;
    /** The amount of strides to fit into a single vertex buffer. */
    private final static int VERTEX_BUFFER_SIZE = 1024;
    
    // Paths
    private final static String VERTEX_SHADER_PATH = "/shaders/default.vert";
    private final static String FRAGMENT_SHADER_PATH = "/shaders/default.frag";
    private final static String FONT_PATH = "/fonts/Inconsolata.ttf";
    
    private final Tracker tracker;
    
    public Renderer(Tracker tracker) {
        this.tracker = tracker;
    }

    @Override
    public void init() {
        setupShaderProgram();
        
        // Enable blending
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        // Enable depth test
        // TO DO: improve depth handling to avoid sorting renderers and use only depth test instead
        glEnable(GL_DEPTH_TEST);
        glDepthFunc(GL_LEQUAL);

        createFont();
        reset();
    }
    
    public void initPreview(int width, int height) {
	    try {
		    fbo = new FrameBufferObject(width, height);
		    fbo.init();
	    } catch (Exception e) {
            System.err.println("Failed to create frame buffer");
            e.printStackTrace();
	    }
        
        setupShaderProgram();
	    createFont();
        reset();
    }
    
    private void createFont() {
        try {
            defaultFont = new Font(getClass().getResourceAsStream(FONT_PATH), 16);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
            defaultFont = new Font();
        }
        debugFont = new Font(12, false);
    }
    
    private void reset() {
        renderScale = 1;
        renderedVertices = 0;
        totalVertices = 0;
    }

    /**
     * Clears the drawing area.
     */
    public void clear() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    /**
     * Begin rendering.
     */
    public void begin() throws IllegalStateException {
        if (drawing) {
            throw new IllegalStateException("Renderer is already drawing!");
        }
        
        if (fbo != null) {
            fbo.bind();
        }
        
        drawing = true;
        numVertices = 0;
        renderedVertices = 0;
        totalVertices = 0;
    }

    /**
     * End rendering.
     */
    public void end() throws IllegalStateException {
        if (!drawing) {
            throw new IllegalStateException("Renderer isn't drawing!");
        }
        drawing = false;
        flush();
        
        if (fbo != null) {
            fbo.unbind();
        }
        
        tracker.setVertices(renderedVertices, totalVertices);
    }

    /**
     * Flushes the data to the GPU to let it get rendered.
     */
    public void flush() {
        if (numVertices > 0) {
            vertices.flip();

            if (vao != null) {
                vao.bind();
            } else {
                vbo.bind(GL_ARRAY_BUFFER);
                specifyVertexAttributes();
            }
            program.use();

            // Upload the new vertex data
            vbo.bind(GL_ARRAY_BUFFER);
            vbo.uploadSubData(GL_ARRAY_BUFFER, 0, vertices);

            // Draw batch
            glDrawArrays(GL_TRIANGLES, 0, numVertices);

            // Clear vertex data for next batch
            vertices.clear();
            renderedVertices += numVertices;
            numVertices = 0;
        }
    }
    
    public void setScale(float scale) {
        this.renderScale = scale;
    }
    
    /**
     * Calculates total width of a debug text.
     * @param text The text
     * @return Total width of the text
     */
    public int getDebugTextWidth(CharSequence text) {
        return debugFont.getWidth(text);
    }
    
    /**
     * Calculates total height of a debug text.
     * @param text The text
     * @return Total width of the text
     */
    public int getDebugTextHeight(CharSequence text) {
        return debugFont.getHeight(text);
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
        return font.getWidth(text);
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
        return font.getHeight(text);
    }

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
        drawRect(x, y, z, width, height, Color.WHITE);
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
        
        Texture.currentTextureId = null;
        
        drawTextureRegion(x, y, x2, y2, z, 0, 0, 0, 0, c);
    }

    public void drawRotatedTexture(Texture texture, float x, float y, float z, float r) {
        drawRotatedTexture(texture, x, y, z, Color.WHITE, r);
    }
    
    public void drawRotatedTexture(Texture texture, float x, float y, float z, Color c, float r) {
        if (r == 0) {
            drawTexture(texture, x, y, z, c);
            return;
        }
        
        // Vertex positions
	    float x2 = x + texture.getWidth() * renderScale;
        float y2 = y + texture.getHeight() * renderScale;
        
        // Texture coordinates
        float s1 = 0f;
        float t1 = 0f;
        float s2 = 1f;
        float t2 = 1f;
        
        texture.bind();
        
        drawRotatedTextureRegion(x, y, x2, y2, z, s1, t1, s2, t2, c, r);
    }
    
    /**
     * Draws a texture on specified coordinates.
     * @param texture Used for getting width and height of the texture
     * @param x       X position of the texture
     * @param y       Y position of the texture
     */
    public void drawTexture(Texture texture, float x, float y, float z) {
        drawTexture(texture, x, y, z, Color.WHITE);
    }

    /**
     * Draws a texture on specified coordinates and with
     * specified color.
     * @param texture Used for getting width and height of the texture
     * @param x       X position of the texture
     * @param y       Y position of the texture
     * @param c       The color to use
     */
    public void drawTexture(Texture texture, float x, float y, float z, Color c) {
        // Vertex positions
	    float x2 = x + texture.getWidth() * renderScale;
        float y2 = y + texture.getHeight() * renderScale;

        // Texture coordinates
        float s1 = 0f;
        float t1 = 0f;
        float s2 = 1f;
        float t2 = 1f;
        
        texture.bind();
        
        drawTextureRegion(x, y, x2, y2, z, s1, t1, s2, t2, c);
    }
    
    public void drawRotatedTextureRegion(Texture texture, float x, float y, float z, float regX, float regY, float regWidth, float regHeight, float r) {
        drawRotatedTextureRegion(texture, x, y, z, regX, regY, regWidth, regHeight, Color.WHITE, r);
    }
    
    public void drawRotatedTextureRegion(Texture texture, float x, float y, float z, float regX, float regY, float regWidth, float regHeight, Color c, float r) {
        if (r == 0) {
            drawTextureRegion(texture, x, y, z, regX, regY, regWidth, regHeight, c);
            return;
        }
        
        // Calculate the rotation angle in radians
        float radians = (float) Math.toRadians(r + 90f);
        
        // Calculate sin and cos (flip them to fix the incorrect orientation)
        float cosAngle = (float) Math.cos(radians);
        float sinAngle = (float) Math.sin(radians);
        
        // Calculate the center of the texture
        float centerX = x + (regWidth * renderScale) / 2.0f;
        float centerY = y + (regHeight * renderScale) / 2.0f;
        
        // Calculate the rotated width and height
        float rotatedWidth = Math.abs(cosAngle * regWidth * renderScale) + Math.abs(sinAngle * regHeight * renderScale);
        float rotatedHeight = Math.abs(sinAngle * regWidth * renderScale) + Math.abs(cosAngle * regHeight * renderScale);
        
        // Adjust the position so that the texture rotates around its center
        float newX = centerX - rotatedWidth / 2.0f;
        float newY = centerY - rotatedHeight / 2.0f;
        
        // Calculate the new x2 and y2
        float x2 = newX + rotatedWidth;
        float y2 = newY + rotatedHeight;
        
        // Texture coordinates
        float s1 = regX / texture.getWidth();
        float t1 = regY / texture.getHeight();
        float s2 = (regX + regWidth) / texture.getWidth();
        float t2 = (regY + regHeight) / texture.getHeight();
        
        // Bind the texture
        texture.bind();
        
        // Delegate to the rotation drawing method
        drawRotatedTextureRegion(newX, newY, x2, y2, z, s1, t1, s2, t2, c, r);
    }
    
    public void drawRotatedTextureRegion(float x1, float y1, float x2, float y2, float z, float s1, float t1, float s2, float t2, float r) {
        drawRotatedTextureRegion(x1, y1, x2, y2, z, s1, t1, s2, t2, Color.WHITE, r);
    }
    
    public void drawRotatedTextureRegion(float x1, float y1, float x2, float y2, float z, float s1, float t1, float s2, float t2, Color c, float r) {
        if (r == 0) {
            drawTextureRegion(x1, y1, x2, y2, z, s1, t1, s2, t2, c);
            return;
        }
        
        r = (float)Math.toRadians(r + 90f);
        
        // Calculate the center of the quad (around which it will rotate)
        float centerX = (x1 + x2) / 2.0f;
        float centerY = (y1 + y2) / 2.0f;
        
        // Calculate cos and sin of the rotation angle
        float cosAngle = (float) Math.cos(r);
        float sinAngle = (float) Math.sin(r);
        
        // Apply rotation to each vertex (rotate around center)
        float newX1 = cosAngle * (x1 - centerX) - sinAngle * (y1 - centerY) + centerX;
        float newY1 = sinAngle * (x1 - centerX) + cosAngle * (y1 - centerY) + centerY;
        
        float newX2 = cosAngle * (x2 - centerX) - sinAngle * (y1 - centerY) + centerX;
        float newY2 = sinAngle * (x2 - centerX) + cosAngle * (y1 - centerY) + centerY;
        
        float newX3 = cosAngle * (x2 - centerX) - sinAngle * (y2 - centerY) + centerX;
        float newY3 = sinAngle * (x2 - centerX) + cosAngle * (y2 - centerY) + centerY;
        
        float newX4 = cosAngle * (x1 - centerX) - sinAngle * (y2 - centerY) + centerX;
        float newY4 = sinAngle * (x1 - centerX) + cosAngle * (y2 - centerY) + centerY;
        
        drawTextureRegion(newX1, newY1, newX2, newY2, newX3, newY3, newX4, newY4, z, s1, t1, s2, t2, c);
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
    public void drawTextureRegion(Texture texture, float x, float y, float z, float regX, float regY, float regWidth, float regHeight) {
        drawTextureRegion(texture, x, y, z, regX, regY, regWidth, regHeight, Color.WHITE);
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
    public void drawTextureRegion(Texture texture, float x, float y, float z, float regX, float regY, float regWidth, float regHeight, Color c) {
        // Vertex positions
	    float x2 = x + regWidth * renderScale;
        float y2 = y + regHeight * renderScale;
        
        if (outOfBounds(x, y, x, y2, x2, y2, x2, y)) {
            totalVertices += 6;
            return;
        }

        // Texture coordinates
        float s1 = regX / texture.getWidth();
        float t1 = regY / texture.getHeight();
        float s2 = (regX + regWidth) / texture.getWidth();
        float t2 = (regY + regHeight) / texture.getHeight();
        
        texture.bind();
        
        drawTextureRegion(x, y, x2, y2, z, s1, t1, s2, t2, c);
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
    public void drawTextureRegion(float x1, float y1, float x2, float y2, float z, float s1, float t1, float s2, float t2) {
        drawTextureRegion(x1, y1, x2, y2, z, s1, t1, s2, t2, Color.WHITE);
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
    public void drawTextureRegion(float x1, float y1, float x2, float y2, float z, float s1, float t1, float s2, float t2, Color c) {
        drawTextureRegion(x1, y1, x1, y2, x2, y2, x2, y1, z, s1, t1, s2, t2, c);
    }
    
    /**
     * Draws a texture region on specified coordinates.
     */
    public void drawTextureRegion(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4, float z,
        float s1, float t1, float s2, float t2, Color c) {
        totalVertices += 6;
        
        if (outOfBounds(x1, y1, x2, y2, x3, y3, x4, y4)) {
            return;
        }
        
        // Ensure we have enough space in the buffer
        if (vertices.remaining() < STRIDE_LENGTH * 6) {
            flush();
        }
        
        // Get color components from the Color object
        float r = c.getRed();
        float g = c.getGreen();
        float b = c.getBlue();
        float a = c.getAlpha();
        
        // Texture ID (default to -1)
        float textureId = -1f;
        if (Texture.currentTextureId != null) {
            textureId = Texture.currentTextureId;
        }
        
        // Ensure texture ID is within bounds
        if (textureId >= MAX_TEXTURES) {
            System.err.println("Exceeded maximum amount of textures: " + MAX_TEXTURES);
        }
        
        // Avoid subpixel issues by snapping to nearest pixel
        x1 = Math.round(x1);
        x2 = Math.round(x2);
        x3 = Math.round(x3);
        x4 = Math.round(x4);
        
        y1 = Math.round(y1);
        y2 = Math.round(y2);
        y3 = Math.round(y3);
        y4 = Math.round(y4);
        
        // Push the vertices to the buffer (with the correct order for a quad)
        vertices.put(x1).put(y1).put(z).put(r).put(g).put(b).put(a).put(s1).put(t1).put(textureId);
        vertices.put(x2).put(y2).put(z).put(r).put(g).put(b).put(a).put(s1).put(t2).put(textureId);
        vertices.put(x3).put(y3).put(z).put(r).put(g).put(b).put(a).put(s2).put(t2).put(textureId);
        
        vertices.put(x1).put(y1).put(z).put(r).put(g).put(b).put(a).put(s1).put(t1).put(textureId);
        vertices.put(x3).put(y3).put(z).put(r).put(g).put(b).put(a).put(s2).put(t2).put(textureId);
        vertices.put(x4).put(y4).put(z).put(r).put(g).put(b).put(a).put(s2).put(t1).put(textureId);
        
        // Increment the number of vertices
        numVertices += 6;
    }
    
    /**
     * Checks if a quad is outside the screen bounds.
     */
    public boolean outOfBounds(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {
        // TO DO: calculate intersection of quad with screen
        return x1 < 0 && x2 < 0 && x3 < 0 && x4 < 0 || // Left
            x1 >= viewWidth && x2 >= viewWidth && x3 >= viewWidth && x4 >= viewWidth || // Right
            y1 < 0 && y2 < 0 && y3 < 0 && y4 < 0 || // Above
            y1 >= viewHeight && y2 >= viewHeight && y3 >= viewHeight && y4 >= viewHeight; // Below
    }
    
    /**
     * Checks if a line is outside the screen bounds.
     */
    public boolean outOfBounds(float x1, float y1, float x2, float y2) {
        // TO DO: calculate intersection of line with screen
        return outOfBounds(x1, y1) && outOfBounds(x2, y2);
    }
    
    /**
     * Checks if coordinates are outside the screen bounds.
     * @param x X position
     * @param y Y position
     * @return True if the coordinate is outside of bounds
     */
    public boolean outOfBounds(float x, float y) {
        return x < 0f || x >= viewWidth || y < 0f || y >= viewHeight;
    }

    /**
     * Disposes renderer and cleans up its used data.
     */
    public void destroy() {
        MemoryUtil.memFree(vertices);

        // Dispose of shader program
        if (vao != null) {
            vao.destroy();
        }
        if (vbo != null) {
            vbo.destroy();
        }
        if (program != null) {
            program.destroy();
        }
        if (fbo != null) {
            fbo.destroy();
        }

        // Dispose of fonts
        if (defaultFont != null) {
            defaultFont.destroy();
        }
        if (debugFont != null) {
            debugFont.destroy();
        }
    }

    /**
     * Initializes the default shader program.
     */
    private void setupShaderProgram() {
        // Generate Vertex Array Object
        vao = new VertexArrayObject();
        vao.bind();

        // Generate Vertex Buffer Object
        vbo = new VertexBufferObject();
        vbo.bind(GL_ARRAY_BUFFER);

        // Create FloatBuffer
        vertices = MemoryUtil.memAllocFloat(VERTEX_BUFFER_SIZE * STRIDE_LENGTH);

        // Upload null data to allocate storage for the VBO
        long size = (long) vertices.capacity() * Float.BYTES;
        vbo.uploadData(GL_ARRAY_BUFFER, size, GL_DYNAMIC_DRAW);

        // Initialize variables */
        numVertices = 0;
        drawing = false;

        // Load shaders
        Shader vertexShader, fragmentShader;
        vertexShader = Shader.loadShader(GL_VERTEX_SHADER, VERTEX_SHADER_PATH);
        fragmentShader = Shader.loadShader(GL_FRAGMENT_SHADER, FRAGMENT_SHADER_PATH);

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

        // Set textures uniform
        int[] textures = IntStream.range(0, MAX_TEXTURES).toArray();
        int uniTex = program.getUniformLocation("uTextures");
        if (uniTex == GL_INVALID_INDEX) {
            throw new RuntimeException("Textures uniform not found. (uTextures)");
        }
        program.setUniform(uniTex, textures);

        // Set view matrix to identity matrix
        Matrix4f view = new Matrix4f();
        int uniView = program.getUniformLocation("uView");
        if (uniView == GL_INVALID_INDEX) {
            throw new RuntimeException("View matrix uniform not found. (uView)");
        }
        program.setUniform(uniView, view);
        
        resize();
    }
    
    /**
     * Updates the projection matrix according to the window's dimensions.
     */
    public void resize() {
        int width, height;
        
        if (fbo == null) {
            // Get width and height of frame buffer
            long window = GLFW.glfwGetCurrentContext();
            try (MemoryStack stack = MemoryStack.stackPush()) {
                IntBuffer widthBuffer = stack.mallocInt(1);
                IntBuffer heightBuffer = stack.mallocInt(1);
                GLFW.glfwGetFramebufferSize(window, widthBuffer, heightBuffer);
                width = widthBuffer.get();
                height = heightBuffer.get();
            }
        } else {
            width = fbo.getWidth();
            height = fbo.getHeight();
        }
        
        if (width == viewWidth && height == viewHeight) {
            return;
        }
        
        // Set projection matrix to an orthographic projection
        Matrix4f projection = Matrix4f.orthographic(0f, width, 0f, height, -1f, 1f);
        int uniProjection = program.getUniformLocation("uProjection");
        if (uniProjection == GL_INVALID_INDEX) {
            throw new RuntimeException("Projection matrix uniform not found. (uProjection)");
        }
        program.setUniform(uniProjection, projection);
        
//        System.out.printf("View: %sx%s%n", width, height);
        
        viewWidth = width;
        viewHeight = height;
    }

    /**
     * Specifies the vertex pointers.
     */
    private void specifyVertexAttributes() {
        // Specify vertex position pointer
        int posAttrib = program.getAttributeLocation("vPosition");
        if (posAttrib == GL_INVALID_INDEX) {
            throw new RuntimeException("Vertex position pointer not found. (vPosition)");
        }
        program.enableVertexAttribute(posAttrib);
        program.pointVertexAttribute(posAttrib, 3, STRIDE_LENGTH * Float.BYTES, 0);

        // Specify color pointer
        int colAttrib = program.getAttributeLocation("vColor");
        if (colAttrib == GL_INVALID_INDEX) {
            throw new RuntimeException("Color pointer not found. (vColor)");
        }
        program.enableVertexAttribute(colAttrib);
        program.pointVertexAttribute(colAttrib, 4, STRIDE_LENGTH * Float.BYTES, 3 * Float.BYTES);

        // Specify texture pointer
        int texAttrib = program.getAttributeLocation("vTexCoords");
        if (texAttrib == GL_INVALID_INDEX) {
            throw new RuntimeException("Texture pointer not found. (vTexCoords)");
        }
        program.enableVertexAttribute(texAttrib);
        program.pointVertexAttribute(texAttrib, 2, STRIDE_LENGTH * Float.BYTES, 7 * Float.BYTES);
        
        // Specify texture ID pointer
        int texIdAttrib = program.getAttributeLocation("vTexId");
        if (texIdAttrib == GL_INVALID_INDEX) {
            throw new RuntimeException("Texture ID pointer not found. (vTexId)");
        }
        program.enableVertexAttribute(texIdAttrib);
        program.pointVertexAttribute(texIdAttrib, 1, STRIDE_LENGTH * Float.BYTES, 9 * Float.BYTES);
    }

    public int getWidth() {
        return this.viewWidth;
    }
    
    public int getHeight() {
        return this.viewHeight;
    }
    
    public boolean isDrawing() {
        return drawing;
    }
    
    public FrameBufferObject getFbo() {
        return fbo;
    }
}
