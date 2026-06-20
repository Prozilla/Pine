package dev.prozilla.pine.core.system.standard.shape;

import dev.prozilla.pine.common.math.vector.Vector3f;
import dev.prozilla.pine.core.component.Transform;
import dev.prozilla.pine.core.component.shape.QuadRenderer;
import dev.prozilla.pine.core.entity.EntityChunk;
import dev.prozilla.pine.core.rendering.Renderer;
import dev.prozilla.pine.core.system.render.RenderSystemBase;

public final class QuadRenderSystem extends RenderSystemBase {
	
	public QuadRenderSystem() {
		super(QuadRenderer.class);
	}
	
	@Override
	public void render(Renderer renderer) {
		forEach(chunk -> {
			Transform transform = chunk.getTransform();
			QuadRenderer quadRenderer = chunk.getComponent(QuadRenderer.class);
			
			Vector3f position = transform.position;
			renderer.drawRect(position.x, position.y, position.z, quadRenderer.size.x, quadRenderer.size.y, quadRenderer.color);
		});
	}
	
	@Override
	protected boolean isChunkActive(EntityChunk chunk) {
		if (!super.isChunkActive(chunk)) {
			return false;
		}
		
		QuadRenderer quadRenderer = chunk.getComponent(QuadRenderer.class);
		return quadRenderer.size.x != 0 || quadRenderer.size.y != 0;
	}
}
