package dev.prozilla.pine.core.system.standard.animation.canvas;

import dev.prozilla.pine.core.component.canvas.CanvasElementStyler;
import dev.prozilla.pine.core.component.canvas.ImageRenderer;
import dev.prozilla.pine.core.entity.EntityChunk;
import dev.prozilla.pine.core.system.update.UpdateSystem;

public class ImageAnimator extends UpdateSystem {
	
	public ImageAnimator() {
		super( ImageRenderer.class, CanvasElementStyler.class);
	}
	
	@Override
	protected void process(EntityChunk chunk, float deltaTime) {
		ImageRenderer imageRenderer = chunk.getComponent(ImageRenderer.class);
		CanvasElementStyler canvasElementStyler = chunk.getComponent(CanvasElementStyler.class);
		
		if (canvasElementStyler.getColorProperty() != null) {
			canvasElementStyler.getColorProperty().transmit(imageRenderer.color);
		}
		if (canvasElementStyler.getSizeProperty() != null) {
			imageRenderer.size = canvasElementStyler.getSizeProperty().getValue();
		}
	}
}
