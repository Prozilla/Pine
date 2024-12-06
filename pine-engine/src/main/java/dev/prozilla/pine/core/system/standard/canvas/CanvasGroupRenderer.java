package dev.prozilla.pine.core.system.standard.canvas;

import dev.prozilla.pine.core.component.canvas.CanvasGroup;
import dev.prozilla.pine.core.entity.EntityMatch;
import dev.prozilla.pine.core.rendering.Renderer;
import dev.prozilla.pine.core.system.render.RenderSystem;

/**
 * Renders canvas groups to the screen.
 */
public class CanvasGroupRenderer extends RenderSystem {
	
	public CanvasGroupRenderer() {
		super(CanvasGroup.class);
	}
	
	@Override
	public void process(EntityMatch match, Renderer renderer) {
		CanvasGroup canvasGroup = match.getComponent(CanvasGroup.class);
		
		if (canvasGroup.width != 0 && canvasGroup.height != 0 && canvasGroup.backgroundColor != null) {
			renderer.drawRect(canvasGroup.x, canvasGroup.y, canvasGroup.width, canvasGroup.height, canvasGroup.backgroundColor);
		}
	}
}
