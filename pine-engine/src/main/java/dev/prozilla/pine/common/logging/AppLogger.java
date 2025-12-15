package dev.prozilla.pine.common.logging;

import dev.prozilla.pine.common.asset.Asset;
import dev.prozilla.pine.common.asset.audio.AudioSource;
import dev.prozilla.pine.common.asset.image.Image;
import dev.prozilla.pine.common.asset.image.TextureAsset;
import dev.prozilla.pine.common.asset.pool.AssetPool;
import dev.prozilla.pine.common.asset.pool.AssetPoolEvent;
import dev.prozilla.pine.common.asset.pool.AssetPools;
import dev.prozilla.pine.common.asset.text.Font;
import dev.prozilla.pine.common.event.EventListener;
import dev.prozilla.pine.common.lifecycle.Initializable;
import dev.prozilla.pine.common.logging.handler.LogHandler;
import dev.prozilla.pine.common.property.style.StyleSheet;
import dev.prozilla.pine.common.system.ResourceUtils;
import dev.prozilla.pine.core.Application;
import dev.prozilla.pine.core.state.config.LogConfig;

/**
 * Logger for the core application.
 * Automatically reads the application's config and updates the logging accordingly.
 */
public class AppLogger extends Logger implements Initializable {
	
	private final Application application;
	private LogConfig config;
	
	protected final EventListener<AssetPoolEvent<Image>> onImageLoad;
	protected final EventListener<AssetPoolEvent<TextureAsset>> onTextureLoad;
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
	 * Creates a timestamp using {@link Application#getTime()}.
	 */
	@Override
	protected String createTimestamp() {
		double time = application.getTime();
		int minutes = (int) (time / 60) % 60;
		int seconds = (int) time % 60;
		int milliseconds = (int) ((time * 1000) % 1000);
		return String.format("%02d:%02d:%03d", minutes, seconds, milliseconds);
	}
	
	/**
	 * Initializes this logger by reading the application's configuration and creating listeners.
	 */
	@Override
	public void init() {
		config = application.getConfig().logging;
		
		config.enableLogs.read((enabled) -> this.enabled = enabled);
		config.prefix.read((prefix) -> this.prefix = prefix);
		config.outputHandler.read((outputLogHandler) -> this.outputLogHandler = outputLogHandler);
		config.errorHandler.read((errorLogHandler) -> this.errorLogHandler = errorLogHandler);
		config.errorHandler.read((warningLogHandler) -> this.warningLogHandler = warningLogHandler);
		config.enableAnsi.read((enableAnsi) -> this.enableAnsi = enableAnsi);
		config.enableTimestamps.read((enableTimestamps) -> this.enableTimestamps = enableTimestamps);
		
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
		assetPool.addListener(AssetPoolEvent.Type.LOADING, listener);
		assetPool.addListener(AssetPoolEvent.Type.FAILED, listener);
	}
	
	private <T extends Asset> void removeAssetPoolListener(AssetPool<T> assetPool, EventListener<AssetPoolEvent<T>> listener) {
		assetPool.removeListener(AssetPoolEvent.Type.LOADING, listener);
		assetPool.removeListener(AssetPoolEvent.Type.FAILED, listener);
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
	
	/**
	 * Enables/disables this logger by setting the value of {@link LogConfig#enableLogs}.
	 * @param enabled Whether to enable or disable this logger
	 * @return This logger.
	 */
	@Override
	public AppLogger setEnabled(boolean enabled) {
		config.enableLogs.set(enabled);
		return this;
	}
	
	/**
	 * Sets the prefix of this logger by setting the value of {@link LogConfig#prefix}.
	 * @param prefix The prefix to add to all logs.
	 * @return This logger.
	 */
	@Override
	public AppLogger setPrefix(String prefix) {
		config.prefix.setValue(prefix);
		return this;
	}
	
	/**
	 * Sets the log handler for the output log level by setting the value of {@link LogConfig#outputHandler}.
	 * @param outputLogHandler The log handler
	 * @return This logger.
	 */
	@Override
	public AppLogger setOutputHandler(LogHandler outputLogHandler) {
		config.outputHandler.setValue(outputLogHandler);
		return this;
	}
	
	/**
	 * Sets the log handler for the output log level by setting the value of {@link LogConfig#errorHandler}.
	 * @param errorLogHandler The log handler
	 * @return This logger.
	 */
	@Override
	public AppLogger setErrorHandler(LogHandler errorLogHandler) {
		config.errorHandler.setValue(outputLogHandler);
		return this;
	}
	
	/**
	 * Sets the log handler for the output log level by setting the value of {@link LogConfig#warningHandler}.
	 * @param warningLogHandler The log handler
	 * @return This logger.
	 */
	@Override
	public AppLogger setWarningHandler(LogHandler warningLogHandler) {
		config.warningHandler.setValue(outputLogHandler);
		return this;
	}
	
	/**
	 * Enables/disables ANSI escape sequences in logs by setting the value of {@link LogConfig#enableAnsi}.
	 * @param ansiEnabled Whether to enable or disable ANSI
	 * @return This logger.
	 */
	@Override
	public AppLogger setAnsiEnabled(boolean ansiEnabled) {
		config.enableAnsi.set(ansiEnabled);
		return this;
	}
	
	/**
	 * Enables/disables timestamps in logs by setting the value of {@link LogConfig#enableTimestamps}.
	 * @param timestampsEnabled Whether to enable or disable timestamps
	 * @return This logger.
	 */
	@Override
	public AppLogger setTimestampsEnabled(boolean timestampsEnabled) {
		config.enableTimestamps.set(timestampsEnabled);
		return this;
	}
	
	/**
	 * Enables/disables logs related to asset pools by setting the value of {@link LogConfig#enableAssetPoolLogs}.
	 * @param assetPoolLogsEnabled Whether to enable or disable asset pool logs
	 * @return This logger.
	 */
	public AppLogger setAssetPoolLogsEnabled(boolean assetPoolLogsEnabled) {
		config.enableAssetPoolLogs.set(assetPoolLogsEnabled);
		return this;
	}
	
}
