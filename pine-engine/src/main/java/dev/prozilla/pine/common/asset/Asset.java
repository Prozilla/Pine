package dev.prozilla.pine.common.asset;

import dev.prozilla.pine.common.Holdable;

/**
 * Represents a type of asset, like images, audio or text.
 */
public interface Asset extends Holdable {
	
	/**
	 * Returns the path to the file this asset was loaded from,
	 * or {@code null} if this asset was not loaded from a file.
	 * @return The path of this asset.
	 */
	String getPath();
	
	/**
	 * Removes this asset from the asset pool and deletes it.
	 */
	@Override
	void destroy();
	
}
