package dev.prozilla.pine.common.asset.image;

import dev.prozilla.pine.common.Printable;
import dev.prozilla.pine.common.util.checks.Checks;
import dev.prozilla.pine.core.Application;

import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL13.*;

/**
 * Represents an OpenGL texture.
 */
public class Texture implements TextureBase, Printable {
	
	/** The handle of this texture */
	private final int id;
	/** The width of this texture */
	private final int width;
	/** The height of this texture */
	private final int height;
	/** The path of the image of this texture */
	private final String path;
	
	/**
	 * Creates an empty texture.
	 */
	public Texture(int width, int height) {
		this(null, width, height, null);
	}
	
	/**
	 * Creates a texture based on an image.
	 */
	public Texture(Image image) {
		this(image.getPath(), image.getWidth(), image.getHeight(), image.getPixels());
	}
	
	public Texture(String path, int width, int height, ByteBuffer pixels) {
		this.path = path;
		this.width = width;
		this.height = height;
		
		Application.requireOpenGL();
		
		Checks.isPositive(width, false, "texture width must be positive");
		Checks.isPositive(height, false, "texture height must be positive");
		
		id = glGenTextures();
		
		init(pixels);
	}
	
	private void init(ByteBuffer pixels) {
		bind();
		
		// Clamp image in both directions
		setParameter(GL_TEXTURE_WRAP_S, GL_CLAMP_TO_BORDER);
		setParameter(GL_TEXTURE_WRAP_T, GL_CLAMP_TO_BORDER);
		
		// Set texture scaling parameters to pixelate
		setParameter(GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		setParameter(GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		
		uploadData(GL_RGBA8, width, height, GL_RGBA, pixels);
		
		unbind();
	}
	
	@Override
	public void bind() {
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, id);
	}
	
	@Override
	public void unbind() {
		glBindTexture(GL_TEXTURE_2D, 0);
	}
	
	public void setParameter(int name, int value) {
		glTexParameteri(GL_TEXTURE_2D, name, value);
	}
	
	/**
	 * Uploads image data with specified width and height.
	 * @param width Width of the image
	 * @param height Height of the image
	 * @param pixels Pixel data of the image
	 */
	public void uploadData(int width, int height, ByteBuffer pixels) {
		uploadData(GL_RGBA8, width, height, GL_RGBA, pixels);
	}
	
	/**
	 * Uploads image data with specified internal format, width, height and
	 * image format.
	 * @param internalFormat Internal format of the image data
	 * @param width Width of the image
	 * @param height Height of the image
	 * @param format Format of the image data
	 * @param pixels Pixel data of the image
	 */
	public void uploadData(int internalFormat, int width, int height, int format, ByteBuffer pixels) {
		glTexImage2D(GL_TEXTURE_2D, 0, internalFormat, width, height, 0, format, GL_UNSIGNED_BYTE, pixels);
	}
	
	@Override
	public void destroy() {
		TextureBase.super.destroy();
		glDeleteTextures(id);
	}
	
	@Override
	public int getId() {
		return id;
	}
	
	@Override
	public int getWidth() {
		return width;
	}
	
	@Override
	public int getHeight() {
		return height;
	}
	
	@Override
	public String getPath() {
		return path;
	}
	
	@Override
	public boolean hasEqualLocation(TextureBase other) {
		return equals(other);
	}
	
	@Override
	public boolean isInArray() {
		return false;
	}
	
	@Override
	public TextureArray getArray() {
		return null;
	}
	
	@Override
	public int hashCode() {
		return id;
	}
	
	public boolean equals(TextureBase other) {
		return !other.isInArray() && other.getId() == id;
	}
	
	@Override
	public boolean equals(Object other) {
		if (other == this) {
			return true;
		}
		
		return other instanceof Texture texture && texture.getId() == id;
	}
	
	@Override
	public String toString() {
		return String.format("Texture #%s (%sx%s)", id, width, height);
	}
}
