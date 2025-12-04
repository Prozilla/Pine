package dev.prozilla.pine.examples.flappybird.component;

import dev.prozilla.pine.core.component.Component;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.examples.flappybird.scene.GameScene;

public class PipesData extends Component {
	
	public final Entity bottomPipe;
	public final Entity topPipe;
	public boolean passed;
	
	public final PipeData bottomPipeData;
	public final PipeData topPipeData;
	
	public GameScene gameScene;
	
	// Constants
	public static final float SPEED = 200f;
	public static final int MIN_GAP = 75;
	public static final int MAX_GAP = 150;
	
	public PipesData(Entity bottomPipe, Entity topPipe) {
		this.bottomPipe = bottomPipe;
		bottomPipeData = bottomPipe.getComponent(PipeData.class);
		this.topPipe = topPipe;
		topPipeData = topPipe.getComponent(PipeData.class);
	}
	
}
