package dev.prozilla.pine.examples.fps;

import dev.prozilla.pine.core.Application;
import dev.prozilla.pine.core.ApplicationBuilder;

public class Main {
	
	public static void main(String[] args) {
		ApplicationBuilder applicationBuilder = new ApplicationBuilder();
		
		applicationBuilder.setTitle("FPS");
		applicationBuilder.setCompanyName("Pine");
		applicationBuilder.setWindowSize(900, 600);
		applicationBuilder.setInitialScene(new GameScene());
		applicationBuilder.setTargetFps(120);
		applicationBuilder.setEnableLocalStorage(true);
		
		if (!Application.isDevMode()) {
			applicationBuilder.setFullscreen(true);
		}
		
		applicationBuilder.build().run();
	}
}
