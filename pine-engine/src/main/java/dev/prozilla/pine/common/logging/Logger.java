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
		.setPrefix(formatBadge("system", Ansi.PURPLE));
	
	/**
	 * Creates a logger without any log handlers.
	 */
	public Logger() {
		this(null);
	}
	
	/**
	 * Creates a logger with an output log handler.
	 */
	public Logger(LogHandler outputLogHandler) {
		this(outputLogHandler, null);
	}
	
	/**
	 * Creates a logger with an output and error log handler.
	 */
	public Logger(LogHandler outputLogHandler, LogHandler errorLogHandler) {
		this.outputLogHandler = outputLogHandler;
		this.errorLogHandler = errorLogHandler;
		
		enabled = outputLogHandler != null || errorLogHandler != null;
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
		if (throwable != null) {
			trace(throwable);
		}
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
	 * Redirects logs from one log level to another.
	 * @param from Log level to redirect logs from
	 * @param to Log level to redirect logs to
	 */
	public Logger redirect(LogLevel from, LogLevel to) {
		LogHandler handler = switch (to) {
			case OUTPUT -> outputLogHandler;
			case ERROR -> errorLogHandler;
		};
		
		switch (from) {
			case OUTPUT -> outputLogHandler = handler;
			case ERROR -> errorLogHandler = handler;
		}
		
		return this;
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
		return formatBadge(label, null);
	}
	
	public static String formatBadge(String label, String color) {
		if (color != null) {
			return String.format("%s[%S]%s ", color, label, Ansi.RESET);
		} else {
			return String.format("[%S] ", label);
		}
	}
	
}
