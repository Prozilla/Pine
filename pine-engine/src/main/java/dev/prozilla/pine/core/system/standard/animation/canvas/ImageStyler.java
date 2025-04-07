package dev.prozilla.pine.core.system.standard.animation.canvas;

import dev.prozilla.pine.core.component.canvas.CanvasElementStyle;
import dev.prozilla.pine.core.component.canvas.ImageRenderer;
import dev.prozilla.pine.core.entity.EntityChunk;
import dev.prozilla.pine.core.system.update.UpdateSystem;

public class ImageStyler extends UpdateSystem {
	
	public ImageStyler() {
		super( ImageRenderer.class, CanvasElementStyle.class);
	}
	
	@Override
	protected void process(EntityChunk chunk, float deltaTime) {
		ImageRenderer imageRenderer = chunk.getComponent(ImageRenderer.class);
		CanvasElementStyle canvasElementStyle = chunk.getComponent(CanvasElementStyle.class);
		
		if (canvasElementStyle.getColorProperty() != null) {
			canvasElementStyle.getColorProperty().transmit(imageRenderer.color);
		}
	}
}
