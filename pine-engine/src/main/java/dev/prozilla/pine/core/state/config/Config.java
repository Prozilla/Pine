package dev.prozilla.pine.core.state.config;

import dev.prozilla.pine.common.logging.Logger;
import dev.prozilla.pine.core.Application;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Manages configuration of the application.
 */
public class Config {
	
	private final Map<ConfigKey<?>, ConfigOption<?>> options;
	
	// Predefined keys
	public static final ConfigKey<Integer> FPS = new ConfigKey<>("fps", Integer.class);
	
	// Predefines options
	/** Target frames per second. When set to <code>0</code>, fps is uncapped. Defaults to <code>120</code>. */
	public final ConfigOption<Integer> fps = new ConfigOption<>(120, (fps) -> fps >= 0);
	/** Options related to rendering. */
	public final RenderConfig rendering = new RenderConfig();
	/** Options related to logging. */
	public final LogConfig logging = new LogConfig();
	/** Options related to the application window. */
	public final WindowConfig window = new WindowConfig();
	
	private final Logger logger;
	
	public Config(Application application) {
		logger = application.getLogger();
		
		options = new HashMap<>();
		
		// Add predefined options
		addOption(FPS, fps);
		
		addOption(RenderConfig.FALLBACK_RENDER_COLOR, rendering.fallbackRenderColor);
		addOption(RenderConfig.ENABLE_BLEND, rendering.enableBlend);
		addOption(RenderConfig.ENABLE_DEPTH_TEST, rendering.enableDepthTest);
		addOption(RenderConfig.RENDER_MODE, rendering.renderMode);
		
		addOption(LogConfig.ENABLE_LOGS, logging.enableLogs);
		addOption(LogConfig.PREFIX, logging.prefix);
		addOption(LogConfig.OUTPUT_LAYER, logging.outputHandler);
		addOption(LogConfig.ERROR_LAYER, logging.errorHandler);
		
		addOption(WindowConfig.SHOW_DECORATIONS, window.showDecorations);
		addOption(WindowConfig.FULLSCREEN, window.fullscreen);
		addOption(WindowConfig.ENABLE_VSYNC, window.enableVSync);
	}
	
	/**
	 * Adds a new config option with a given key.
	 * @param key Key of the config option
	 * @param option Config option
	 * @param <T> Type of the option
	 * @throws IllegalStateException If <code>key</code> is already associated with an option.
	 */
	public <T> void addOption(ConfigKey<T> key, ConfigOption<T> option) throws IllegalStateException {
		if (options.containsKey(key)) {
			throw new IllegalStateException(String.format("option with key '%s' already exists", key));
		}
		
		option.setLogger(logger);
		
		options.put(key, option);
	}
	
	/**
	 * Checks whether this config has an option with a given key.
	 * @param key Key of the option
	 */
	public boolean hasOption(ConfigKey<?> key) {
		return options.containsKey(key);
	}
	
	/**
	 * Returns the value of an option.
	 * @param key Key of the config option
	 * @return Value of the option associated with <code>key</code>
	 * @param <T> Type of the option
	 */
	public <T> T getOption(ConfigKey<T> key) {
		@SuppressWarnings("unchecked")
		ConfigOption<T> option = (ConfigOption<T>)options.get(key);
		
		return option.get();
	}
	
	/**
	 * Sets the value of an option.
	 * @param key Key of the config option
	 * @param value New value for the option associated with <code>key</code>
	 * @param <T> Type of the option
	 * @throws IllegalArgumentException If the option has a validator that evaluates to <code>false</code> for <code>value</code>.
	 */
	public <T> void setOption(ConfigKey<T> key, T value) throws IllegalArgumentException {
		@SuppressWarnings("unchecked")
		ConfigOption<T> option = (ConfigOption<T>)options.get(key);
		
		if (!option.isValidValue(value)) {
			throw new IllegalArgumentException(String.format("Invalid value for option with key '%s': %s", key, value));
		}
		
		option.set(value);
	}
	
	/**
	 * Resets an option to its initial value.
	 * @param key Key of the config option
	 */
	public void resetOption(ConfigKey<?> key) {
		ConfigOption<?> option = options.get(key);
		option.reset();
	}
	
	public Collection<ConfigKey<?>> getKeys() {
		return options.keySet();
	}
	
	/**
	 * Returns the collection of options in this configuration.
	 */
	public Collection<ConfigOption<?>> getOptions() {
		return options.values();
	}
	
	/**
	 * Copies all options from another config.
	 * @param otherConfig Config to copy from
	 */
	public void copyFrom(Config otherConfig) {
		copyFrom(otherConfig, otherConfig.getKeys());
	}
	
	/**
	 * Copies all options from another config based on a collection of keys.
	 * @param otherConfig The config to copy from
	 * @param keys The keys of the options to copy
	 */
	public void copyFrom(Config otherConfig, Collection<ConfigKey<?>> keys) {
		for (ConfigKey<?> key : keys) {
			if (hasOption(key) && otherConfig.hasOption(key)) {
				copyFrom(otherConfig, key);
			}
		}
	}
	
	/**
	 * Copies an option with a given key from another config.
	 * @param otherConfig Config to copy from
	 * @param key Key of the option to copy
	 */
	public <T> void copyFrom(Config otherConfig, ConfigKey<T> key) {
		@SuppressWarnings("unchecked")
		ConfigOption<T> option = (ConfigOption<T>)options.get(key);
		option.set(otherConfig.getOption(key));
	}
	
	/**
	 * Resets all config options to their initial values.
	 */
	public void reset() {
		for (ConfigOption<?> option : options.values()) {
			option.reset();
		}
	}
}
