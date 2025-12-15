package dev.prozilla.pine.core.state.config;

import dev.prozilla.pine.common.logging.AppLogger;
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
	public static final ConfigKey<LogHandler> OUTPUT_LAYER = new ConfigKey<>("outputHandler", LogHandler.class);
	public static final ConfigKey<LogHandler> ERROR_LAYER = new ConfigKey<>("errorHandler", LogHandler.class);
	public static final ConfigKey<LogHandler> WARN_LAYER = new ConfigKey<>("warningHandler", LogHandler.class);
	public static final ConfigKey<Boolean> ENABLE_ANSI = new ConfigKey<>("enableAnsi", Boolean.class);
	public static final ConfigKey<Boolean> ENABLE_APPLICATION_STATE_LOGS = new ConfigKey<>("enableApplicationStateLogs", Boolean.class);
	public static final ConfigKey<Boolean> ENABLE_ASSET_POOL_LOGS = new ConfigKey<>("enableAssetPoolLogs", Boolean.class);
	public static final ConfigKey<Boolean> ENABLE_TIMESTAMPS = new ConfigKey<>("enableTimestamps", Boolean.class);
	
	// Predefines options
	/** Enables logs. Defaults to <code>true</code>. The shorthand for this option is {@link AppLogger#setEnabled(boolean)}. */
	public final BooleanConfigOption enableLogs = new BooleanConfigOption(true);
	/** Prefix to add to all logged strings. Defaults to a formatted badge with label <code>"app"</code>. The shorthand for this option is {@link AppLogger#setPrefix(String)}. */
	public final StringConfigOption prefix = new StringConfigOption(Logger.formatBadge("app", Ansi.CYAN));
	/** Log handler for the output log level. Settings the value to {@code null} disables output logs. Defaults to {@link StandardOutputLogHandler}. The shorthand for this option is {@link AppLogger#setOutputHandler(LogHandler)}. */
	public final ObjectConfigOption<LogHandler> outputHandler = new ObjectConfigOption<>(new StandardOutputLogHandler());
	/** Log handler for the error log level. Settings the value to {@code null} disables error logs. Defaults to {@link StandardErrorLogHandler}. The shorthand for this option is {@link AppLogger#setErrorHandler(LogHandler)}. */
	public final ObjectConfigOption<LogHandler> errorHandler = new ObjectConfigOption<>(new StandardErrorLogHandler());
	/** Log handler for the warning log level. Settings the value to {@code null} disables warning logs. Defaults to {@link StandardOutputLogHandler}. The shorthand for this option is {@link AppLogger#setWarningHandler(LogHandler)}. */
	public final ObjectConfigOption<LogHandler> warningHandler = new ObjectConfigOption<>(new StandardOutputLogHandler());
	/** When set to <code>false</code>, all ANSI escape sequences will be stripped from logs. Defaults to {@code true}. The shorthand for this option is {@link AppLogger#setAnsiEnabled(boolean)}. */
	public final BooleanConfigOption enableAnsi = new BooleanConfigOption(true);
	/** Enables logging of state changes of the application. Defaults to {@code true}. */
	public final BooleanConfigOption enableApplicationStateLogs = new BooleanConfigOption(true);
	/** Enables logging of asset pool events. Defaults to {@code true}. The shorthand for this option is {@link AppLogger#setAssetPoolLogsEnabled(boolean)}. */
	public final BooleanConfigOption enableAssetPoolLogs = new BooleanConfigOption(true);
	/** Determines whether each log will be preceded by a timestamp. Defaults to {@code false}. The shorthand for this option is {@link AppLogger#setTimestampsEnabled(boolean)}. */
	public final BooleanConfigOption enableTimestamps = new BooleanConfigOption(false);
	
}
