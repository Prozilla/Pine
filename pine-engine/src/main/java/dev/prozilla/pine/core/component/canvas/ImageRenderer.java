package dev.prozilla.pine.core.component.canvas;

import dev.prozilla.pine.common.system.resource.ResourcePool;
import dev.prozilla.pine.common.system.resource.Texture;
import dev.prozilla.pine.common.system.resource.Color;
import dev.prozilla.pine.core.rendering.Renderer;

/**
 * A component for rendering images on the canvas.
 */
public class ImageRenderer extends RectTransform {
	
	public Texture image;
	public Color color;
	
	public int regionX;
	public int regionY;
	public int regionWidth;
	public int regionHeight;
	
	public ImageRenderer(String imagePath) {
		this(ResourcePool.loadTexture(imagePath));
	}
	
	public ImageRenderer(Texture image) {
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
	public String getName() {
		return "ImageRenderer";
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
