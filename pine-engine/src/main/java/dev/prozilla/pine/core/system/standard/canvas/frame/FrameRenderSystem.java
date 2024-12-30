package dev.prozilla.pine.core.system.standard.canvas.frame;

import dev.prozilla.pine.core.component.Transform;
import dev.prozilla.pine.core.component.canvas.FrameRenderer;
import dev.prozilla.pine.core.component.canvas.RectTransform;
import dev.prozilla.pine.core.entity.EntityChunk;
import dev.prozilla.pine.core.rendering.FrameBufferObject;
import dev.prozilla.pine.core.rendering.Renderer;
import dev.prozilla.pine.core.system.render.RenderSystem;

public class FrameRenderSystem extends RenderSystem {
	
	public FrameRenderSystem() {
		super(RectTransform.class, FrameRenderer.class);
	}
	
	@Override
	protected void process(EntityChunk chunk, Renderer renderer) {
		Transform transform = chunk.getTransform();
		RectTransform rect = chunk.getComponent(RectTransform.class);
		FrameRenderer frame = chunk.getComponent(FrameRenderer.class);
		
		// Draw the background
		if (frame.backgroundColor != null) {
			renderer.drawRect(rect.currentPosition.x, rect.currentPosition.y, transform.getDepth(), frame.getWidth(), frame.getHeight());
		}
		
		// Draw the frame buffer
		FrameBufferObject fbo = frame.fbo;
		if (fbo != null) {
			renderer.drawTexture(fbo.getTexture(), rect.currentPosition.x, rect.currentPosition.y, transform.getDepth());
		}
	}
}
