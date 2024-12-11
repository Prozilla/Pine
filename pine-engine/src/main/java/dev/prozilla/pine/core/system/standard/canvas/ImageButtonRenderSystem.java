package dev.prozilla.pine.core.system.standard.canvas;

import dev.prozilla.pine.common.system.resource.Color;
import dev.prozilla.pine.core.component.canvas.ImageButtonRenderer;
import dev.prozilla.pine.core.entity.EntityMatch;
import dev.prozilla.pine.core.rendering.Renderer;
import dev.prozilla.pine.core.system.render.RenderSystem;

public class ImageButtonRenderSystem extends RenderSystem {
	
	public ImageButtonRenderSystem() {
		super(ImageButtonRenderer.class);
	}
	
	@Override
	protected void process(EntityMatch match, Renderer renderer) {
		ImageButtonRenderer imageButtonRenderer = match.getComponent(ImageButtonRenderer.class);
		
		// Render background
		if (imageButtonRenderer.width != 0 && imageButtonRenderer.height != 0 && imageButtonRenderer.backgroundColor != null) {
			Color backgroundColor = (imageButtonRenderer.isHovering && imageButtonRenderer.backgroundHoverColor != null) ? imageButtonRenderer.backgroundHoverColor : imageButtonRenderer.backgroundColor;
			renderer.drawRect(imageButtonRenderer.x, imageButtonRenderer.y - imageButtonRenderer.paddingY * 2, imageButtonRenderer.width, imageButtonRenderer.height, backgroundColor);
		}
		
		// Render image
		Color imageColor = (imageButtonRenderer.isHovering && imageButtonRenderer.hoverColor != null) ? imageButtonRenderer.hoverColor : imageButtonRenderer.color;
		int imageX = imageButtonRenderer.x + imageButtonRenderer.paddingX;
		int imageY = imageButtonRenderer.y - imageButtonRenderer.paddingY;
		int imageWidth = imageButtonRenderer.width - imageButtonRenderer.paddingX * 2;
		int imageHeight = imageButtonRenderer.height - imageButtonRenderer.paddingY * 2;
		
		ImageRenderSystem.renderImage(renderer, imageButtonRenderer, imageX, imageY, imageWidth, imageHeight, imageColor);
	}
}
