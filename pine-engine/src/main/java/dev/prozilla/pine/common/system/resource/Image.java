package dev.prozilla.pine.common.system.resource;

import dev.prozilla.pine.common.Lifecycle;

import java.nio.ByteBuffer;

import static org.lwjgl.stb.STBImage.*;

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
	
	public static ByteBuffer flipImageVertically(ByteBuffer image, int width, int height, int channels) {
		int stride = width * channels;
		ByteBuffer flipped = ByteBuffer.allocateDirect(image.capacity());
		
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < stride; x++) {
				flipped.put((height - 1 - y) * stride + x, image.get(y * stride + x));
			}
		}
		return flipped;
	}
}
