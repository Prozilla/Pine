package dev.prozilla.pine.examples.audio;

import dev.prozilla.pine.core.Application;
import dev.prozilla.pine.core.ApplicationBuilder;

public class Main {
	
	// Window
	public static final int WIDTH = 900;
	public static final int HEIGHT = 600;
	
	// Bars
	public static final int BAR_COUNT = 25;
	public static final float BAR_GAP = 4;
	public static final float LERP_SPEED = 10;
	
	// Shuffle
	public static final boolean ENABLE_SHUFFLE = true;
	public static final long SHUFFLE_SEED = 0;
	
	public static void main(String[] args) {
		ApplicationBuilder applicationBuilder = new ApplicationBuilder();
		
		applicationBuilder.setTitle("Audio Visualizer");
		applicationBuilder.setWindowSize(WIDTH, HEIGHT);
		applicationBuilder.setInitialScene(new AudioVisualizerScene());
		applicationBuilder.setTargetFps(60);
		
		if (!Application.isDevMode()) {
			applicationBuilder.setFullscreen(true);
		}
		
		applicationBuilder.buildAndRun();
	}
}
