package dev.prozilla.pine.core.system.standard.animation.canvas;

import dev.prozilla.pine.core.component.canvas.ImageRenderer;
import dev.prozilla.pine.core.component.canvas.ImageStyler;
import dev.prozilla.pine.core.entity.EntityChunk;
import dev.prozilla.pine.core.system.update.UpdateSystem;

public class ImageAnimator extends UpdateSystem {
	
	public ImageAnimator() {
		super( ImageRenderer.class, ImageStyler.class);
	}
	
	@Override
	protected void process(EntityChunk chunk, float deltaTime) {
		ImageRenderer imageRenderer = chunk.getComponent(ImageRenderer.class);
		ImageStyler imageStyler = chunk.getComponent(ImageStyler.class);
		
		if (imageStyler.getColorProperty() != null) {
			imageStyler.getColorProperty().transmit(imageRenderer.color);
		}
		if (imageStyler.getSizeProperty() != null) {
			imageRenderer.size = imageStyler.getSizeProperty().getValue();
		}
	}
}
