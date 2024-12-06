package dev.prozilla.pine.examples.flappybird.system.obstacle;

import dev.prozilla.pine.core.entity.EntityMatch;
import dev.prozilla.pine.core.system.init.InitSystem;
import dev.prozilla.pine.examples.flappybird.GameScene;
import dev.prozilla.pine.examples.flappybird.Main;
import dev.prozilla.pine.examples.flappybird.component.PipeData;
import dev.prozilla.pine.examples.flappybird.component.PipesData;

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
	protected void process(EntityMatch match) {
		PipesData pipesData = match.getComponent(PipesData.class);
		
		// Store reference to scene
		pipesData.gameScene = (GameScene)scene;
		
		// Randomize height and gap
		int height = Math.round(random.nextFloat(Main.HEIGHT / -4f, Main.HEIGHT / 4f));
		int gap = random.nextInt(150, 200);
		
		// Position pipes
		pipesData.topPipe.transform.y = height + gap / 2f;
		pipesData.bottomPipe.transform.y = height - gap / 2f - PipeData.HEIGHT * 1.5f;
		
		pipesData.passed = false;
	}
}
