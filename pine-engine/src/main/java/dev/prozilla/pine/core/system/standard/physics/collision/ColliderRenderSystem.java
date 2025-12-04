package dev.prozilla.pine.core.system.standard.physics.collision;

import dev.prozilla.pine.core.component.physics.collision.Collider;
import dev.prozilla.pine.core.entity.EntityChunk;
import dev.prozilla.pine.core.rendering.Renderer;
import dev.prozilla.pine.core.system.render.RenderSystem;

/**
 * System that draws all colliders in the current scene.
 */
public final class ColliderRenderSystem extends RenderSystem {
	
	public ColliderRenderSystem() {
		super(Collider.class);
	}
	
	@Override
	protected void process(EntityChunk chunk, Renderer renderer) {
		chunk.getComponent(Collider.class).draw(renderer);
	}
	
}
