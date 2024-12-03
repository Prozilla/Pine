package dev.prozilla.pine.examples.flappybird;

import dev.prozilla.pine.core.ApplicationBuilder;
import dev.prozilla.pine.examples.flappybird.entity.Background;

public class Main {
	
	// Constants
	public static final int WIDTH = Background.WIDTH * 3;
	public static final int HEIGHT = Background.HEIGHT;
	
	public static void main(String[] args) {
		ApplicationBuilder flappyBird = new ApplicationBuilder();
		
		flappyBird.setTitle("Flappy Bird");
		flappyBird.setWindowSize(WIDTH, HEIGHT);
		flappyBird.setInitialScene(new GameScene());
		flappyBird.setIcons("flappybird/icon.png");
		
		flappyBird.buildAndRun();
	}
}
