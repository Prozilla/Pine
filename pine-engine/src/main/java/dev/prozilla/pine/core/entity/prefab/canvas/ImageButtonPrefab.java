package dev.prozilla.pine.core.entity.prefab.canvas;

import dev.prozilla.pine.common.math.dimension.Dimension;
import dev.prozilla.pine.common.math.dimension.DualDimension;
import dev.prozilla.pine.common.system.resource.Color;
import dev.prozilla.pine.common.system.resource.ResourcePool;
import dev.prozilla.pine.common.system.resource.Texture;
import dev.prozilla.pine.core.component.canvas.ButtonData;
import dev.prozilla.pine.core.component.canvas.ImageButtonRenderer;
import dev.prozilla.pine.core.entity.Entity;

/**
 * Prefab for image buttons in the UI.
 */
public class ImageButtonPrefab extends ImagePrefab {
	
	protected Color hoverColor;
	protected Color backgroundHoverColor;
	protected Color backgroundColor;
	protected DualDimension padding;
	
	protected ButtonData.ClickCallback clickCallback;
	
	public ImageButtonPrefab(String imagePath) {
		this(ResourcePool.loadTexture(imagePath));
	}
	
	public ImageButtonPrefab(Texture image) {
		super(image);
		setName("ImageButton");
		
		padding = new DualDimension();
	}
	
	public void setPadding(Dimension x, Dimension y) {
		setPadding(new DualDimension(x, y));
	}
	
	public void setPadding(DualDimension padding) {
		this.padding = padding;
	}
	
	public void setClickCallback(ButtonData.ClickCallback callback) {
		clickCallback = callback;
	}
	
	public void setTextColors(Color normal, Color hover) {
		setTextColor(normal);
		setTextHoverColor(hover);
	}
	
	public void setTextColor(Color color) {
		setColor(color);
	}
	
	public void setTextHoverColor(Color color) {
		hoverColor = color;
	}
	
	public void setBackgroundColors(Color normal, Color hover) {
		setBackgroundColor(normal);
		setBackgroundHoverColor(hover);
	}
	
	public void setBackgroundColor(Color color) {
		backgroundColor = color;
	}
	
	public void setBackgroundHoverColor(Color color) {
		backgroundHoverColor = color;
	}
	
	@Override
	protected void apply(Entity entity) {
		super.apply(entity);
		
		ImageButtonRenderer imageButtonRenderer = entity.addComponent(new ImageButtonRenderer());
		
		imageButtonRenderer.padding = padding;
		
		if (hoverColor != null) {
			imageButtonRenderer.hoverColor = hoverColor;
		}
		if (backgroundColor != null) {
			imageButtonRenderer.backgroundColor = backgroundColor;
		}
		if (backgroundHoverColor != null) {
			imageButtonRenderer.backgroundHoverColor = backgroundHoverColor;
		}
		
		ButtonData buttonData = entity.addComponent(new ButtonData());
		
		if (clickCallback != null) {
			buttonData.clickCallback = clickCallback;
		}
	}
}
