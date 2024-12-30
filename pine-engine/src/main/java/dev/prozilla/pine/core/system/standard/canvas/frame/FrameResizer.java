package dev.prozilla.pine.core.system.standard.canvas.frame;

import dev.prozilla.pine.core.component.canvas.FrameRenderer;
import dev.prozilla.pine.core.component.canvas.RectTransform;
import dev.prozilla.pine.core.entity.EntityChunk;
import dev.prozilla.pine.core.system.update.UpdateSystem;

public class FrameResizer extends UpdateSystem {
	
	public FrameResizer() {
		super(RectTransform.class, FrameRenderer.class);
	}
	
	@Override
	protected void process(EntityChunk chunk, float deltaTime) {
		RectTransform rect = chunk.getComponent(RectTransform.class);
		FrameRenderer frame = chunk.getComponent(FrameRenderer.class);
		
		rect.currentSize.x = frame.getWidth();
		rect.currentSize.y = frame.getHeight();
	}
}
