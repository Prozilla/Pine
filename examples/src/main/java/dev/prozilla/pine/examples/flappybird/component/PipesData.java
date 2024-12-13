package dev.prozilla.pine.examples.flappybird.component;

import dev.prozilla.pine.core.component.Component;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.examples.flappybird.GameScene;

public class PipesData extends Component {
	
	public final Entity bottomPipePrefab;
	public final Entity topPipePrefab;
	public boolean passed;
	
	public GameScene gameScene;
	
	// Constants
	public static final float SPEED = 200f;
	
	public PipesData(Entity bottomPipePrefab, Entity topPipePrefab) {
		this.bottomPipePrefab = bottomPipePrefab;
		this.topPipePrefab = topPipePrefab;
	}
	
}
