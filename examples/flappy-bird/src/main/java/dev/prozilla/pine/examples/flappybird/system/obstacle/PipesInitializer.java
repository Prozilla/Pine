package dev.prozilla.pine.examples.flappybird.system.obstacle;

import dev.prozilla.pine.core.entity.EntityChunk;
import dev.prozilla.pine.core.system.init.InitSystem;
import dev.prozilla.pine.examples.flappybird.FlappyBird;
import dev.prozilla.pine.examples.flappybird.component.PipeData;
import dev.prozilla.pine.examples.flappybird.component.PipesData;
import dev.prozilla.pine.examples.flappybird.scene.GameScene;

import java.util.Random;

/**
 * Initializes pipe pairs by randomizing their heights and gaps.
 */
public class PipesInitializer extends InitSystem {
	
	private static final Random random = new Random();
	
	public PipesInitializer() {
		super(PipesData.class);
	}
	
	@Override
	protected void process(EntityChunk chunk) {
		PipesData pipesData = chunk.getComponent(PipesData.class);
		
		// Store reference to scene
		pipesData.gameScene = (GameScene)scene;
		
		// Randomize height and gap
		int height = Math.round(random.nextFloat(FlappyBird.HEIGHT / -4f, FlappyBird.HEIGHT / 4f));
		int gap = random.nextInt(150, 200);
		
		// Position pipes
		pipesData.topPipePrefab.transform.position.y = height + gap / 2f;
		pipesData.bottomPipePrefab.transform.position.y = height - gap / 2f - PipeData.HEIGHT * 1.5f;
		
		pipesData.passed = false;
	}
}
