package dev.prozilla.pine.examples.flappybird.component;

import dev.prozilla.pine.core.component.Component;
import dev.prozilla.pine.examples.flappybird.GameScene;
import dev.prozilla.pine.examples.flappybird.Main;

public class PlayerData extends Component {
	
	public int animationFrame;
	public float age;
	public float velocity;
	
	public GameScene gameScene;
	
	// Constants
	public static final int WIDTH = 32;
	public static final int HEIGHT = 32;
	public static final float POSITION_X = Main.WIDTH / -4f;
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
