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
	
	public Image(ByteBuffer image, int width, int height) {
		this.buffer = image;
		this.width = width;
		this.height = height;
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
	
	@Override
	public void destroy() {
		stbi_image_free(buffer);
	}
}
