package dev.prozilla.pine.core.system.standard.canvas.image;

import dev.prozilla.pine.core.component.canvas.ImageRenderer;
import dev.prozilla.pine.core.component.canvas.RectTransform;
import dev.prozilla.pine.core.entity.EntityMatch;
import dev.prozilla.pine.core.system.init.InitSystem;

public class ImageInitializer extends InitSystem {
	
	public ImageInitializer() {
		super(ImageRenderer.class, RectTransform.class);
	}
	
	@Override
	protected void process(EntityMatch match) {
		ImageRenderer imageRenderer = match.getComponent(ImageRenderer.class);
		RectTransform rect = match.getComponent(RectTransform.class);
		
		rect.width = imageRenderer.regionWidth;
		rect.height = imageRenderer.regionHeight;
	}
}
