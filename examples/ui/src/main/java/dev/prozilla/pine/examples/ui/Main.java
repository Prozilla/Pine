package dev.prozilla.pine.examples.ui;

import dev.prozilla.pine.core.ApplicationBuilder;

public class Main {
	
	public static void main(String[] args) {
		ApplicationBuilder applicationBuilder = new ApplicationBuilder();
		
		applicationBuilder.setInitialScene(new MainScene());
		applicationBuilder.setTitle("UI Demo");
		applicationBuilder.setWindowSize(900, 600);
		applicationBuilder.setTargetFps(120);
		applicationBuilder.getRenderConfig().snapPixels.setValue(true);
		
		applicationBuilder.buildAndRun();
	}
	
}
