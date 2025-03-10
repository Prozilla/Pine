package dev.prozilla.pine.core.state;

import dev.prozilla.pine.core.Application;

public enum ApplicationState implements State<Application> {
	INITIALIZING {
		@Override
		public void enter(Application application) {
			ApplicationState.logStatus(application, "Initializing application");
		}
		
		@Override
		public void exit(Application application) {
			ApplicationState.logStatus(application, "Initialized application");
		}
	},
	LOADING {
		@Override
		public void enter(Application application) {
			ApplicationState.logStatus(application, "Loading application");
		}
		
		@Override
		public void exit(Application application) {
			ApplicationState.logStatus(application, "Loaded application");
		}
	},
	RUNNING,
	PAUSED {
		@Override
		public void enter(Application application) {
			ApplicationState.logStatus(application, "Paused application");
		}
		
		@Override
		public void exit(Application application) {
			ApplicationState.logStatus(application, "Resumed application");
		}
	},
	STOPPED {
		@Override
		public void enter(Application application) {
			ApplicationState.logStatus(application, "Stopping application");
		}
	};
	
	private static void logStatus(Application application, String message) {
		if (!application.getConfig().logging.enableApplicationStateLogs.get()) {
			return;
		}
		
		application.getLogger().log(message);
	}
}
