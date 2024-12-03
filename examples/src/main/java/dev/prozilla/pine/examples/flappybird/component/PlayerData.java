package dev.prozilla.pine.examples.flappybird.component;

import dev.prozilla.pine.core.component.Component;
import dev.prozilla.pine.examples.flappybird.GameScene;
import dev.prozilla.pine.examples.flappybird.Main;

public class PlayerData extends Component {
	
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
	
}
