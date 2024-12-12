package dev.prozilla.pine.core.entity.prefab.canvas;

import dev.prozilla.pine.common.system.resource.Color;
import dev.prozilla.pine.common.system.resource.ResourcePool;
import dev.prozilla.pine.common.system.resource.Texture;
import dev.prozilla.pine.core.component.canvas.ImageRenderer;
import dev.prozilla.pine.core.entity.Entity;

/**
 * Prefab for images in the UI.
 */
public class ImagePrefab extends CanvasElementPrefab {
	
	protected Texture image;
	protected Color color;
	protected int width;
	protected int height;
	
	protected boolean cropToRegion;
	protected int regionX;
	protected int regionY;
	protected int regionWidth;
	protected int regionHeight;
	
	public ImagePrefab(String imagePath) {
		this(ResourcePool.loadTexture(imagePath));
	}
	
	public ImagePrefab(Texture image) {
		this.image = image;
		
		width = image.getWidth();
		height = image.getHeight();
		cropToRegion = false;
		
		setName("Image");
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	
	public void setSize(int width, int height) {
		setWidth(width);
		setHeight(height);
	}
	
	public void setWidth(int width) {
		this.width = width;
	}
	
	public void setHeight(int height) {
		this.height = height;
	}
	
	public void setRegion(int x, int y, int width, int height) {
		cropToRegion = true;
		regionX = x;
		regionY = y;
		regionWidth = width;
		regionHeight = height;
	}
	
	@Override
	protected void apply(Entity entity) {
		super.apply(entity);
		
		ImageRenderer imageRenderer = entity.addComponent(new ImageRenderer(image));
		
		// Set image dimensions
		imageRenderer.width = width;
		imageRenderer.height = height;
		
		// Crop image
		if (cropToRegion) {
			imageRenderer.regionX = regionX;
			imageRenderer.regionY = regionY;
			imageRenderer.regionWidth = regionWidth;
			imageRenderer.regionHeight = regionHeight;
		}
		
		if (color != null) {
			imageRenderer.color = color;
		}
	}
}
