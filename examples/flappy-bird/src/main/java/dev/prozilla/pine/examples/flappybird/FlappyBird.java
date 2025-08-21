package dev.prozilla.pine.examples.flappybird;

import dev.prozilla.pine.core.Application;
import dev.prozilla.pine.core.ApplicationBuilder;
import dev.prozilla.pine.core.scene.Scene;
import dev.prozilla.pine.examples.flappybird.component.BackgroundData;
import dev.prozilla.pine.examples.flappybird.scene.GameScene;
import dev.prozilla.pine.examples.flappybird.scene.MenuScene;

public class FlappyBird {
	
	// Scenes
	public static final Scene menuScene = new MenuScene();
	public static final Scene gameScene = new GameScene();
	
	// Constants
	public static final String TITLE = "Flappy Bird";
	public static final int WIDTH = BackgroundData.WIDTH * 3;
	public static final int HEIGHT = BackgroundData.HEIGHT;
	
	public static ApplicationBuilder getApplication() {
		ApplicationBuilder flappyBird = new ApplicationBuilder();
		
		flappyBird.setTitle(TITLE);
		flappyBird.setWindowSize(WIDTH, HEIGHT);
		flappyBird.setInitialScene(menuScene);
		flappyBird.setIcons("flappybird/icon.png");
		flappyBird.setTargetFps(120);
		
		if (!Application.isDevMode()) {
			flappyBird.setFullscreen(true);
		}
		
		return flappyBird;
	}
	
	public static void main(String[] args) {
		ApplicationBuilder flappyBird = getApplication();
		flappyBird.build().run();
	}
}
