package dev.prozilla.pine.core.system.standard.canvas.image;

import dev.prozilla.pine.common.system.resource.Color;
import dev.prozilla.pine.common.system.resource.Texture;
import dev.prozilla.pine.core.component.canvas.ImageRenderer;
import dev.prozilla.pine.core.component.canvas.RectTransform;
import dev.prozilla.pine.core.entity.EntityMatch;
import dev.prozilla.pine.core.rendering.Renderer;
import dev.prozilla.pine.core.system.render.RenderSystem;

public class ImageRenderSystem extends RenderSystem {
	
	public ImageRenderSystem() {
		super(ImageRenderer.class, RectTransform.class);
	}
	
	@Override
	protected void process(EntityMatch match, Renderer renderer) {
		ImageRenderer imageRenderer = match.getComponent(ImageRenderer.class);
		RectTransform rect = match.getComponent(RectTransform.class);
		renderImage(renderer, imageRenderer, rect);
	}
	
	public static void renderImage(Renderer renderer, ImageRenderer imageRenderer, RectTransform rect) {
		renderImage(renderer, imageRenderer,
		 rect.x, rect.y, imageRenderer.width, imageRenderer.height, imageRenderer.color);
	}
	
	public static void renderImage(Renderer renderer, ImageRenderer imageRenderer, int x, int y, int width, int height, Color color) {
		renderImage(renderer, imageRenderer.image,
			imageRenderer.regionX, imageRenderer.regionY,
			imageRenderer.regionWidth, imageRenderer.regionHeight,
			x, y, width, height, color);
	}
	
	public static void renderImage(Renderer renderer, Texture image, int regX, int regY, int regWidth, int regHeight, int x, int y, int width, int height, Color color) {
		int x2 = x + width;
		int y2 = y + height;
		
		float s1 = (float)regX / image.getWidth();
		float t1 = (float)regY / image.getHeight();
		float s2 = (float)(regX + regWidth) / image.getWidth();
		float t2 = (float)(regY + regHeight) / image.getHeight();
		
		image.bind();
		
		if (color == null) {
			renderer.drawTextureRegion(x, y, x2, y2, s1, t1, s2, t2);
		} else {
			renderer.drawTextureRegion(x, y, x2, y2, s1, t1, s2, t2, color);
		}
	}
}
