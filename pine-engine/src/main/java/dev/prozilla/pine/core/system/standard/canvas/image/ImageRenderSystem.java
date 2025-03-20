package dev.prozilla.pine.core.system.standard.canvas.image;

import dev.prozilla.pine.common.system.resource.Color;
import dev.prozilla.pine.common.system.resource.TextureBase;
import dev.prozilla.pine.core.component.Transform;
import dev.prozilla.pine.core.component.canvas.ImageButtonRenderer;
import dev.prozilla.pine.core.component.canvas.ImageRenderer;
import dev.prozilla.pine.core.component.canvas.RectTransform;
import dev.prozilla.pine.core.entity.EntityChunk;
import dev.prozilla.pine.core.rendering.Renderer;
import dev.prozilla.pine.core.system.render.RenderSystem;

public class ImageRenderSystem extends RenderSystem {
	
	public ImageRenderSystem() {
		super(ImageRenderer.class, RectTransform.class);
		setExcludedComponentTypes(ImageButtonRenderer.class);
	}
	
	@Override
	protected void process(EntityChunk chunk, Renderer renderer) {
		Transform transform = chunk.getTransform();
		ImageRenderer imageRenderer = chunk.getComponent(ImageRenderer.class);
		RectTransform rect = chunk.getComponent(RectTransform.class);
		
		if (!rect.readyToRender) {
			return;
		}
		
		renderImage(renderer, imageRenderer, rect, transform.getDepth());
	}
	
	public static void renderImage(Renderer renderer, ImageRenderer imageRenderer, RectTransform rect, float z) {
		renderImage(renderer, imageRenderer,
		 rect.currentPosition.x, rect.currentPosition.y, imageRenderer.size.computeX(rect), imageRenderer.size.computeY(rect), z, imageRenderer.color);
	}
	
	public static void renderImage(Renderer renderer, ImageRenderer imageRenderer, int x, int y, int width, int height, float z, Color color) {
		renderImage(renderer, imageRenderer.image,
			imageRenderer.regionOffset.x, imageRenderer.regionOffset.y,
			imageRenderer.regionSize.x, imageRenderer.regionSize.y,
			x, y, width, height, z, color);
	}
	
	public static void renderImage(Renderer renderer, TextureBase texture, int regX, int regY, int regWidth, int regHeight, int x, int y, int width, int height, float z, Color color) {
		int x2 = x + width;
		int y2 = y + height;
		
		float s1 = (float)regX / texture.getWidth();
		float t1 = (float)regY / texture.getHeight();
		float s2 = (float)(regX + regWidth) / texture.getWidth();
		float t2 = (float)(regY + regHeight) / texture.getHeight();
		
		if (color == null) {
			renderer.drawTextureRegion(texture, x, y, x2, y2, z, s1, t1, s2, t2);
		} else {
			renderer.drawTextureRegion(texture, x, y, x2, y2, z, s1, t1, s2, t2, color);
		}
	}
}
