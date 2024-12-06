package dev.prozilla.pine.core.system.standard.canvas;

import dev.prozilla.pine.core.component.canvas.CanvasRenderer;
import dev.prozilla.pine.core.rendering.Renderer;
import dev.prozilla.pine.core.system.render.RenderSystemBase;

/**
 * Prepares the rendering of canvas elements.
 */
public class CanvasRenderSystem extends RenderSystemBase {
	
	public CanvasRenderSystem() {
		super(CanvasRenderer.class);
	}
	
	@Override
	public void render(Renderer renderer) {
		renderer.setScale(1f);
	}
}
