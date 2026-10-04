package dev.prozilla.pine.core.system.standard.mesh;

import dev.prozilla.pine.core.component.mesh.MeshRenderer;
import dev.prozilla.pine.core.entity.EntityChunk;
import dev.prozilla.pine.core.rendering.Renderer;
import dev.prozilla.pine.core.system.render.RenderSystem;

public final class MeshRenderSystem extends RenderSystem {
	
	public MeshRenderSystem() {
		super(MeshRenderer.class);
	}
	
	@Override
	protected void process(EntityChunk chunk, Renderer renderer) {
		MeshRenderer<?> meshRenderer = chunk.getComponent(MeshRenderer.class);
		
		if (meshRenderer.mesh == null) {
			return;
		}
		
		meshRenderer.mesh.draw(renderer, meshRenderer.material);
	}
	
}
