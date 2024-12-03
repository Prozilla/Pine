package dev.prozilla.pine.examples.flappybird;

import dev.prozilla.pine.core.Game;
import dev.prozilla.pine.examples.flappybird.entity.Background;

public class Main {
	
	// Constants
	public static final int WIDTH = Background.WIDTH * 3;
	public static final int HEIGHT = Background.HEIGHT;
	
	public static void main(String[] args) {
		// Create new scene and game
		GameScene scene = new GameScene();
		Game game = new Game("Flappy Bird", WIDTH, HEIGHT, scene);
		
		try {
			// Initialize game and start game loop
			game.init();
			game.setIcons(new String[]{ "flappybird/icon.png" });
			game.start();
		} catch (RuntimeException e) {
			// Quit game if anything goes wrong and throw exception
			game.destroy();
			throw new RuntimeException(e);
		}
	}
	
}
