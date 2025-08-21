package dev.prozilla.pine.examples.snake;

import dev.prozilla.pine.core.ApplicationBuilder;

import static dev.prozilla.pine.examples.snake.GameScene.WORLD_HEIGHT;
import static dev.prozilla.pine.examples.snake.GameScene.WORLD_WIDTH;

public class Main {
	
	public static void main(String[] args) {
		ApplicationBuilder snake = new ApplicationBuilder();
		
		snake.setTitle("Snake");
		snake.setCompanyName("Pine");
		snake.setWindowSize(WORLD_WIDTH, WORLD_HEIGHT);
		snake.setInitialScene(new GameScene());
		snake.setIcons("snake/apple.png");
		snake.setTargetFps(120);
		
		snake.build().run();
	}
}
