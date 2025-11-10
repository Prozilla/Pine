package dev.prozilla.pine.common.asset.image;

import dev.prozilla.pine.common.Cloneable;
import dev.prozilla.pine.common.IntEnum;
import dev.prozilla.pine.common.Printable;
import dev.prozilla.pine.common.asset.pool.AssetPools;
import dev.prozilla.pine.common.util.EnumUtils;
import dev.prozilla.pine.common.util.checks.Checks;
import dev.prozilla.pine.core.Application;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL14.GL_MIRRORED_REPEAT;
import static org.lwjgl.opengl.GL44.GL_MIRROR_CLAMP_TO_EDGE;

/**
 * Represents an OpenGL texture.
 */
public class Texture implements TextureAsset, Printable, Cloneable<Texture> {
	
	/** The handle of this texture */
	private final int id;
	/** The width of this texture */
	private final int width;
	/** The height of this texture */
	private final int height;
	/** The path of the image of this texture */
	private final String path;

	public static final Wrap DEFAULT_WRAP = Wrap.CLAMP_TO_BORDER;
	public static final Filter DEFAULT_FILTER = Filter.LINEAR;
	
	/**
	 * Creates an empty texture.
	 */
	public Texture(int width, int height) {
		this(width, height, DEFAULT_WRAP, DEFAULT_FILTER);
	}
	
	/**
	 * Creates an empty texture.
	 */
	public Texture(int width, int height, Wrap wrap, Filter filter) {
		this(null, width, height, null, wrap, filter);
	}
	
	/**
	 * Creates a texture based on an image.
	 */
	public Texture(Image image) {
		this(image, DEFAULT_WRAP, DEFAULT_FILTER);
	}
	
	/**
	 * Creates a texture based on an image.
	 */
	public Texture(Image image, Wrap wrap, Filter filter) {
		this(image.getPath(), image.getWidth(), image.getHeight(), image.getPixels(), wrap, filter);
	}
	
	public Texture(String path, int width, int height, ByteBuffer pixels) {
		this(path, width, height, pixels, DEFAULT_WRAP, DEFAULT_FILTER);
	}
	
	public Texture(String path, int width, int height, ByteBuffer pixels, Wrap wrap, Filter filter) {
		this.path = path;
		this.width = width;
		this.height = height;
		
		Application.requireOpenGL();
		
		Checks.isPositive(width, false, "texture width must be positive");
		Checks.isPositive(height, false, "texture height must be positive");
		
		id = glGenTextures();
		
		init(pixels, wrap, filter);
	}
	
	private void init(ByteBuffer pixels, Wrap wrap, Filter filter) {
		bind();
		
		if (wrap != null) {
			setWrap(wrap);
		}
		if (filter != null) {
			setFilter(filter);
		}
		
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
	
	@Override
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
		TextureAsset.super.destroy();
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
	public boolean hasEqualLocation(TextureAsset other) {
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
	
	@Override
	public boolean equals(Texture texture) {
		return equals((TextureAsset)texture);
	}
	
	public boolean equals(TextureAsset other) {
		return other != null && !other.isInArray() && other.getId() == id;
	}
	
	@Override
	public boolean equals(Object other) {
		return other == this || (other instanceof TextureAsset textureAsset && equals(textureAsset));
	}
	
	@Override
	public Texture clone() {
		return new Texture(path, width, height, AssetPools.images.load(path).getPixels());
	}
	
	@Override
	public @NotNull String toString() {
		return String.format("Texture #%s (%sx%s)", id, width, height);
	}
	
	public enum Filter implements IntEnum {
		NEAREST(GL_NEAREST),
		LINEAR(GL_LINEAR);
		
		private final int value;
		
		Filter(int value) {
			this.value = value;
		}
		
		public int getValue() {
			return value;
		}
		
		public static boolean isValid(int value) {
			return EnumUtils.hasIntValue(values(), value);
		}
		
	}
	
	public enum Wrap implements IntEnum {
		CLAMP_TO_EDGE(GL_CLAMP_TO_EDGE),
		CLAMP_TO_BORDER(GL_CLAMP_TO_BORDER),
		MIRRORED_REPEAT( GL_MIRRORED_REPEAT),
		REPEAT( GL_REPEAT),
		MIRROR_CLAMP_TO_EDGE(GL_MIRROR_CLAMP_TO_EDGE);
		
		private final int value;
		
		Wrap(int value) {
			this.value = value;
		}
		
		public int getValue() {
			return value;
		}
		
		public static boolean isValid(int value) {
			return EnumUtils.hasIntValue(values(), value);
		}
		
	}
	
}
