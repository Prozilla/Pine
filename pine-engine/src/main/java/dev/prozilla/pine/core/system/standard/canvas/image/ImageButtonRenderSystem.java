package dev.prozilla.pine.core.system.standard.canvas.image;

import dev.prozilla.pine.common.system.resource.Color;
import dev.prozilla.pine.core.component.Transform;
import dev.prozilla.pine.core.component.canvas.ImageButtonRenderer;
import dev.prozilla.pine.core.component.canvas.ImageRenderer;
import dev.prozilla.pine.core.component.canvas.RectTransform;
import dev.prozilla.pine.core.entity.EntityChunk;
import dev.prozilla.pine.core.rendering.Renderer;
import dev.prozilla.pine.core.system.render.RenderSystem;

public class ImageButtonRenderSystem extends RenderSystem {
	
	public ImageButtonRenderSystem() {
		super(ImageButtonRenderer.class, ImageRenderer.class, RectTransform.class);
	}
	
	@Override
	protected void process(EntityChunk chunk, Renderer renderer) {
		Transform transform = chunk.getTransform();
		ImageButtonRenderer imageButtonRenderer = chunk.getComponent(ImageButtonRenderer.class);
		ImageRenderer imageRenderer = chunk.getComponent(ImageRenderer.class);
		RectTransform rect = chunk.getComponent(RectTransform.class);
		
		// Render background
		if (rect.width != 0 && rect.height != 0 && imageButtonRenderer.backgroundColor != null) {
			Color backgroundColor = (imageButtonRenderer.isHovering && imageButtonRenderer.backgroundHoverColor != null) ? imageButtonRenderer.backgroundHoverColor : imageButtonRenderer.backgroundColor;
			renderer.drawRect(rect.x, rect.y - imageButtonRenderer.paddingY * 2, transform.getDepth(), rect.width, rect.height, backgroundColor);
		}
		
		// Render image
		Color imageColor = (imageButtonRenderer.isHovering && imageButtonRenderer.hoverColor != null) ? imageButtonRenderer.hoverColor : imageRenderer.color;
		int imageX = rect.x + imageButtonRenderer.paddingX;
		int imageY = rect.y - imageButtonRenderer.paddingY;
		int imageWidth = rect.width - imageButtonRenderer.paddingX * 2;
		int imageHeight = rect.height - imageButtonRenderer.paddingY * 2;
		
		ImageRenderSystem.renderImage(renderer, imageRenderer, imageX, imageY, imageWidth, imageHeight, transform.getDepth(), imageColor);
	}
}
