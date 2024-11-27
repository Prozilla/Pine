package dev.prozilla.pine.core.component.canvas;

import dev.prozilla.pine.common.system.resource.ResourcePool;
import dev.prozilla.pine.common.system.resource.Texture;
import dev.prozilla.pine.common.system.resource.Color;
import dev.prozilla.pine.core.rendering.Renderer;

/**
 * A component for rendering images on the canvas.
 */
public class ImageRenderer extends RectTransform {
	
	protected Texture image;
	public Color color;
	
	public int regionX;
	public int regionY;
	public int regionWidth;
	public int regionHeight;
	
	public ImageRenderer(String imagePath) {
		this(ResourcePool.loadTexture(imagePath));
	}
	
	public ImageRenderer(Texture image) {
		this("ImageRenderer", image);
	}
	
	public ImageRenderer(String name, Texture image) {
		super(name);
		
		if (image == null) {
			throw new IllegalArgumentException("Image can't be null");
		}
		
		this.image = image;
		
		width = image.getWidth();
		height = image.getHeight();
		regionX = 0;
		regionY = 0;
		regionWidth = width;
		regionHeight = height;
	}
	
	@Override
	public void render(Renderer renderer) {
		renderImage(renderer, x, y, width, height, color);
	}
	
	protected void renderImage(Renderer renderer, int x, int y, int width, int height, Color color) {
		super.render(renderer);
		
		int x2 = x + width;
		int y2 = y + height;
		
		float s1 = (float)regionX / image.getWidth();
		float t1 = (float)regionY / image.getHeight();
		float s2 = (float)(regionX + regionWidth) / image.getWidth();
		float t2 = (float)(regionY + regionHeight) / image.getHeight();

		image.bind();
		
		if (color == null) {
			renderer.drawTextureRegion(x, y, x2, y2, s1, t1, s2, t2);
		} else {
			renderer.drawTextureRegion(x, y, x2, y2, s1, t1, s2, t2, color);
		}
	}
	
	public void setImage(Texture image) {
		this.image = image;
		regionX = 0;
		regionY = 0;
		regionWidth = image.getWidth();
		regionHeight = image.getHeight();
	}
	
	public void setWidth(int width) {
		this.width = width;
	}
	
	public void setHeight(int height) {
		this.height = height;
	}
}
