package dev.prozilla.pine.core.system.standard.animation.canvas;

import dev.prozilla.pine.core.component.canvas.CanvasElementStyle;
import dev.prozilla.pine.core.component.canvas.ImageButtonRenderer;
import dev.prozilla.pine.core.entity.EntityChunk;
import dev.prozilla.pine.core.system.update.UpdateSystem;

public class ImageButtonStyler extends UpdateSystem {
	
	public ImageButtonStyler() {
		super( ImageButtonRenderer.class, CanvasElementStyle.class);
	}
	
	@Override
	protected void process(EntityChunk chunk, float deltaTime) {
		ImageButtonRenderer imageButtonRenderer = chunk.getComponent(ImageButtonRenderer.class);
		CanvasElementStyle canvasElementStyle = chunk.getComponent(CanvasElementStyle.class);
		
		if (canvasElementStyle.getBackgroundColorProperty() != null) {
			canvasElementStyle.getBackgroundColorProperty().transmit(imageButtonRenderer.backgroundColor);
		}
	}
}
