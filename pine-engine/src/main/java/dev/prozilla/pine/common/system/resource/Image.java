package dev.prozilla.pine.common.system.resource;

import dev.prozilla.pine.common.Lifecycle;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.system.MemoryUtil;

import java.nio.ByteBuffer;

import static org.lwjgl.stb.STBImage.stbi_image_free;

/**
 * Represents an STB image with a width and a height.
 */
public class Image implements Lifecycle {

	private final ByteBuffer buffer;
	private final int width;
	private final int height;
	private final int channels;
	
	public Image(ByteBuffer image, int width, int height, int channels) {
		this.buffer = image;
		this.width = width;
		this.height = height;
		this.channels = channels;
	}
	
	public ByteBuffer getFlippedImage() {
		return flipImageVertically(buffer, width, height, channels);
	}
	
	public ByteBuffer getPremultipliedImage() {
		return premultiplyAlpha(buffer, width, height, channels);
	}
	
	public ByteBuffer getImage() {
		return buffer;
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
	
	@Override
	public void destroy() {
		stbi_image_free(buffer);
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
