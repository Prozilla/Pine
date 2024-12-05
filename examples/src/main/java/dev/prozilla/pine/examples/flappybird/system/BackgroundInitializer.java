package dev.prozilla.pine.examples.flappybird.system;

import dev.prozilla.pine.core.component.ComponentCollector;
import dev.prozilla.pine.core.component.SpriteRenderer;
import dev.prozilla.pine.core.component.Transform;
import dev.prozilla.pine.core.system.InitSystem;
import dev.prozilla.pine.examples.flappybird.GameScene;
import dev.prozilla.pine.examples.flappybird.Main;
import dev.prozilla.pine.examples.flappybird.component.BackgroundData;

public class BackgroundInitializer extends InitSystem {
	
	public BackgroundInitializer() {
		super(new ComponentCollector(BackgroundData.class, SpriteRenderer.class, Transform.class));
	}
	
	@Override
	public void init(long window) {
		forEach(componentGroup -> {
			Transform transform = componentGroup.getComponent(Transform.class);
			BackgroundData backgroundData = componentGroup.getComponent(BackgroundData.class);
			SpriteRenderer spriteRenderer = componentGroup.getComponent(SpriteRenderer.class);
			
			// Store reference to scene
			backgroundData.gameScene = (GameScene)world.scene;
			
			// Set sprite properties
			spriteRenderer.setRegion(0, 0, BackgroundData.WIDTH, BackgroundData.HEIGHT);
			spriteRenderer.scale = 1.0001f; // Fix lines appearing between sprites
			
			// Set initial position
			transform.setPosition(Main.WIDTH / -2f + BackgroundData.WIDTH * backgroundData.index, Main.HEIGHT / -2f);
		});
	}
}
