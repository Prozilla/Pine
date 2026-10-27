package dev.prozilla.pine.core.rendering.light;

import dev.prozilla.pine.common.asset.image.Texture;
import dev.prozilla.pine.common.exception.GLException;
import dev.prozilla.pine.common.lifecycle.Destructible;
import dev.prozilla.pine.common.lifecycle.Initializable;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL30.*;

/**
 * Frame buffer that stores the depth maps of the cascade shadows.
 */
public class ShadowBuffer implements Initializable, Destructible {
	
	private final int id;
	private final int size;
	private final Texture[] depthMaps;
	
	/** The first texture unit that the depth maps are bound to. Unit 0 is used by textures and cubemaps, unit 1 by texture arrays. */
	public static final int FIRST_UNIT = 2;
	
	public ShadowBuffer(int size) {
		this.size = size;
		id = glGenFramebuffers();
		depthMaps = new Texture[CascadeShadow.COUNT];
	}
	
	@Override
	public void init() {
		for (int i = 0; i < depthMaps.length; i++) {
			// Create texture, then re-specify its storage as a depth texture
			// (NEAREST because the depth is compared manually in the shader)
			Texture depthMap = new Texture(size, size, Texture.Wrap.CLAMP_TO_EDGE, Texture.Filter.NEAREST);
			depthMap.bind();
			depthMap.uploadData(GL_DEPTH_COMPONENT32F, size, size, GL_DEPTH_COMPONENT, GL_FLOAT, null);
			depthMap.unbind();
			depthMaps[i] = depthMap;
		}
		
		// Depth-only frame buffer
		glBindFramebuffer(GL_FRAMEBUFFER, id);
		glFramebufferTexture2D(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_TEXTURE_2D, depthMaps[0].getId(), 0);
		glDrawBuffer(GL_NONE);
		glReadBuffer(GL_NONE);
		checkStatus();
		unbind();
	}
	
	/**
	 * Binds this frame buffer to render to the depth map of a cascade, and clears that depth map.
	 */
	public void bind(int cascade) {
		glBindFramebuffer(GL_FRAMEBUFFER, id);
		glFramebufferTexture2D(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_TEXTURE_2D, depthMaps[cascade].getId(), 0);
		glViewport(0, 0, size, size);
		glClear(GL_DEPTH_BUFFER_BIT);
	}
	
	public void unbind() {
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
	}
	
	/**
	 * Binds the depth maps for sampling, starting at {@link #FIRST_UNIT}.
	 */
	public void bindTextures() {
		for (int i = 0; i < depthMaps.length; i++) {
			glActiveTexture(GL_TEXTURE0 + FIRST_UNIT + i);
			glBindTexture(GL_TEXTURE_2D, depthMaps[i].getId());
		}
		// Leave unit 0 active, like the other textures do
		glActiveTexture(GL_TEXTURE0);
	}
	
	private void checkStatus() throws GLException {
		if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
			throw new GLException("Shadow frame buffer is not complete");
		}
	}
	
	@Override
	public void destroy() {
		glDeleteFramebuffers(id);
		for (Texture depthMap : depthMaps) {
			if (depthMap != null) {
				depthMap.destroy();
			}
		}
	}
	
	public int getSize() {
		return size;
	}
	
}