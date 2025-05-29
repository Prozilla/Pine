package dev.prozilla.pine.examples.sokoban;

import dev.prozilla.pine.core.Application;
import dev.prozilla.pine.core.ApplicationBuilder;

public class Main {
	
	public static void main(String[] args) {
		ApplicationBuilder applicationBuilder = new ApplicationBuilder();
		
		applicationBuilder.setTitle("Sokoban");
		applicationBuilder.setWindowSize(900, 600);
		applicationBuilder.setInitialScene(new GameScene());
		applicationBuilder.setIcons("images/crates/crate_03.png");
		applicationBuilder.setTargetFps(120);
		
		if (!Application.isDevMode()) {
			applicationBuilder.setFullscreen(true);
		}
		
		Application application = applicationBuilder.build();
		application.getConfig().rendering.snapPixels.set(true);
		
		GameManager.instance = new GameManager(application);
		application.setApplicationManager(GameManager.instance);
		
		application.run();
	}
}
