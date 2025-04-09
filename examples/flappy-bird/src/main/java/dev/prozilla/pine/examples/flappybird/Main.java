package dev.prozilla.pine.examples.flappybird;

import dev.prozilla.pine.core.ApplicationBuilder;
import dev.prozilla.pine.examples.flappybird.component.BackgroundData;

public class Main {
	
	// Constants
	public static final int WIDTH = BackgroundData.WIDTH * 3;
	public static final int HEIGHT = BackgroundData.HEIGHT;
	
	public static ApplicationBuilder getApplication() {
		ApplicationBuilder flappyBird = new ApplicationBuilder();
		
		flappyBird.setTitle("Flappy Bird");
		flappyBird.setWindowSize(WIDTH, HEIGHT);
		flappyBird.setInitialScene(new GameScene());
		flappyBird.setIcons("flappybird/icon.png");
		flappyBird.setTargetFps(120);
//		flappyBird.setFullscreen(true);
		
		return flappyBird;
	}
	
	public static void main(String[] args) {
		ApplicationBuilder flappyBird = getApplication();
		flappyBird.buildAndRun();
	}
}
