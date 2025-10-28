package dev.prozilla.pine.examples.flappybird.system.obstacle;

import dev.prozilla.pine.common.property.random.LocalRandomFloatProperty;
import dev.prozilla.pine.common.property.random.LocalRandomIntProperty;
import dev.prozilla.pine.common.property.random.RandomFloatProperty;
import dev.prozilla.pine.common.property.random.RandomIntProperty;
import dev.prozilla.pine.core.entity.EntityChunk;
import dev.prozilla.pine.core.system.init.InitSystem;
import dev.prozilla.pine.examples.flappybird.FlappyBird;
import dev.prozilla.pine.examples.flappybird.component.GroundData;
import dev.prozilla.pine.examples.flappybird.component.PipeData;
import dev.prozilla.pine.examples.flappybird.component.PipesData;
import dev.prozilla.pine.examples.flappybird.scene.GameScene;

/**
 * Initializes pipe pairs by randomizing their heights and gaps.
 */
public class PipesInitializer extends InitSystem {
	
	public static final RandomFloatProperty heightProperty = new LocalRandomFloatProperty(FlappyBird.HEIGHT / -4f + GroundData.HEIGHT - GroundData.ELEVATION + PipeData.RIM_HEIGHT, FlappyBird.HEIGHT / 4f);
	public static final RandomIntProperty gapProperty = new LocalRandomIntProperty(PipesData.MIN_GAP, PipesData.MAX_GAP);
	
	public PipesInitializer() {
		super(PipesData.class);
	}
	
	@Override
	protected void process(EntityChunk chunk) {
		PipesData pipesData = chunk.getComponent(PipesData.class);
		
		// Store reference to scene
		pipesData.gameScene = (GameScene)scene;
		
		// Randomize height and gap
		int height = Math.round(heightProperty.get());
		int gap = gapProperty.get();
		
		// Position pipes
		pipesData.topPipe.transform.position.y = height + gap / 2f;
		pipesData.bottomPipe.transform.position.y = height - gap / 2f - PipeData.SPRITE_HEIGHT * 1.5f;
		
		pipesData.passed = false;
	}
}
