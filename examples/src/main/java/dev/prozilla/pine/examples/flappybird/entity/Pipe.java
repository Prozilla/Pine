package dev.prozilla.pine.examples.flappybird.entity;

import dev.prozilla.pine.core.World;
import dev.prozilla.pine.core.entity.Sprite;
import dev.prozilla.pine.examples.flappybird.Main;

public class Pipe extends Sprite {
	
	// Constants
	public static final int WIDTH = 52;
	public static final int HEIGHT = 320;
	
	public Pipe(World world, boolean top) {
		super(world, "flappybird/pipe.png");
		
		// Set sprite properties
		spriteRenderer.crop(0, 0, WIDTH, HEIGHT);
		spriteRenderer.scale = 1.25f;
		if (top) {
			// Flip sprite
			spriteRenderer.rotation = -180;
		}
		
		// Set initial position
		transform.x = Main.WIDTH / 2f;
	}
	
	@Override
	public String getName() {
		return getName("Pipe");
	}
}
