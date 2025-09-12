package dev.prozilla.pine.core.system.standard.shape;

import dev.prozilla.pine.core.component.Transform;
import dev.prozilla.pine.core.component.shape.ShapeRenderer;
import dev.prozilla.pine.core.entity.EntityChunk;
import dev.prozilla.pine.core.rendering.Renderer;
import dev.prozilla.pine.core.system.render.RenderSystem;

public final class ShapeRenderSystem extends RenderSystem {
	
	public ShapeRenderSystem() {
		super(ShapeRenderer.class);
	}
	
	@Override
	protected void process(EntityChunk chunk, Renderer renderer) {
		Transform transform = chunk.getTransform();
		ShapeRenderer<?> shapeRenderer = chunk.getComponent(ShapeRenderer.class);
		
		if (shapeRenderer.shape == null) {
			return;
		}
		
		if (shapeRenderer.color == null) {
			shapeRenderer.shape.draw(renderer, shapeRenderer.texture, transform.getDepth());
		} else {
			shapeRenderer.shape.draw(renderer, shapeRenderer.texture, shapeRenderer.color, transform.getDepth());
		}
	}
	
}
