package dev.prozilla.pine.core.system.standard.canvas.image;

import dev.prozilla.pine.core.component.canvas.ImageButtonRenderer;
import dev.prozilla.pine.core.component.canvas.ImageRenderer;
import dev.prozilla.pine.core.component.canvas.RectTransform;
import dev.prozilla.pine.core.entity.EntityChunk;
import dev.prozilla.pine.core.system.update.UpdateSystem;

public class ImageButtonResizer extends UpdateSystem {
	
	public ImageButtonResizer() {
		super(ImageButtonRenderer.class, ImageRenderer.class, RectTransform.class);
	}
	
	@Override
	protected void process(EntityChunk chunk, float deltaTime) {
		ImageButtonRenderer imageButtonRenderer = chunk.getComponent(ImageButtonRenderer.class);
		ImageRenderer imageRenderer = chunk.getComponent(ImageRenderer.class);
		RectTransform rect = chunk.getComponent(RectTransform.class);
		
		rect.width = imageRenderer.width + imageButtonRenderer.paddingX * 2;
		rect.height = imageRenderer.height + imageButtonRenderer.paddingY * 2;
	}
}
