package dev.prozilla.pine.common.system.resource.image;

import dev.prozilla.pine.common.system.resource.Resource;
import dev.prozilla.pine.common.system.resource.ResourcePool;

/**
 * Base class for different types of textures.
 */
public interface TextureBase extends Resource {
	
	/**
	 * Binds this texture.
	 */
	void bind();
	
	/**
	 * Unbinds this texture.
	 */
	void unbind();
	
	/**
	 * Checks whether two textures are from the same location.
	 * @param other Other texture
	 * @return True if <code>other</code> is equal to this texture or in the same texture array.
	 */
	boolean hasEqualLocation(TextureBase other);
	
	int getId();
	
	/**
	 * @return The width of this texture, in pixels.
	 */
	int getWidth();
	
	/**
	 * @return The height of this texture, in pixels.
	 */
	int getHeight();
	
	/**
	 * @return The path of the image file of this texture.
	 */
	@Override
	String getPath();
	
	/**
	 * Removes this texture from the resource pool and deletes it.
	 */
	@Override
	default void destroy() {
		String path = getPath();
		if (path != null) {
			ResourcePool.removeTexture(path);
		}
	}
	
	/**
	 * @return True if this texture is part of a texture array.
	 */
	boolean isInArray();
	
	/**
	 * @return The texture array this texture is part of, or <code>null</code> if this texture is not in a texture array.
	 */
	TextureArray getArray();
	
}
