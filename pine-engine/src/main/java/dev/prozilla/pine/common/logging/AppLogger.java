package dev.prozilla.pine.common.logging;

import dev.prozilla.pine.core.Application;
import dev.prozilla.pine.core.state.config.Config;

public class AppLogger extends Logger {
	
	private final Application application;
	
	public AppLogger(Application application) {
		super();
		this.application = application;
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
	
}
