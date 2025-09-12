package dev.prozilla.pine.examples.audio;

import dev.prozilla.pine.core.Application;
import dev.prozilla.pine.core.ApplicationBuilder;

public class Main {
	
	// Constants
	public static final String TITLE = "Audio Visualizer";
	public static final int WIDTH = 900;
	public static final int HEIGHT = 450;
	public static final int FPS = 60;
	
	public static void main(String[] args) {
		ApplicationBuilder applicationBuilder = new ApplicationBuilder();
		
		applicationBuilder.setTitle(TITLE);
		applicationBuilder.setWindowSize(WIDTH, HEIGHT);
		applicationBuilder.setInitialScene(new AudioVisualizerScene());
		applicationBuilder.setTargetFps(FPS);
		
		if (!Application.isDevMode()) {
			applicationBuilder.setFullscreen(true);
		}
		
		applicationBuilder.build().run();
	}
}
