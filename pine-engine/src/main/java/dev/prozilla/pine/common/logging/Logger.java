package dev.prozilla.pine.common.logging;

import dev.prozilla.pine.common.Lifecycle;
import dev.prozilla.pine.core.Application;
import dev.prozilla.pine.core.state.config.Config;

public class Logger implements LogLayer, Lifecycle {
	
	// Options
	private boolean enabled;
	private String prefix;
	
	// Layers
	private LogLayer outputLogLayer;
	private LogLayer errorLogLayer;
	
	private final Application application;
	
	public Logger(Application application) {
		this.application = application;
		
		enabled = false;
	}
	
	public void init() {
		Config config = application.getConfig();
		
		config.logging.enableLogs.read(() -> {
			enabled = config.logging.enableLogs.get();
		});
		config.logging.prefix.read(() -> {
			prefix = config.logging.prefix.get();
		});
		config.logging.outputLayer.read(() -> {
			outputLogLayer = config.logging.outputLayer.get();
		});
		config.logging.errorLayer.read(() -> {
			errorLogLayer = config.logging.errorLayer.get();
		});
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
		
		errorLogLayer.log(message);
	}
	
	/**
	 * Logs the stack trace of a throwable.
	 */
	public void trace(Throwable throwable) {
		if (!isErrorActive()) {
			return;
		}
		
		// Print stack trace
		log(throwable);
		StackTraceElement[] trace = throwable.getStackTrace();
		for (StackTraceElement traceElement : trace) {
			log("\tat " + traceElement);
		}
	}
	
	private boolean isOutputActive() {
		return enabled && outputLogLayer != null;
	}
	
	private boolean isErrorActive() {
		return enabled && errorLogLayer != null;
	}
}
