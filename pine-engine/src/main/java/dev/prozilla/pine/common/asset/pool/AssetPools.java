package dev.prozilla.pine.common.asset.pool;

import dev.prozilla.pine.common.logging.Logger;

/**
 * Contains asset pools that manage assets efficiently,
 * by avoiding loading assets multiple times.
 */
public final class AssetPools {
	
	// Pools
	public static final ImagePool images = new ImagePool();
	public static final TexturePool textures = new TexturePool(images);
	public static final FontPool fonts = new FontPool();
	public static final StyleSheetPool styleSheets = new StyleSheetPool();
	public static final AudioSourcePool audioSources = new AudioSourcePool();
	
	/**
	 * Clears all asset pools.
	 */
	public static void clear() {
		images.clear();
		textures.clear();
		fonts.clear();
		styleSheets.clear();
		audioSources.clear();
	}
	
	/**
	 * Logs the amount of each type of asset in the asset pool.
	 */
	public static void printInfo() {
		printInfo(Logger.system);
	}
	
	/**
	 * Logs the amount of each type of asset in the asset pool.
	 */
	public static void printInfo(Logger logger) {
		logger.log("Resource pool summary:");
		logger.log("Images: " + images.count());
		logger.log("Textures: " + textures.count());
		logger.log("Texture arrays: " + textures.textureArrayCount());
		logger.log("Fonts: " + fonts.count());
		logger.log("Style sheets: " + styleSheets.count());
		logger.log("Audio sources: " + audioSources.count());
	}
	
}
