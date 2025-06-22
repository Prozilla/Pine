package dev.prozilla.pine.examples.audio;

import dev.prozilla.pine.core.Application;
import dev.prozilla.pine.core.ApplicationBuilder;

public class Main {
	
	public static final int WIDTH = 900;
	public static final int HEIGHT = 600;
	
	public static final int BAR_COUNT = 25;
	public static final float LERP_SPEED = 10;
	
	public static void main(String[] args) {
		ApplicationBuilder applicationBuilder = new ApplicationBuilder();
		
		applicationBuilder.setTitle("Audio Visualizer");
		applicationBuilder.setWindowSize(WIDTH, HEIGHT);
		applicationBuilder.setInitialScene(new MainScene());
		applicationBuilder.setTargetFps(60);
		
		if (!Application.isDevMode()) {
			applicationBuilder.setFullscreen(true);
		}
		
		applicationBuilder.buildAndRun();
	}
}
