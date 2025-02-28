package dev.prozilla.pine.core.state.config;

import dev.prozilla.pine.common.logging.Logger;
import dev.prozilla.pine.common.logging.handler.LogHandler;
import dev.prozilla.pine.common.logging.handler.StandardErrorLogHandler;
import dev.prozilla.pine.common.logging.handler.StandardOutputLogHandler;
import dev.prozilla.pine.common.system.Ansi;

import java.util.Objects;

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
	
	// Predefines options
	/** Defaults to <code>true</code>. */
	public final ConfigOption<Boolean> enableLogs = new ConfigOption<>(true, Objects::nonNull);
	/** Prefix to add to all logged strings. Defaults to a formatted badge with label <code>"app"</code>. */
	public final ConfigOption<String> prefix = new ConfigOption<>(Logger.formatBadge("app", Ansi.CYAN));
	/** Log handler for the output log level. Defaults to {@link StandardOutputLogHandler}. */
	public final ConfigOption<LogHandler> outputHandler = new ConfigOption<>(new StandardOutputLogHandler());
	/** Log handler for the error log level. Defaults to {@link StandardErrorLogHandler}. */
	public final ConfigOption<LogHandler> errorHandler = new ConfigOption<>(new StandardErrorLogHandler());
	/** Defaults to <code>true</code>. */
	public final ConfigOption<Boolean> enableAnsi = new ConfigOption<>(true, Objects::nonNull);
	
}
