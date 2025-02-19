package dev.prozilla.pine.common.logging;

import dev.prozilla.pine.common.Lifecycle;
import dev.prozilla.pine.common.logging.handler.LogHandler;
import dev.prozilla.pine.common.logging.handler.StandardOutputLogHandler;
import dev.prozilla.pine.common.system.Ansi;
import dev.prozilla.pine.common.system.PathUtils;

/**
 * Represents the main access points for logging.
 * Manages different log levels, each with their own log handler, and formats logs.
 */
public class Logger implements LogHandler, Lifecycle {
	
	// Formatting options
	protected boolean enabled;
	protected String prefix;
	protected boolean enableAnsi;
	
	// Handlers
	protected LogHandler outputLogHandler;
	protected LogHandler errorLogHandler;
	
	/**
	 * The global system logger for writing things to the console.
	 * Equivalent of {@link System#out} and {@link System#err}.
	 */
	public static final Logger system = new Logger(new StandardOutputLogHandler(), new StandardOutputLogHandler())
		.setPrefix(Ansi.purple(formatBadge("system")));
	
	/**
	 * Creates a logger that is initially disabled.
	 */
	public Logger() {
		enabled = false;
		enableAnsi = true;
	}
	
	/**
	 * Creates a logger with an output and error log level.
	 */
	public Logger(LogHandler outputLogHandler, LogHandler errorLogHandler) {
		this.outputLogHandler = outputLogHandler;
		this.errorLogHandler = errorLogHandler;
		
		enabled = true;
		enableAnsi = true;
	}
	
	@Override
	public void log() {
		if (!isOutputActive()) {
			return;
		}
		outputLogHandler.log();
	}
	
	@Override
	public void log(boolean x) {
		if (!isOutputActive()) {
			return;
		}
		outputLogHandler.log(x);
	}
	
	@Override
	public void log(char x) {
		if (!isOutputActive()) {
			return;
		}
		outputLogHandler.log(x);
	}
	
	@Override
	public void log(int x) {
		if (!isOutputActive()) {
			return;
		}
		outputLogHandler.log(x);
	}
	
	@Override
	public void log(long x) {
		if (!isOutputActive()) {
			return;
		}
		outputLogHandler.log(x);
	}
	
	@Override
	public void log(float x) {
		if (!isOutputActive()) {
			return;
		}
		outputLogHandler.log(x);
	}
	
	@Override
	public void log(double x) {
		if (!isOutputActive()) {
			return;
		}
		outputLogHandler.log(x);
	}
	
	@Override
	public void log(char[] x) {
		if (!isOutputActive()) {
			return;
		}
		outputLogHandler.log(x);
	}
	
	@Override
	public void log(Object x) {
		if (!isOutputActive()) {
			return;
		}
		outputLogHandler.log(x);
	}
	
	@Override
	public void logf(String format, Object... args) {
		if (!isOutputActive()) {
			return;
		}
		outputLogHandler.logf(applyFormat(format), args);
	}
	
	@Override
	public void log(String text) {
		if (!isOutputActive()) {
			return;
		}
		outputLogHandler.log(applyFormat(text));
	}
	
	/**
	 * Logs an error message and a stack trace.
	 */
	public void error(String message, Throwable throwable) {
		error(message);
		trace(throwable);
	}
	
	/**
	 * Logs an error message.
	 */
	public void error(String message) {
		if (!isErrorActive()) {
			return;
		}
		errorLogHandler.log(applyFormat(Ansi.red(message)));
	}
	
	private String applyFormat(String text) {
		// Add prefix
		if (prefix != null) {
			text = prefix + text;
		}
		
		// Strip ANSI, if ANSI is disabled
		if (!enableAnsi) {
			text = Ansi.strip(text);
		}
		
		return text;
	}
	
	/**
	 * Logs the stack trace of a throwable.
	 */
	public void trace(Throwable throwable) {
		if (!isErrorActive()) {
			return;
		}
		
		// Print stack trace
		log(Ansi.red(formatBadge("error") + throwable));
		StackTraceElement[] trace = throwable.getStackTrace();
		for (StackTraceElement traceElement : trace) {
			log(Ansi.red("\tat " + traceElement));
		}
	}
	
	public void logPath(String text, String filePath) {
		logf("%s: %s", text, formatPath(filePath));
	}
	
	protected boolean isOutputActive() {
		return enabled && outputLogHandler != null;
	}
	
	protected boolean isErrorActive() {
		return enabled && errorLogHandler != null;
	}
	
	/**
	 * Enables or disables this logger.
	 * @param enabled If <code>true</code>, the logger will be enabled.
	 */
	public Logger setEnabled(boolean enabled) {
		this.enabled = enabled;
		return this;
	}
	
	/**
	 * Sets the prefix of this logger.
	 * @param prefix Prefix to add to all logged strings.
	 */
	public Logger setPrefix(String prefix) {
		this.prefix = prefix;
		return this;
	}
	
	public Logger enableAnsi() {
		enableAnsi = true;
		return this;
	}
	
	public Logger disableAnsi() {
		enableAnsi = false;
		return this;
	}
	
	public static String formatPath(String path) {
		return PathUtils.createLink(path);
	}
	
	public static String formatBadge(String label) {
		return String.format("[%S] ", label);
	}
	
}
