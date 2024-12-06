package dev.prozilla.pine.core.system.canvas;

import dev.prozilla.pine.core.component.canvas.RectRenderer;
import dev.prozilla.pine.core.rendering.Renderer;
import dev.prozilla.pine.core.system.RenderSystem;

public class RectRenderSystem extends RenderSystem {
	
	public RectRenderSystem() {
		super(RectRenderer.class);
	}
	
	@Override
	public void render(Renderer renderer) {
		forEach(match -> {
			RectRenderer rectRenderer = match.getComponent(RectRenderer.class);
			
			if (rectRenderer.width != 0 && rectRenderer.height != 0) {
				if (rectRenderer.color == null) {
					renderer.drawRect(rectRenderer.x, rectRenderer.y, rectRenderer.width, rectRenderer.height);
				} else {
					renderer.drawRect(rectRenderer.x, rectRenderer.y, rectRenderer.width, rectRenderer.height, rectRenderer.color);
				}
			}
		});
	}
}
