package dev.prozilla.pine.examples.flappybird.system;

import dev.prozilla.pine.core.component.ComponentCollector;
import dev.prozilla.pine.core.component.SpriteRenderer;
import dev.prozilla.pine.core.component.Transform;
import dev.prozilla.pine.core.system.InitSystem;
import dev.prozilla.pine.examples.flappybird.Main;
import dev.prozilla.pine.examples.flappybird.component.PipeData;

public class PipeInitializer extends InitSystem {
	
	public PipeInitializer() {
		super(new ComponentCollector(PipeData.class, SpriteRenderer.class, Transform.class));
	}
	
	@Override
	public void init(long window) {
		forEach(componentGroup -> {
			PipeData pipeData = componentGroup.getComponent(PipeData.class);
			SpriteRenderer spriteRenderer = componentGroup.getComponent(SpriteRenderer.class);
			Transform transform = componentGroup.getComponent(Transform.class);
			
			// Set sprite properties
			spriteRenderer.setRegion(0, 0, PipeData.WIDTH, PipeData.HEIGHT);
			spriteRenderer.scale = 1.25f;
			if (pipeData.isTop) {
				// Flip sprite
				spriteRenderer.rotation = -180;
			}
			
			// Set initial position
			transform.x = Main.WIDTH / 2f;
		});
	}
}
