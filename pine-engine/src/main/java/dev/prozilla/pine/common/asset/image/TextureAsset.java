package dev.prozilla.pine.common.asset.image;

import dev.prozilla.pine.common.asset.Asset;
import dev.prozilla.pine.common.asset.pool.AssetPools;

/**
 * Base class for different types of texture assets.
 *
 * <p>This can be used for both simple texture and layers inside texture arrays.</p>
 */
public interface TextureAsset extends TextureBase, Asset {
	
	/**
	 * Checks whether two textures are from the same location.
	 * @param other Other texture
	 * @return True if <code>other</code> is equal to this texture or in the same texture array.
	 */
	boolean hasEqualLocation(TextureAsset other);
	
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
		AssetPools.textures.remove(this);
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
