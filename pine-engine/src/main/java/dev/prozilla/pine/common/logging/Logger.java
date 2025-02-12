package dev.prozilla.pine.common.logging;

import dev.prozilla.pine.common.Lifecycle;
import dev.prozilla.pine.common.system.Ansi;
import dev.prozilla.pine.common.system.Path;

public class Logger implements LogLayer, Lifecycle {
	
	// Options
	protected boolean enabled;
	protected String prefix;
	
	// Layers
	protected LogLayer outputLogLayer;
	protected LogLayer errorLogLayer;
	
	public static final Logger system = new Logger(new DefaultOutputLogLayer(), new DefaultOutputLogLayer())
	                                     .setPrefix(Ansi.purple("[SYSTEM] "));
	
	/**
	 * Creates a logger that is initially disabled.
	 */
	public Logger() {
		enabled = false;
	}
	
	/**
	 * Creates a logger with an output and error log layer.
	 */
	public Logger(LogLayer outputLog, LogLayer errorLog) {
		this.outputLogLayer = outputLog;
		this.errorLogLayer = errorLog;
		
		enabled = true;
	}
	
	@Override
	public void log() {
		if (!isOutputActive()) {
			return;
		}
		outputLogLayer.log();
	}
	
	@Override
	public void log(boolean x) {
		if (!isOutputActive()) {
			return;
		}
		outputLogLayer.log(x);
	}
	
	@Override
	public void log(char x) {
		if (!isOutputActive()) {
			return;
		}
		outputLogLayer.log(x);
	}
	
	@Override
	public void log(int x) {
		if (!isOutputActive()) {
			return;
		}
		outputLogLayer.log(x);
	}
	
	@Override
	public void log(long x) {
		if (!isOutputActive()) {
			return;
		}
		outputLogLayer.log(x);
	}
	
	@Override
	public void log(float x) {
		if (!isOutputActive()) {
			return;
		}
		outputLogLayer.log(x);
	}
	
	@Override
	public void log(double x) {
		if (!isOutputActive()) {
			return;
		}
		outputLogLayer.log(x);
	}
	
	@Override
	public void log(char[] x) {
		if (!isOutputActive()) {
			return;
		}
		outputLogLayer.log(x);
	}
	
	@Override
	public void log(Object x) {
		if (!isOutputActive()) {
			return;
		}
		outputLogLayer.log(x);
	}
	
	@Override
	public void logf(String format, Object... args) {
		if (!isOutputActive()) {
			return;
		}
		
		// Add prefix
		if (prefix != null) {
			format = prefix + format;
		}
		
		outputLogLayer.logf(format, args);
	}
	
	@Override
	public void log(String text) {
		if (!isOutputActive()) {
			return;
		}
		
		// Add prefix
		if (prefix != null) {
			text = prefix + text;
		}
		
		outputLogLayer.log(text);
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
		
		// Add prefix
		if (prefix != null) {
			message = prefix + message;
		}
		
		errorLogLayer.log(Ansi.red(message));
	}
	
	/**
	 * Logs the stack trace of a throwable.
	 */
	public void trace(Throwable throwable) {
		if (!isErrorActive()) {
			return;
		}
		
		// Print stack trace
		log(Ansi.red("[ERROR] " + throwable));
		StackTraceElement[] trace = throwable.getStackTrace();
		for (StackTraceElement traceElement : trace) {
			log(Ansi.red("\tat " + traceElement));
		}
	}
	
	public void logFile(String text, String filePath) {
		logf("%s: %s", text, formatPath(filePath));
	}
	
	protected boolean isOutputActive() {
		return enabled && outputLogLayer != null;
	}
	
	protected boolean isErrorActive() {
		return enabled && errorLogLayer != null;
	}
	
	public Logger setPrefix(String prefix) {
		this.prefix = prefix;
		return this;
	}
	
	public static String formatPath(String path) {
		return Path.createLink(path);
	}
	
}
