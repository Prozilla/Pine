package dev.prozilla.pine.examples.flappybird.object;

import dev.prozilla.pine.common.math.MathUtils;
import dev.prozilla.pine.core.Game;
import dev.prozilla.pine.core.object.Sprite;
import dev.prozilla.pine.core.state.input.Key;
import dev.prozilla.pine.core.state.input.MouseButton;
import dev.prozilla.pine.examples.flappybird.Main;
import dev.prozilla.pine.examples.flappybird.GameScene;

public class Player extends Sprite {
	
	private int animationFrame;
	private float age;
	private float velocity;
	
	private GameScene gameScene;
	
	// Constants
	public static final int WIDTH = 32;
	public static final int HEIGHT = 32;
	public static final float POSITION_X = Main.WIDTH / -4f;
	public static final float ANIMATION_SPEED = 10f;
	public static final float SPEED = 5f;
	public static final float JUMP_VELOCITY = 0.65f;
	
	public Player(Game game) {
		super(game, "flappybird/bird.png");
	}
	
	@Override
	public void init(long window) throws IllegalStateException {
		super.init(window);
		
		// Store reference to scene
		gameScene = (GameScene)scene;
		
		// Set player properties
		animationFrame = 0;
		age = 0;
		velocity = JUMP_VELOCITY;
		x = POSITION_X;
		y = 0;
		
		// Set sprite properties
		spriteRenderer.scale = 1.5f;
		spriteRenderer.rotation = 0;
	}
	
	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		
		if (y <= Main.HEIGHT / -2f || y + HEIGHT >= Main.HEIGHT / 2f) {
			gameScene.endGame();
		}
		
		// Crop sprite to current frame
		spriteRenderer.crop(animationFrame * WIDTH, 0, WIDTH, HEIGHT);
		
		if (!gameScene.gameOver) {
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
		
		if (!gameScene.gameOver) {
			// Jump
			if (getInput().getKeyDown(Key.SPACE) || getInput().getMouseButtonDown(MouseButton.LEFT)) {
				velocity = JUMP_VELOCITY;
			}
		}
	}
	
	@Override
	public String getName() {
		return "Player";
	}
	
	public void resetVelocity() {
		velocity = 0;
	}
}
