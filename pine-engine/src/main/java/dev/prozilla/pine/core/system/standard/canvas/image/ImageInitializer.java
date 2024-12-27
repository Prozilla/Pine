package dev.prozilla.pine.core.system.standard.canvas.image;

import dev.prozilla.pine.core.component.canvas.ImageRenderer;
import dev.prozilla.pine.core.component.canvas.RectTransform;
import dev.prozilla.pine.core.entity.EntityChunk;
import dev.prozilla.pine.core.system.init.InitSystem;

public class ImageInitializer extends InitSystem {
	
	public ImageInitializer() {
		super(ImageRenderer.class, RectTransform.class);
	}
	
	@Override
	protected void process(EntityChunk chunk) {
		ImageRenderer imageRenderer = chunk.getComponent(ImageRenderer.class);
		RectTransform rect = chunk.getComponent(RectTransform.class);
		
		rect.currentSize.x = imageRenderer.regionSize.x;
		rect.currentSize.y = imageRenderer.regionSize.y;
	}
}
