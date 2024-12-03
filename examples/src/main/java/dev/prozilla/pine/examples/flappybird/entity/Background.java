package dev.prozilla.pine.examples.flappybird.entity;

import dev.prozilla.pine.core.World;
import dev.prozilla.pine.core.entity.Sprite;
import dev.prozilla.pine.examples.flappybird.Main;
import dev.prozilla.pine.examples.flappybird.GameScene;

public class Background extends Sprite {
	
	private final int index;
	
	// Constants
	public static final int WIDTH = 288;
	public static final int HEIGHT = 512;
	public static final float SPEED = 100f;
	
	public Background(World world, int index) {
		super(world, "flappybird/background.png");
		
		this.index = index;
	}
	
	@Override
	public void init(long window) throws IllegalStateException {
		super.init(window);
		
		// Set sprite properties
		spriteRenderer.crop(0, 0, WIDTH, HEIGHT);
		spriteRenderer.scale = 1.0001f; // Fix lines appearing between sprites
		
		// Set initial position
		transform.setPosition(Main.WIDTH / -2f + WIDTH * index, Main.HEIGHT / -2f);
	}
	
	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		
		if (!((GameScene)scene).gameOver) {
			// Scroll position to the left
			transform.x -= deltaTime * SPEED;
			
			// Reset position when edge is reached
			if (transform.x + WIDTH < Main.WIDTH / -2f) {
				transform.x += Main.WIDTH + WIDTH;
			}
		}
	}
	
	@Override
	public String getName() {
		return getName("Background");
	}
}
