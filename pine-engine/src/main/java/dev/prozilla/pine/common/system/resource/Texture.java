package dev.prozilla.pine.common.system.resource;

import dev.prozilla.pine.common.Lifecycle;
import dev.prozilla.pine.common.Printable;
import dev.prozilla.pine.core.rendering.Renderer;

import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL13.*;

/**
 * Represents an OpenGL texture.
 */
public class Texture implements Lifecycle, Printable {

    /**
     * Stores the handle of the texture.
     */
    public final int id;

    /**
     * Width of the texture.
     */
    private int width;
    /**
     * Height of the texture.
     */
    private int height;
    
    public static Integer currentTextureId;
    
    /** Creates a texture. */
    public Texture() {
        id = glGenTextures();
        
        if (id >= Renderer.MAX_TEXTURES) {
            throw new RuntimeException("Exceeded maximum amount of textures: " + Renderer.MAX_TEXTURES);
        }
    }

    /**
     * Binds the texture.
     */
    public void bind() {
        if (currentTextureId == null || currentTextureId != id) {
            glActiveTexture(GL_TEXTURE0 + id);
            glBindTexture(GL_TEXTURE_2D, id);
            currentTextureId = id;
        }
    }
    
    public void unbind() {
        glBindTexture(GL_TEXTURE_2D, 0);
    }
    
    /**
     * Sets a parameter of the texture.
     * @param name  Name of the parameter
     * @param value Value to set
     */
    public void setParameter(int name, int value) {
        glTexParameteri(GL_TEXTURE_2D, name, value);
    }

    /**
     * Uploads image data with specified width and height.
     * @param width  Width of the image
     * @param height Height of the image
     * @param data   Pixel data of the image
     */
    public void uploadData(int width, int height, ByteBuffer data) {
        uploadData(GL_RGBA8, width, height, GL_RGBA, data);
    }

    /**
     * Uploads image data with specified internal format, width, height and
     * image format.
     * @param internalFormat Internal format of the image data
     * @param width          Width of the image
     * @param height         Height of the image
     * @param format         Format of the image data
     * @param data           Pixel data of the image
     */
    public void uploadData(int internalFormat, int width, int height, int format, ByteBuffer data) {
        glTexImage2D(GL_TEXTURE_2D, 0, internalFormat, width, height, 0, format, GL_UNSIGNED_BYTE, data);
    }

    /**
     * Delete the texture.
     */
    @Override
    public void destroy() {
        glDeleteTextures(id);
    }

    /**
     * Gets the texture width.
     *
     * @return Texture width
     */
    public int getWidth() {
        return width;
    }

    /**
     * Sets the texture width.
     * @param width The width to set
     */
    public void setWidth(int width) {
        if (width > 0) {
            this.width = width;
        }
    }

    /**
     * Gets the texture height.
     * @return Texture height
     */
    public int getHeight() {
        return height;
    }

    /**
     * Sets the texture height.
     * @param height The height to set
     */
    public void setHeight(int height) {
        if (height > 0) {
            this.height = height;
        }
    }
    
    @Override
    public String toString() {
        return String.format("Texture #%s (%sx%s)", id, width, height);
    }
    
    /**
     * Creates a texture based on an image.
     * @param image Image
     * @return Texture
     */
    public static Texture createTexture(Image image) {
        return createTexture(image.getWidth(), image.getHeight(), image.getImage());
    }
    
    /**
     * Creates a texture with specified width, height and data.
     * @param width  Width of the texture
     * @param height Height of the texture
     * @param data   Picture Data in RGBA format
     * @return Texture from the specified data
     */
    public static Texture createTexture(int width, int height, ByteBuffer data) {
        Texture texture = new Texture();
        texture.setWidth(width);
        texture.setHeight(height);
        
        texture.bind();

        // Clamp image in both directions
        texture.setParameter(GL_TEXTURE_WRAP_S, GL_CLAMP_TO_BORDER);
        texture.setParameter(GL_TEXTURE_WRAP_T, GL_CLAMP_TO_BORDER);
        
        // Set texture scaling parameters to pixelate
        texture.setParameter(GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        texture.setParameter(GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        texture.uploadData(GL_RGBA8, width, height, GL_RGBA, data);
        
        return texture;
    }
    
    public static void reset() {
        currentTextureId = null;
    }
}
