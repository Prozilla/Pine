package dev.prozilla.pine.core.entity.prefab.canvas;

import dev.prozilla.pine.common.math.dimension.Dimension;
import dev.prozilla.pine.common.math.dimension.DualDimension;
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
	protected DualDimension size;
	
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
		
		size = new DualDimension(image.getWidth(), image.getHeight());
		cropToRegion = false;
		
		setName("Image");
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	
	public void setSize(Dimension x, Dimension y) {
		setSize(new DualDimension(x, y));
	}
	
	public void setSize(DualDimension size) {
		this.size = size;
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
		imageRenderer.size = size;
		
		// Crop image
		if (cropToRegion) {
			imageRenderer.regionOffset.x = regionX;
			imageRenderer.regionOffset.y = regionY;
			imageRenderer.regionSize.x = regionWidth;
			imageRenderer.regionSize.y = regionHeight;
		}
		
		if (color != null) {
			imageRenderer.color = color;
		}
	}
}
