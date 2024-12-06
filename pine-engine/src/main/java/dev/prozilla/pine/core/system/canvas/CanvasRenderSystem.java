package dev.prozilla.pine.core.system.canvas;

import dev.prozilla.pine.core.component.canvas.CanvasRenderer;
import dev.prozilla.pine.core.rendering.Renderer;
import dev.prozilla.pine.core.system.RenderSystem;

public class CanvasRenderSystem extends RenderSystem {
	
	public CanvasRenderSystem() {
		super(CanvasRenderer.class);
	}
	
	@Override
	public void render(Renderer renderer) {
		renderer.setScale(1f);
	}
}
