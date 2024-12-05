package dev.prozilla.pine.examples.flappybird.system;

import dev.prozilla.pine.core.component.ComponentCollector;
import dev.prozilla.pine.core.system.InitSystem;
import dev.prozilla.pine.examples.flappybird.GameScene;
import dev.prozilla.pine.examples.flappybird.Main;
import dev.prozilla.pine.examples.flappybird.component.PipeData;
import dev.prozilla.pine.examples.flappybird.component.PipesData;
import dev.prozilla.pine.examples.flappybird.entity.Pipe;

import java.util.Random;

public class PipesInitializer extends InitSystem {
	
	private static final Random random = new Random();
	
	public PipesInitializer() {
		super(new ComponentCollector(PipesData.class));
	}
	
	@Override
	public void init(long window) {
		forEach(componentGroup -> {
			PipesData pipesData = componentGroup.getComponent(PipesData.class);
			
			// Store reference to scene
			pipesData.gameScene = (GameScene)world.scene;
			
			// Randomize height and gap
			int height = Math.round(random.nextFloat(Main.HEIGHT / -4f, Main.HEIGHT / 4f));
			int gap = random.nextInt(150, 200);
			
			// Position pipes
			pipesData.topPipe.transform.y = height + gap / 2f;
			pipesData.bottomPipe.transform.y = height - gap / 2f - PipeData.HEIGHT * 1.5f;
			
			pipesData.passed = false;
		});
	}
}
