package dev.prozilla.pine.examples.flappybird.object;

import dev.prozilla.pine.core.Game;
import dev.prozilla.pine.core.object.GameObject;
import dev.prozilla.pine.examples.flappybird.Main;
import dev.prozilla.pine.examples.flappybird.Scene;

import java.util.Random;

public class Pipes extends GameObject {
	
	private static final Random random = new Random();
	
	private final Pipe bottomPipe;
	private final Pipe topPipe;
	
	// Constants
	public static final float SPEED = 200f;
	
	public Pipes(Game game) {
		super(game, "Pipes");
		
		// Randomize height and gap
		int height = Math.round(random.nextFloat(Main.HEIGHT / -4f, Main.HEIGHT / 4f));
		int gap = random.nextInt(150, 200);
		
		// Add pipes
		bottomPipe = (Pipe)addChild(new Pipe(game, false));
		topPipe = (Pipe)addChild(new Pipe(game, true));
		
		// Position pipes
		topPipe.y = height + gap / 2f;
		bottomPipe.y = height - gap / 2f - Pipe.HEIGHT * 1.5f;
	}
	
	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		
		if (!((Scene)scene).gameOver) {
			// Scroll position of pipes to the left
			bottomPipe.x -= deltaTime * SPEED;
			topPipe.x = bottomPipe.x;
		}
	}
}
