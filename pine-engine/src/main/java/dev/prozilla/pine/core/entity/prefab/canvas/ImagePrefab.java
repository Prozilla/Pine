package dev.prozilla.pine.core.entity.prefab.canvas;

import dev.prozilla.pine.common.system.resource.ResourcePool;
import dev.prozilla.pine.common.system.resource.image.TextureBase;
import dev.prozilla.pine.core.component.canvas.ImageRenderer;
import dev.prozilla.pine.core.entity.Entity;

/**
 * Prefab for images in the UI.
 */
public class ImagePrefab extends CanvasElementPrefab {
	
	protected TextureBase image;
	
	protected boolean cropToRegion;
	protected int regionX;
	protected int regionY;
	protected int regionWidth;
	protected int regionHeight;
	
	public ImagePrefab(String imagePath) {
		this(ResourcePool.loadTexture(imagePath));
	}
	
	public ImagePrefab(TextureBase image) {
		this.image = image;
		
		cropToRegion = false;
		
		setName("Image");
		addClass("image");
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
		
		// Crop image
		if (cropToRegion) {
			imageRenderer.regionOffset.x = regionX;
			imageRenderer.regionOffset.y = regionY;
			imageRenderer.regionSize.x = regionWidth;
			imageRenderer.regionSize.y = regionHeight;
		}
	}
}
