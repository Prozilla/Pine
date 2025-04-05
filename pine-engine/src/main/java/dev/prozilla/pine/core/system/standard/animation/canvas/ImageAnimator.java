package dev.prozilla.pine.core.system.standard.animation.canvas;

import dev.prozilla.pine.core.component.canvas.ImageAnimation;
import dev.prozilla.pine.core.component.canvas.ImageRenderer;
import dev.prozilla.pine.core.entity.EntityChunk;
import dev.prozilla.pine.core.system.update.UpdateSystem;

public class ImageAnimator extends UpdateSystem {
	
	public ImageAnimator() {
		super( ImageRenderer.class, ImageAnimation.class);
	}
	
	@Override
	protected void process(EntityChunk chunk, float deltaTime) {
		ImageRenderer imageRenderer = chunk.getComponent(ImageRenderer.class);
		ImageAnimation imageAnimation = chunk.getComponent(ImageAnimation.class);
		
		if (imageAnimation.getColorProperty() != null) {
			imageAnimation.getColorProperty().transmit(imageRenderer.color);
		}
		if (imageAnimation.getSizeProperty() != null) {
			imageRenderer.size = imageAnimation.getSizeProperty().getValue();
		}
	}
}
