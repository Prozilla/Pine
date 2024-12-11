package dev.prozilla.pine.core.system.standard.canvas;

import dev.prozilla.pine.core.component.canvas.CanvasGroup;
import dev.prozilla.pine.core.component.canvas.RectTransform;
import dev.prozilla.pine.core.entity.EntityMatch;
import dev.prozilla.pine.core.rendering.Renderer;
import dev.prozilla.pine.core.system.render.RenderSystem;

/**
 * Renders canvas groups to the screen.
 */
public class CanvasGroupRenderer extends RenderSystem {
	
	public CanvasGroupRenderer() {
		super(CanvasGroup.class, RectTransform.class);
	}
	
	@Override
	public void process(EntityMatch match, Renderer renderer) {
		CanvasGroup canvasGroup = match.getComponent(CanvasGroup.class);
		RectTransform rect = match.getComponent(RectTransform.class);
		
		if (rect.width != 0 && rect.height != 0 && canvasGroup.backgroundColor != null) {
			renderer.drawRect(rect.x, rect.y, rect.width, rect.height, canvasGroup.backgroundColor);
		}
	}
}
