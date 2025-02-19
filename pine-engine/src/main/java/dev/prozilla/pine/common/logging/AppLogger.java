package dev.prozilla.pine.common.logging;

import dev.prozilla.pine.core.Application;
import dev.prozilla.pine.core.state.config.LogConfig;

/**
 * Logger for the core application.
 * Automatically reads the application's config and updates the logging accordingly.
 */
public class AppLogger extends Logger {
	
	private final Application application;
	
	public AppLogger(Application application) {
		super();
		this.application = application;
	}
	
	/**
	 * Initializes this logger by reading the application's configuration and creating listeners.
	 */
	public void init() {
		LogConfig config = application.getConfig().logging;
		
		config.enableLogs.read(() -> enabled = config.enableLogs.get());
		config.prefix.read(() -> prefix = config.prefix.get());
		config.outputHandler.read(() -> outputLogHandler = config.outputHandler.get());
		config.errorHandler.read(() -> errorLogHandler = config.errorHandler.get());
		config.enableAnsi.read(() -> enableAnsi = config.enableAnsi.get());
	}
	
}
