package dev.prozilla.pine.common.asset.image;

import dev.prozilla.pine.common.Cloneable;
import dev.prozilla.pine.common.asset.Asset;
import dev.prozilla.pine.common.asset.pool.AssetPools;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.system.MemoryUtil;

import java.nio.ByteBuffer;

import static org.lwjgl.stb.STBImage.stbi_image_free;

/**
 * Represents an STB image with a width and a height.
 */
public class Image implements Asset, Cloneable<Image> {

	public int id;
	private final ByteBuffer pixels;
	private final int width;
	private final int height;
	private final int channels;
	private final String path;
	
	private int holderCount;
	
	private static int lastId = 0;
	
	public static final int INVALID_ID = -1;
	
	public Image(String path, ByteBuffer pixels, int width, int height, int channels) {
		this.path = path;
		this.pixels = pixels;
		this.width = width;
		this.height = height;
		this.channels = channels;
		
		holderCount = 0;
		
		id = lastId++;
	}
	
	public ByteBuffer getFlippedImage() {
		return flipImageVertically(pixels, width, height, channels);
	}
	
	public ByteBuffer getPremultipliedImage() {
		return premultiplyAlpha(pixels, width, height, channels);
	}
	
	public ByteBuffer getPixels() {
		return pixels;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public int getChannels() {
		return channels;
	}
	
	public String getPath() {
		return path;
	}
	
	@Override
	public void hold() {
		holderCount++;
	}
	
	@Override
	public void drop() {
		holderCount--;
		checkHoldState();
	}
	
	@Override
	public int getHolderCount() {
		return holderCount;
	}
	
	@Override
	public void destroy() {
		if (id == INVALID_ID) {
			return;
		}
		AssetPools.images.remove(this);
		stbi_image_free(pixels);
		id = INVALID_ID;
	}
	
	@Override
	public boolean equals(Object object) {
		return object == this || (object instanceof Image image && equals(image));
	}
	
	@Override
	public boolean equals(Image image) {
		return image != null && image.id == id;
	}
	
	@Override
	public int hashCode() {
		return id;
	}
	
	@Override
	public Image clone() {
		return new Image(path, pixels, width, height, channels);
	}
	
	public GLFWImage toGLFWImage() {
		GLFWImage glfwImage = GLFWImage.malloc();
		glfwImage.width(getWidth())
			.height(getHeight())
		    .pixels(getFlippedImage());
		
		return glfwImage;
	}
	
	public static ByteBuffer premultiplyAlpha(ByteBuffer image, int width, int height, int channels) {
		if (channels < 4) {
			return image;
		}
		
		ByteBuffer premultiplied = MemoryUtil.memAlloc(image.capacity());
		
		for (int i = 0; i < width * height; i++) {
			int index = i * 4;
			float alpha = (image.get(index + 3) & 0xFF) / 255.0f;
			
			premultiplied.put(index, (byte) ((image.get(index) & 0xFF) * alpha));
			premultiplied.put(index + 1, (byte) ((image.get(index + 1) & 0xFF) * alpha));
			premultiplied.put(index + 2, (byte) ((image.get(index + 2) & 0xFF) * alpha));
			premultiplied.put(index + 3, image.get(index + 3));
		}
		
		return premultiplied;
	}
	
	
	public static ByteBuffer flipImageVertically(ByteBuffer image, int width, int height, int channels) {
		int stride = width * channels;
		ByteBuffer flipped = MemoryUtil.memAlloc(image.capacity());
		
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < stride; x++) {
				flipped.put((height - 1 - y) * stride + x, image.get(y * stride + x));
			}
		}
		return flipped;
	}
}
