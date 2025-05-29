package dev.prozilla.pine.common.asset;

import dev.prozilla.pine.common.lifecycle.Destructible;

/**
 * Represents a type of asset, like images, audio or text.
 */
public interface Asset extends Destructible {
	
	/**
	 * Returns the path to the file this asset was loaded from.
	 * @return The path of this asset.
	 */
	String getPath();
	
	/**
	 * Removes this asset from the asset pool and deletes it.
	 */
	@Override
	void destroy();
	
}
