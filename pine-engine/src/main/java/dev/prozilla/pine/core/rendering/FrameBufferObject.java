package dev.prozilla.pine.core.rendering;

import dev.prozilla.pine.common.Lifecycle;
import dev.prozilla.pine.common.system.resource.Texture;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;

/**
 * Represents an OpenGL frame buffer.
 */
public class FrameBufferObject implements Lifecycle {
	
	private final int id;
	private Texture texture;
	private final int width;
	private final int height;
	
	public FrameBufferObject(int width, int height) {
		this.width = width;
		this.height = height;
		
		// Generate frame buffer
		id = glGenFramebuffers();
	}
	
	/**
	 * Initializes this frame buffer object with a texture.
	 */
	@Override
	public void init() {
		bind();
		
		// Create texture to use as color attachment
		texture = new Texture();
		texture.setWidth(width);
		texture.setHeight(height);
		
		texture.bind();
		texture.uploadData(width, height, null);
		texture.setParameter(GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		texture.setParameter(GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		
		// Attach texture to frame buffer
		glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, texture.id, 0);
		
		checkStatus();
		unbind();
	}
	
	public void bind() {
		glBindFramebuffer(GL_FRAMEBUFFER, id);
		glViewport(0, 0, width, height);
	}
	
	public void unbind() {
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
	}
	
	private void checkStatus() throws RuntimeException {
		if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
			throw new RuntimeException("Frame buffer is not complete");
		}
	}
	
	/**
	 * Deletes this frame buffer and its texture.
	 */
	@Override
	public void destroy() {
		glDeleteFramebuffers(id);
		texture.destroy();
	}
	
	public Texture getTexture() {
		return texture;
	}
	
	public int getId() {
		return id;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
}
