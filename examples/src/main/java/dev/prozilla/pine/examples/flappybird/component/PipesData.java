package dev.prozilla.pine.examples.flappybird.component;

import dev.prozilla.pine.core.component.Component;
import dev.prozilla.pine.examples.flappybird.GameScene;
import dev.prozilla.pine.examples.flappybird.entity.Pipe;

public class PipesData extends Component {
	
	public final Pipe bottomPipe;
	public final Pipe topPipe;
	public boolean passed;
	
	public GameScene gameScene;
	
	// Constants
	public static final float SPEED = 200f;
	
	public PipesData(Pipe bottomPipe, Pipe topPipe) {
		this.bottomPipe = bottomPipe;
		this.topPipe = topPipe;
	}
	
}
