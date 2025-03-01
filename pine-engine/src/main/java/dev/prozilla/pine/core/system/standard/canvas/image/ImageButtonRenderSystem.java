package dev.prozilla.pine.core.system.standard.canvas.image;

import dev.prozilla.pine.common.system.resource.Color;
import dev.prozilla.pine.core.component.Transform;
import dev.prozilla.pine.core.component.canvas.ButtonData;
import dev.prozilla.pine.core.component.canvas.ImageButtonRenderer;
import dev.prozilla.pine.core.component.canvas.ImageRenderer;
import dev.prozilla.pine.core.component.canvas.RectTransform;
import dev.prozilla.pine.core.entity.EntityChunk;
import dev.prozilla.pine.core.rendering.Renderer;
import dev.prozilla.pine.core.system.render.RenderSystem;

public class ImageButtonRenderSystem extends RenderSystem {
	
	public ImageButtonRenderSystem() {
		super(ImageButtonRenderer.class, ButtonData.class, ImageRenderer.class, RectTransform.class);
	}
	
	@Override
	protected void process(EntityChunk chunk, Renderer renderer) {
		Transform transform = chunk.getTransform();
		ImageButtonRenderer imageButtonRenderer = chunk.getComponent(ImageButtonRenderer.class);
		ButtonData buttonData = chunk.getComponent(ButtonData.class);
		ImageRenderer imageRenderer = chunk.getComponent(ImageRenderer.class);
		RectTransform rect = chunk.getComponent(RectTransform.class);
		
		int paddingX = imageButtonRenderer.padding.computeX(rect);
		int paddingY = imageButtonRenderer.padding.computeY(rect);
		
		// Render background
		if (rect.currentSize.x != 0 && rect.currentSize.y != 0 && imageButtonRenderer.backgroundColor != null) {
			Color backgroundColor = (buttonData.isHovering && imageButtonRenderer.backgroundHoverColor != null) ? imageButtonRenderer.backgroundHoverColor : imageButtonRenderer.backgroundColor;
			renderer.drawRect(rect.currentPosition.x, rect.currentPosition.y - paddingY * 2, transform.getDepth(), rect.currentSize.x, rect.currentSize.y, backgroundColor);
		}
		
		// Render image
		Color imageColor = (buttonData.isHovering && imageButtonRenderer.hoverColor != null) ? imageButtonRenderer.hoverColor : imageRenderer.color;
		int imageX = rect.currentPosition.x + paddingX;
		int imageY = rect.currentPosition.y - paddingY;
		int imageWidth = rect.currentSize.x - paddingX * 2;
		int imageHeight = rect.currentSize.y - paddingY * 2;
		
		ImageRenderSystem.renderImage(renderer, imageRenderer, imageX, imageY, imageWidth, imageHeight, transform.getDepth(), imageColor);
	}
}
