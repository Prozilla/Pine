package dev.prozilla.pine.examples.flappybird;

import dev.prozilla.pine.core.Application;
import dev.prozilla.pine.examples.flappybird.entity.Background;

public class Main {
	
	// Constants
	public static final int WIDTH = Background.WIDTH * 3;
	public static final int HEIGHT = Background.HEIGHT;
	
	public static void main(String[] args) {
		// Create new scene and game
		GameScene scene = new GameScene();
		Application game = new Application("Flappy Bird", WIDTH, HEIGHT, scene);
		
		game.setIcons("flappybird/icon.png");
		game.run();
	}
}
