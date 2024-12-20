package dev.prozilla.pine.core.system.standard.canvas;

import dev.prozilla.pine.core.component.Transform;
import dev.prozilla.pine.core.component.canvas.RectRenderer;
import dev.prozilla.pine.core.component.canvas.RectTransform;
import dev.prozilla.pine.core.entity.EntityChunk;
import dev.prozilla.pine.core.rendering.Renderer;
import dev.prozilla.pine.core.system.render.RenderSystem;

/**
 * Renders canvas elements to the screen.
 */
public class RectRenderSystem extends RenderSystem {
	
	public RectRenderSystem() {
		super(RectRenderer.class, RectTransform.class);
	}
	
	@Override
	public void process(EntityChunk chunk, Renderer renderer) {
		Transform transform = chunk.getTransform();
		RectRenderer rectRenderer = chunk.getComponent(RectRenderer.class);
		RectTransform rect = chunk.getComponent(RectTransform.class);
		
		if (rect.size.x != 0 && rect.size.y != 0) {
			if (rectRenderer.color == null) {
				renderer.drawRect(rect.position.x, rect.position.y, transform.getDepth(), rect.size.x, rect.size.y);
			} else {
				renderer.drawRect(rect.position.x, rect.position.y, transform.getDepth(), rect.size.x, rect.size.y, rectRenderer.color);
			}
		}
	}
}
