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
		
		if (!rect.readyToRender) {
			return;
		}
		
		if (rect.currentSize.x != 0 && rect.currentSize.y != 0) {
			if (rectRenderer.color == null) {
				renderer.drawRect(rect.currentPosition.x, rect.currentPosition.y, transform.getDepth(), rect.currentSize.x, rect.currentSize.y);
			} else {
				renderer.drawRect(rect.currentPosition.x, rect.currentPosition.y, transform.getDepth(), rect.currentSize.x, rect.currentSize.y, rectRenderer.color);
			}
		}
	}
}
