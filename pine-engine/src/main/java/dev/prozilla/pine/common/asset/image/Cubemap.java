package dev.prozilla.pine.common.asset.image;

import dev.prozilla.pine.common.IntEnum;
import dev.prozilla.pine.common.Printable;
import dev.prozilla.pine.common.asset.Asset;
import dev.prozilla.pine.common.util.EnumUtils;
import dev.prozilla.pine.common.util.checks.Checks;
import dev.prozilla.pine.core.Application;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL13.*;

/**
 * Represents an OpenGL cubemap.
 */
public class Cubemap implements Asset, TextureBase, Printable {
	
	/** The handle of this cubemap */
	private final int id;
	/** The width of this cubemap */
	private final int width;
	/** The height of this cubemap */
	private final int height;
	/** The path of the image of this cubemap */
	private final String path;

	public static final Texture.Wrap DEFAULT_WRAP = Texture.Wrap.CLAMP_TO_EDGE;
	public static final Texture.Filter DEFAULT_FILTER = Texture.Filter.LINEAR;
	
	/**
	 * Creates an empty cubemap.
	 */
	public Cubemap(int width, int height) {
		this(width, height, DEFAULT_WRAP, DEFAULT_FILTER);
	}
	
	/**
	 * Creates an empty texture.
	 */
	public Cubemap(int width, int height, Texture.Wrap wrap, Texture.Filter filter) {
		this(null, width, height, null, wrap, filter);
	}
	
	/**
	 * Creates a cubemap based on images.
	 */
	public Cubemap(String path, Image[] images) {
		this(path, images, DEFAULT_WRAP, DEFAULT_FILTER);
	}
	
	/**
	 * Creates a cubemap based on images.
	 */
	public Cubemap(String path, Image[] images, Texture.Wrap wrap, Texture.Filter filter) {
		this(path, images[0].getWidth(), images[0].getHeight(), imagesToPixels(images), wrap, filter);
	}
	
	public Cubemap(String path, int width, int height, ByteBuffer[] images) {
		this(path, width, height, images, DEFAULT_WRAP, DEFAULT_FILTER);
	}
	
	public Cubemap(String path, int width, int height, ByteBuffer[] images, Texture.Wrap wrap, Texture.Filter filter) {
		this.path = path;
		this.width = width;
		this.height = height;
		
		Application.requireOpenGL();
		
		Checks.isPositive(width, false, "cubemap width must be positive");
		Checks.isPositive(height, false, "cubemap height must be positive");
		
		id = glGenTextures();
		
		init(images, wrap, filter);
	}
	
	private void init(ByteBuffer[] images, Texture.Wrap wrap, Texture.Filter filter) {
		bind();
		
		if (wrap != null) {
			setWrap(wrap);
		}
		if (filter != null) {
			setFilter(filter);
		}
		
		Target[] targets = Target.values();
		for (int i = 0; i < Math.min(images.length, targets.length); i++) {
			uploadData(targets[i], GL_RGBA8, width, height, GL_RGBA, images[i]);
		}
		
		unbind();
	}
	
	@Override
	public void bind() {
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_CUBE_MAP, id);
	}
	
	@Override
	public void unbind() {
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_CUBE_MAP, 0);
	}
	
	@Override
	public void setWrap(Texture.Wrap wrap) {
		TextureBase.super.setWrap(wrap);
		setParameter(GL_TEXTURE_WRAP_R, wrap.getValue());
	}
	
	@Override
	public void setParameter(int name, int value) {
		glTexParameteri(GL_TEXTURE_CUBE_MAP, name, value);
	}
	
	/**
	 * Uploads image data with specified width and height.
	 * @param width Width of the image
	 * @param height Height of the image
	 * @param pixels Pixel data of the image
	 */
	public void uploadData(Target target, int width, int height, ByteBuffer pixels) {
		uploadData(target ,GL_RGBA8, width, height, GL_RGBA, pixels);
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
	public void uploadData(Target target, int internalFormat, int width, int height, int format, ByteBuffer pixels) {
		glTexImage2D(target.value, 0, internalFormat, width, height, 0, format, GL_UNSIGNED_BYTE, pixels);
	}
	
	@Override
	public void destroy() {
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
	public int hashCode() {
		return id;
	}
	
	@Override
	public @NotNull String toString() {
		return String.format("Cubemap #%s (%sx%s)", id, width, height);
	}
	
	public enum Target implements IntEnum {
		RIGHT(GL_TEXTURE_CUBE_MAP_POSITIVE_X),
		LEFT(GL_TEXTURE_CUBE_MAP_NEGATIVE_X),
		TOP( GL_TEXTURE_CUBE_MAP_POSITIVE_Y),
		BOTTOM( GL_TEXTURE_CUBE_MAP_NEGATIVE_Y),
		FRONT(GL_TEXTURE_CUBE_MAP_POSITIVE_Z),
		BACK(GL_TEXTURE_CUBE_MAP_NEGATIVE_Z);
		
		private final int value;
		
		Target(int value) {
			this.value = value;
		}
		
		public int getValue() {
			return value;
		}
		
		public static boolean isValid(int value) {
			return EnumUtils.hasIntValue(values(), value);
		}
		
	}
	
	private static ByteBuffer[] imagesToPixels(Image[] images) {
		ByteBuffer[] pixels = new ByteBuffer[images.length];
		for (int i = 0; i < images.length; i++) {
			pixels[i] = images[i].getPixels();
		}
		return pixels;
	}
	
}
