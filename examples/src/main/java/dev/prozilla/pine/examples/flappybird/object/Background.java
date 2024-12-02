package dev.prozilla.pine.examples.flappybird.object;

import dev.prozilla.pine.core.Game;
import dev.prozilla.pine.core.object.Sprite;
import dev.prozilla.pine.examples.flappybird.Main;
import dev.prozilla.pine.examples.flappybird.GameScene;

public class Background extends Sprite {
	
	private final int index;
	
	// Constants
	public static final int WIDTH = 288;
	public static final int HEIGHT = 512;
	public static final float SPEED = 100f;
	
	public Background(Game game, int index) {
		super(game, "flappybird/background.png");
		
		this.index = index;
	}
	
	@Override
	public void init(long window) throws IllegalStateException {
		super.init(window);
		
		// Set sprite properties
		spriteRenderer.crop(0, 0, WIDTH, HEIGHT);
		spriteRenderer.scale = 1.0001f; // Fix lines appearing between sprites
		
		// Set initial position
		x = Main.WIDTH / -2f + WIDTH * index;
		y = Main.HEIGHT / -2f;
	}
	
	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		
		if (!((GameScene)scene).gameOver) {
			// Scroll position to the left
			x -= deltaTime * SPEED;
			
			// Reset position when edge is reached
			if (x + WIDTH < Main.WIDTH / -2f) {
				x += Main.WIDTH + WIDTH;
			}
		}
	}
	
	@Override
	public String getName() {
		return getName("Background");
	}
}
