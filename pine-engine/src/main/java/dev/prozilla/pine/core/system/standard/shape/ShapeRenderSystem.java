package dev.prozilla.pine.core.system.standard.shape;

import dev.prozilla.pine.core.component.mesh.MeshRenderer;
import dev.prozilla.pine.core.entity.EntityChunk;
import dev.prozilla.pine.core.rendering.Renderer;
import dev.prozilla.pine.core.system.render.RenderSystem;

public final class ShapeRenderSystem extends RenderSystem {
	
	public ShapeRenderSystem() {
		super(MeshRenderer.class);
	}
	
	@Override
	protected void process(EntityChunk chunk, Renderer renderer) {
		MeshRenderer<?> meshRenderer = chunk.getComponent(MeshRenderer.class);
		
		if (meshRenderer.mesh == null) {
			return;
		}
		
		if (meshRenderer.color == null) {
			meshRenderer.mesh.draw(renderer, meshRenderer.texture);
		} else {
			meshRenderer.mesh.draw(renderer, meshRenderer.texture, meshRenderer.color);
		}
	}
	
}
