package dev.prozilla.pine.examples.flappybird.object;

import dev.prozilla.pine.core.Game;
import dev.prozilla.pine.core.object.Sprite;
import dev.prozilla.pine.examples.flappybird.Main;

public class Pipe extends Sprite {
	
	// Constants
	public static final int WIDTH = 52;
	public static final int HEIGHT = 320;
	
	public Pipe(Game game, boolean top) {
		super(game, "flappybird/pipe.png");
		
		// Set sprite properties
		spriteRenderer.crop(0, 0, WIDTH, HEIGHT);
		spriteRenderer.scale = 1.25f;
		if (top) {
			// Flip sprite
			spriteRenderer.rotation = -180;
		}
		
		// Set initial position
		x = Main.WIDTH / 2f;
	}
	
	@Override
	public String getName() {
		return getName("Pipe");
	}
}
