package dev.prozilla.pine.common.logging;

import dev.prozilla.pine.core.Application;
import dev.prozilla.pine.core.state.config.Config;

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
		Config config = application.getConfig();
		
		config.logging.enableLogs.read(() -> {
			enabled = config.logging.enableLogs.get();
		});
		config.logging.prefix.read(() -> {
			prefix = config.logging.prefix.get();
		});
		config.logging.outputHandler.read(() -> {
			outputLogHandler = config.logging.outputHandler.get();
		});
		config.logging.errorHandler.read(() -> {
			errorLogHandler = config.logging.errorHandler.get();
		});
	}
	
}
