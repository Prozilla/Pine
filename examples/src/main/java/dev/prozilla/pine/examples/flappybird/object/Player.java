package dev.prozilla.pine.examples.flappybird.object;

import dev.prozilla.pine.common.math.MathUtils;
import dev.prozilla.pine.core.Game;
import dev.prozilla.pine.core.object.Sprite;
import dev.prozilla.pine.core.state.input.Key;
import dev.prozilla.pine.core.state.input.MouseButton;
import dev.prozilla.pine.examples.flappybird.Main;
import dev.prozilla.pine.examples.flappybird.Scene;

public class Player extends Sprite {
	
	private int animationFrame;
	private float age;
	private float velocity;
	
	// Constants
	public static final int WIDTH = 32;
	public static final int HEIGHT = 32;
	public static final float ANIMATION_SPEED = 10f;
	public static final float SPEED = 5f;
	public static final float JUMP_VELOCITY = 0.65f;
	
	public Player(Game game) {
		super(game, "Player", "flappybird/bird.png");
		
		// Set player properties
		animationFrame = 0;
		age = 0;
		velocity = JUMP_VELOCITY;
		x = Main.WIDTH / -4f;
		
		// Set sprite properties
		spriteRenderer.rotation = 0;
		spriteRenderer.scale = 1.5f;
	}
	
	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		
		if (y <= Main.HEIGHT / -2f || y + HEIGHT >= Main.HEIGHT / 2f) {
			((Scene)scene).endGame();
		}
		
		// Crop sprite to current frame
		spriteRenderer.crop(animationFrame * WIDTH, 0, WIDTH, HEIGHT);
		
		if (!((Scene)scene).gameOver) {
			// Update age and calculate frame
			age += deltaTime;
			animationFrame = Math.round((age * ANIMATION_SPEED)) % 3;
		} else {
			// Flip sprite if player is dead
			spriteRenderer.rotation = 180;
		}
		
		// Update velocity and move based on current velocity
		velocity -= deltaTime / 2f;
		y += velocity * SPEED;
		velocity -= deltaTime / 2f;
		
		// Clamp position inside screen bounds
		y = MathUtils.clamp(y, Main.HEIGHT / -2f, Main.HEIGHT / 2f);
	}
	
	@Override
	public void input(float deltaTime) {
		super.input(deltaTime);
		
		if (!((Scene)scene).gameOver) {
			// Jump
			if (game.input.getKeyDown(Key.SPACE) || game.input.getMouseButtonDown(MouseButton.LEFT)) {
				velocity = JUMP_VELOCITY;
			}
		}
	}
}
