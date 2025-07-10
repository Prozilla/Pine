package dev.prozilla.pine.common.logging;

import dev.prozilla.pine.common.asset.Asset;
import dev.prozilla.pine.common.asset.audio.AudioSource;
import dev.prozilla.pine.common.asset.image.Image;
import dev.prozilla.pine.common.asset.image.TextureBase;
import dev.prozilla.pine.common.asset.pool.AssetPool;
import dev.prozilla.pine.common.asset.pool.AssetPoolEvent;
import dev.prozilla.pine.common.asset.pool.AssetPoolEventType;
import dev.prozilla.pine.common.asset.pool.AssetPools;
import dev.prozilla.pine.common.asset.text.Font;
import dev.prozilla.pine.common.event.EventListener;
import dev.prozilla.pine.common.property.style.StyleSheet;
import dev.prozilla.pine.common.system.ResourceUtils;
import dev.prozilla.pine.core.Application;
import dev.prozilla.pine.core.state.config.LogConfig;

/**
 * Logger for the core application.
 * Automatically reads the application's config and updates the logging accordingly.
 */
public class AppLogger extends Logger {
	
	private final Application application;
	
	protected final EventListener<AssetPoolEvent<Image>> onImageLoad;
	protected final EventListener<AssetPoolEvent<TextureBase>> onTextureLoad;
	protected final EventListener<AssetPoolEvent<Font>> onFontLoad;
	protected final EventListener<AssetPoolEvent<StyleSheet>> onStyleSheetLoad;
	protected final EventListener<AssetPoolEvent<AudioSource>> onAudioSourceLoad;
	
	public AppLogger(Application application) {
		this.application = application;
		
		onImageLoad = createAssetPoolListener("image");
		onTextureLoad = createAssetPoolListener("texture");
		onFontLoad = createAssetPoolListener("font");
		onStyleSheetLoad = createAssetPoolListener("style sheet");
		onAudioSourceLoad = createAssetPoolListener("audio source");
	}
	
	/**
	 * Initializes this logger by reading the application's configuration and creating listeners.
	 */
	public void init() {
		LogConfig config = application.getConfig().logging;
		
		config.enableLogs.read((enabled) -> this.enabled = enabled);
		config.prefix.read((prefix) -> this.prefix = prefix);
		config.outputHandler.read((outputLogHandler) -> this.outputLogHandler = outputLogHandler);
		config.errorHandler.read((errorLogHandler) -> this.errorLogHandler = errorLogHandler);
		config.enableAnsi.read((enableAnsi) -> this.enableAnsi = enableAnsi);
		
		config.enableAssetPoolLogs.read((enableAssetPoolLogs) -> {
			if (enableAssetPoolLogs) {
				addAssetPoolListener(AssetPools.images, onImageLoad);
				addAssetPoolListener(AssetPools.textures, onTextureLoad);
				addAssetPoolListener(AssetPools.fonts, onFontLoad);
				addAssetPoolListener(AssetPools.styleSheets, onStyleSheetLoad);
				addAssetPoolListener(AssetPools.audioSources, onAudioSourceLoad);
			} else {
				removeAssetPoolListener(AssetPools.images, onImageLoad);
				removeAssetPoolListener(AssetPools.textures, onTextureLoad);
				removeAssetPoolListener(AssetPools.fonts, onFontLoad);
				removeAssetPoolListener(AssetPools.styleSheets, onStyleSheetLoad);
				removeAssetPoolListener(AssetPools.audioSources, onAudioSourceLoad);
			}
		});
	}
	
	private <T extends Asset> void addAssetPoolListener(AssetPool<T> assetPool, EventListener<AssetPoolEvent<T>> listener) {
		assetPool.addListener(AssetPoolEventType.LOADING, listener);
		assetPool.addListener(AssetPoolEventType.FAILED, listener);
	}
	
	private <T extends Asset> void removeAssetPoolListener(AssetPool<T> assetPool, EventListener<AssetPoolEvent<T>> listener) {
		assetPool.removeListener(AssetPoolEventType.LOADING, listener);
		assetPool.removeListener(AssetPoolEventType.FAILED, listener);
	}
	
	private <T extends Asset> EventListener<AssetPoolEvent<T>> createAssetPoolListener(String assetName) {
		return (event) -> {
			if (event.hasFailed()) {
				String message = String.format("Failed to load %s: %s", assetName, event.getPath());
				String error = event.getError();
				if (error != null) {
					message += System.lineSeparator() + error;
				}
				error(message, event.getException());
			} else {
				logPath("Loading " + assetName, ResourceUtils.getResourcePath(event.getPath()));
			}
		};
	}
	
}
