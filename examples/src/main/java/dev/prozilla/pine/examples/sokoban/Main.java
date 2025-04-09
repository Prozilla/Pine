package dev.prozilla.pine.examples.sokoban;

import dev.prozilla.pine.core.Application;
import dev.prozilla.pine.core.ApplicationBuilder;

public class Main {
	
	public static void main(String[] args) {
		ApplicationBuilder applicationBuilder = new ApplicationBuilder();
		
		applicationBuilder.setTitle("Sokoban");
		applicationBuilder.setWindowSize(900, 600);
		applicationBuilder.setInitialScene(new GameScene());
		applicationBuilder.setIcons("sokoban/PNG/Default size/Crates/crate_03.png");
		applicationBuilder.setTargetFps(120);
		
		Application application = applicationBuilder.build();
		application.getConfig().rendering.snapPixels.set(true);
		
		application.run();
	}
}
