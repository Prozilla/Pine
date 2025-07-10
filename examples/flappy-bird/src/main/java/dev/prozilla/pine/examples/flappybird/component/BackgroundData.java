package dev.prozilla.pine.examples.flappybird.component;

import dev.prozilla.pine.core.component.Component;
import dev.prozilla.pine.examples.flappybird.scene.GameScene;

public class BackgroundData extends Component {
	
	public final int index;
	
	public GameScene gameScene;
	
	// Constants
	public static final int WIDTH = 288;
	public static final int HEIGHT = 512;
	public static final float SPEED = 100f;
	
	public BackgroundData(int index) {
		this.index = index;
	}
	
}
