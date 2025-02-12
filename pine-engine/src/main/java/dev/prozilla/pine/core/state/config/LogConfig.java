package dev.prozilla.pine.core.state.config;

import dev.prozilla.pine.common.logging.LogLayer;
import dev.prozilla.pine.common.logging.SystemErrorLogLayer;
import dev.prozilla.pine.common.logging.SystemOutputLogLayer;

/**
 * Manages configuration options related to logging.
 */
public class LogConfig {
	
	// Predefined keys
	public static final ConfigKey<Boolean> ENABLE_LOGS = new ConfigKey<>("enableLogs", Boolean.class);
	public static final ConfigKey<String> PREFIX = new ConfigKey<>("prefix", String.class);
	public static final ConfigKey<LogLayer> OUTPUT_LAYER = new ConfigKey<>("outputLayer", LogLayer.class);
	public static final ConfigKey<LogLayer> ERROR_LAYER = new ConfigKey<>("errorLayer", LogLayer.class);
	
	// Predefines options
	/** Defaults to <code>true</code>. */
	public final ConfigOption<Boolean> enableLogs = new ConfigOption<>(true);
	/** Defaults to <code>null</code>. */
	public final ConfigOption<String> prefix = new ConfigOption<>(null);
	public final ConfigOption<LogLayer> outputLayer = new ConfigOption<>(new SystemOutputLogLayer());
	public final ConfigOption<LogLayer> errorLayer = new ConfigOption<>(new SystemErrorLogLayer());
	
}
