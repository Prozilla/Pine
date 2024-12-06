package dev.prozilla.pine.core.system.canvas;

import dev.prozilla.pine.core.component.canvas.CanvasGroup;
import dev.prozilla.pine.core.rendering.Renderer;
import dev.prozilla.pine.core.system.RenderSystem;

public class CanvasGroupRenderer extends RenderSystem {
	
	public CanvasGroupRenderer() {
		super(CanvasGroup.class);
	}
	
	@Override
	public void render(Renderer renderer) {
		forEach(match -> {
			CanvasGroup canvasGroup = match.getComponent(CanvasGroup.class);
			
			if (canvasGroup.width != 0 && canvasGroup.height != 0 && canvasGroup.backgroundColor != null) {
				renderer.drawRect(canvasGroup.x, canvasGroup.y, canvasGroup.width, canvasGroup.height, canvasGroup.backgroundColor);
			}
		});
	}
}
