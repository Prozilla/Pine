package dev.prozilla.pine.examples.gallery;

import dev.prozilla.pine.core.Application;
import dev.prozilla.pine.core.ApplicationBuilder;

public class Main {
	
	public static void main(String[] args) {
		ApplicationBuilder applicationBuilder = new ApplicationBuilder();
		
		applicationBuilder.setTitle("Gallery");
		applicationBuilder.setCompanyName("Pine");
		applicationBuilder.setWindowSize(900, 600);
		applicationBuilder.setInitialScene(new SponzaScene());
		applicationBuilder.setTargetFps(0);
		applicationBuilder.getConfig().window.enableVSync.set(false);
		applicationBuilder.setEnableLocalStorage(true);
		applicationBuilder.setFullscreen(true);
		applicationBuilder.getConfig().stopOnException.set(true);
		
		Application application = applicationBuilder.build();
		application.addScene(new MainScene());
		application.run();
	}
}
