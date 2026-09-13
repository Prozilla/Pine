package dev.prozilla.pine.core.system.standard.physics.collision;

import dev.prozilla.pine.core.component.physics.collision.Collider;
import dev.prozilla.pine.core.rendering.Renderer;
import dev.prozilla.pine.core.system.render.RenderSystemBase;

/**
 * System that draws all colliders in the current scene.
 */
public final class ColliderRenderSystem extends RenderSystemBase {
	
	public ColliderRenderSystem() {
		super(Collider.class);
	}
	
	@Override
	public void render(Renderer renderer) {
		// Draw colliders on top of everything else in the scene.
		renderer.clearDepthBuffer();
		forEach(chunk -> {
			renderer.resetModelMatrix();
			chunk.getComponent(Collider.class).draw(renderer);
		});
	}
	
}
