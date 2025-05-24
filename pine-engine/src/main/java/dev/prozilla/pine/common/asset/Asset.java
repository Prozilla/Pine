package dev.prozilla.pine.common.asset;

/**
 * Represents a type of asset, like images, audio or text.
 */
public interface Asset {
	
	/**
	 * Returns the path to the file this asset was loaded from.
	 * @return The path of this asset.
	 */
	String getPath();
	
	/**
	 * Removes this asset from the asset pool and deletes it.
	 */
	void destroy();
	
}
