package dev.prozilla.pine.common.asset.image;

import static org.lwjgl.opengl.GL11.*;

/**
 * Base class for all kinds of textures.
 */
public interface TextureBase {
	
	/**
	 * Binds this texture.
	 */
	void bind();
	
	/**
	 * Unbinds this texture.
	 */
	void unbind();
	
	/**
	 * Sets the wrap parameter of this texture.
	 */
	default void setWrap(Texture.Wrap wrap) {
		setParameter(GL_TEXTURE_WRAP_S, wrap.getValue());
		setParameter(GL_TEXTURE_WRAP_T, wrap.getValue());
	}
	
	/**
	 * Sets the filter parameter of this texture.
	 */
	default void setFilter(Texture.Filter filter) {
		setParameter(GL_TEXTURE_MIN_FILTER, filter.getValue());
		setParameter(GL_TEXTURE_MAG_FILTER, filter.getValue());
	}
	
	/**
	 * Sets the value of a parameter of this texture.
	 * @param name The name of the parameter
	 * @param value The value to assign
	 */
	void setParameter(int name, int value);
	
	int getId();
	
	/**
	 * Returns the width of this texture.
	 * @return The width of this texture, in pixels.
	 */
	int getWidth();
	
	/**
	 * Returns the height of this texture.
	 * @return The height of this texture, in pixels.
	 */
	int getHeight();
	
}
