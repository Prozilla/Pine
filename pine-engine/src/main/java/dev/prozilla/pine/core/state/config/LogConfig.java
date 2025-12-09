package dev.prozilla.pine.core.state.config;

import dev.prozilla.pine.common.logging.Logger;
import dev.prozilla.pine.common.logging.handler.LogHandler;
import dev.prozilla.pine.common.logging.handler.StandardErrorLogHandler;
import dev.prozilla.pine.common.logging.handler.StandardOutputLogHandler;
import dev.prozilla.pine.common.system.Ansi;
import dev.prozilla.pine.core.state.config.option.BooleanConfigOption;
import dev.prozilla.pine.core.state.config.option.ObjectConfigOption;
import dev.prozilla.pine.core.state.config.option.StringConfigOption;

/**
 * Manages configuration options related to logging.
 */
public class LogConfig {
	
	// Predefined keys
	public static final ConfigKey<Boolean> ENABLE_LOGS = new ConfigKey<>("enableLogs", Boolean.class);
	public static final ConfigKey<String> PREFIX = new ConfigKey<>("prefix", String.class);
	public static final ConfigKey<LogHandler> OUTPUT_LAYER = new ConfigKey<>("outputLayer", LogHandler.class);
	public static final ConfigKey<LogHandler> ERROR_LAYER = new ConfigKey<>("errorLayer", LogHandler.class);
	public static final ConfigKey<Boolean> ENABLE_ANSI = new ConfigKey<>("enableAnsi", Boolean.class);
	public static final ConfigKey<Boolean> ENABLE_APPLICATION_STATE_LOGS = new ConfigKey<>("enableApplicationStateLogs", Boolean.class);
	public static final ConfigKey<Boolean> ENABLE_ASSET_POOL_LOGS = new ConfigKey<>("enableAssetPoolLogs", Boolean.class);
	public static final ConfigKey<Boolean> ENABLE_TIMESTAMPS = new ConfigKey<>("enableTimestamps", Boolean.class);
	
	// Predefines options
	/** Enables logs. Defaults to <code>true</code>. */
	public final BooleanConfigOption enableLogs = new BooleanConfigOption(true);
	/** Prefix to add to all logged strings. Defaults to a formatted badge with label <code>"app"</code>. */
	public final StringConfigOption prefix = new StringConfigOption(Logger.formatBadge("app", Ansi.CYAN));
	/** Log handler for the output log level. Defaults to {@link StandardOutputLogHandler}. */
	public final ObjectConfigOption<LogHandler> outputHandler = new ObjectConfigOption<>(new StandardOutputLogHandler());
	/** Log handler for the error log level. Defaults to {@link StandardErrorLogHandler}. */
	public final ObjectConfigOption<LogHandler> errorHandler = new ObjectConfigOption<>(new StandardErrorLogHandler());
	/** When set to <code>false</code>, all ANSI escape sequences will be stripped from logs. Defaults to <code>true</code>. */
	public final BooleanConfigOption enableAnsi = new BooleanConfigOption(true);
	/** Enables logging of state changes of the application. Defaults to <code>true</code>. */
	public final BooleanConfigOption enableApplicationStateLogs = new BooleanConfigOption(true);
	/** Enables logging of asset pool events. Defaults to <code>true</code>. */
	public final BooleanConfigOption enableAssetPoolLogs = new BooleanConfigOption(true);
	/** Determines whether each log will be preceded by a timestamp. Defaults to {@code false}. */
	public final BooleanConfigOption enableTimestamps = new BooleanConfigOption(false);
	
}
