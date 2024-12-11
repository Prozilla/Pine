package dev.prozilla.pine.core.system.standard.canvas;

import dev.prozilla.pine.core.component.canvas.RectRenderer;
import dev.prozilla.pine.core.component.canvas.RectTransform;
import dev.prozilla.pine.core.entity.EntityMatch;
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
	public void process(EntityMatch match, Renderer renderer) {
		RectRenderer rectRenderer = match.getComponent(RectRenderer.class);
		RectTransform rect = match.getComponent(RectTransform.class);
		
		if (rect.width != 0 && rect.height != 0) {
			if (rectRenderer.color == null) {
				renderer.drawRect(rect.x, rect.y, rect.width, rect.height);
			} else {
				renderer.drawRect(rect.x, rect.y, rect.width, rect.height, rectRenderer.color);
			}
		}
	}
}
