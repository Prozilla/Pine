package dev.prozilla.pine.core.entity.prefab.canvas;

import dev.prozilla.pine.common.math.dimension.DualDimension;
import dev.prozilla.pine.common.property.VariableProperty;
import dev.prozilla.pine.common.property.adaptive.AdaptiveColorProperty;
import dev.prozilla.pine.common.property.style.StyledPropertyName;
import dev.prozilla.pine.common.system.resource.Color;
import dev.prozilla.pine.common.system.resource.ResourcePool;
import dev.prozilla.pine.common.system.resource.image.TextureBase;
import dev.prozilla.pine.core.component.canvas.ImageRenderer;
import dev.prozilla.pine.core.entity.Entity;

/**
 * Prefab for images in the UI.
 */
public class ImagePrefab extends CanvasElementPrefab {
	
	protected TextureBase image;
	protected Color color;
	
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
		
		size = new DualDimension(image.getWidth(), image.getHeight());
		cropToRegion = false;
		
		setName("Image");
	}
	
	public void setColor(Color color) {
		if (styleSheet == null) {
			this.color = color;
		} else {
			setColor(AdaptiveColorProperty.adapt(color));
		}
	}
	
	public void setColor(VariableProperty<Color> color) {
		setDefaultPropertyValue(StyledPropertyName.COLOR, AdaptiveColorProperty.adapt(color));
		this.color = color.getValue().clone();
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
		imageRenderer.size = size.clone();
		
		// Crop image
		if (cropToRegion) {
			imageRenderer.regionOffset.x = regionX;
			imageRenderer.regionOffset.y = regionY;
			imageRenderer.regionSize.x = regionWidth;
			imageRenderer.regionSize.y = regionHeight;
		}
		
		if (color != null) {
			imageRenderer.color = color.clone();
		}
	}
}
