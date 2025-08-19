package dev.prozilla.pine.examples.flappybird.component;

import dev.prozilla.pine.core.component.Component;
import dev.prozilla.pine.examples.flappybird.FlappyBird;
import dev.prozilla.pine.examples.flappybird.scene.GameScene;

public class PlayerData extends Component {
	
	public int animationFrame;
	public float age;
	public float velocity;
	
	public GameScene gameScene;
	
	// Constants
	public static final int SPRITE_WIDTH = 32;
	public static final int SPRITE_HEIGHT = 32;
	public static final float SCALE = 1.5f;
	public static final float WIDTH = SPRITE_WIDTH * SCALE;
	public static final float HEIGHT = SPRITE_HEIGHT * SCALE;
	public static final float POSITION_X = FlappyBird.WIDTH / -4f;
	
	public static final float ANIMATION_SPEED = 10f;
	public static final float SPEED = 650f;
	public static final float ROTATION_FACTOR = 50f;
	public static final float ROTATION_SPEED = 10f;
	public static final float JUMP_VELOCITY = 0.65f;
	
	public void resetVelocity() {
		if (velocity > 0) {
			velocity = 0;
		}
	}
}
