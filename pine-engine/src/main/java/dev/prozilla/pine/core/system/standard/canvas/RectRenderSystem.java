package dev.prozilla.pine.core.system.standard.canvas;

import dev.prozilla.pine.core.component.canvas.RectRenderer;
import dev.prozilla.pine.core.entity.EntityMatch;
import dev.prozilla.pine.core.rendering.Renderer;
import dev.prozilla.pine.core.system.render.RenderSystem;

/**
 * Renders canvas elements to the screen.
 */
public class RectRenderSystem extends RenderSystem {
	
	public RectRenderSystem() {
		super(RectRenderer.class);
	}
	
	@Override
	public void process(EntityMatch match, Renderer renderer) {
		RectRenderer rectRenderer = match.getComponent(RectRenderer.class);
		
		if (rectRenderer.width != 0 && rectRenderer.height != 0) {
			if (rectRenderer.color == null) {
				renderer.drawRect(rectRenderer.x, rectRenderer.y, rectRenderer.width, rectRenderer.height);
			} else {
				renderer.drawRect(rectRenderer.x, rectRenderer.y, rectRenderer.width, rectRenderer.height, rectRenderer.color);
			}
		}
	}
}
