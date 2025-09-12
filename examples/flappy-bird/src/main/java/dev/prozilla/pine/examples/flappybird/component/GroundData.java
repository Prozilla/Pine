package dev.prozilla.pine.examples.flappybird.component;

import dev.prozilla.pine.core.component.Component;
import dev.prozilla.pine.examples.flappybird.FlappyBird;
import dev.prozilla.pine.examples.flappybird.scene.GameScene;

public class GroundData extends Component {
	
	public final int index;
	
	public GameScene gameScene;
	
	// Constants
	public static final int WIDTH = 336;
	public static final int HEIGHT = 112;
	public static final float SPEED = 200f;
	public static final float ELEVATION = -25f;
	public static final float TOP_Y = FlappyBird.HEIGHT / -2f + HEIGHT + ELEVATION;
	
	public GroundData(int index) {
		this.index = index;
	}
	
}
