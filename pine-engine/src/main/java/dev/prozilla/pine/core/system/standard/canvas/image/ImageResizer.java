package dev.prozilla.pine.core.system.standard.canvas.image;

import dev.prozilla.pine.core.component.canvas.ImageButtonRenderer;
import dev.prozilla.pine.core.component.canvas.ImageRenderer;
import dev.prozilla.pine.core.component.canvas.RectTransform;
import dev.prozilla.pine.core.entity.EntityMatch;
import dev.prozilla.pine.core.system.update.UpdateSystem;

public class ImageResizer extends UpdateSystem {
	
	public ImageResizer() {
		super(ImageRenderer.class, RectTransform.class);
		setExcludedComponentTypes(ImageButtonRenderer.class);
	}
	
	@Override
	protected void process(EntityMatch match, float deltaTime) {
		ImageRenderer imageRenderer = match.getComponent(ImageRenderer.class);
		RectTransform rect = match.getComponent(RectTransform.class);
		
		rect.width = imageRenderer.width;
		rect.height = imageRenderer.height;
	}
}
