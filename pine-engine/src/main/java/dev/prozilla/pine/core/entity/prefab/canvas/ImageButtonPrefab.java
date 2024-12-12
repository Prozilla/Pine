package dev.prozilla.pine.core.entity.prefab.canvas;

import dev.prozilla.pine.common.Callback;
import dev.prozilla.pine.common.system.resource.Color;
import dev.prozilla.pine.common.system.resource.ResourcePool;
import dev.prozilla.pine.common.system.resource.Texture;
import dev.prozilla.pine.core.component.canvas.ImageButtonRenderer;
import dev.prozilla.pine.core.entity.Entity;

/**
 * Prefab for image buttons in the UI.
 */
public class ImageButtonPrefab extends ImagePrefab {
	
	protected Color hoverColor;
	protected Color backgroundHoverColor;
	protected Color backgroundColor;
	/** Horizontal padding around text. */
	protected int paddingX;
	/** Vertical padding around text. */
	protected int paddingY;
	
	protected Callback clickCallback;
	
	public ImageButtonPrefab(String imagePath) {
		this(ResourcePool.loadTexture(imagePath));
	}
	
	public ImageButtonPrefab(Texture image) {
		super(image);
		setName("ImageButton");
		
		paddingX = 0;
		paddingY = 0;
	}
	
	/**
	 * Sets the padding around the text of this button.
	 */
	public void setPadding(int padding) {
		setPadding(padding, padding);
	}
	
	/**
	 * Sets the horizontal and vertical padding around the text of this button.
	 * @param paddingX Horizontal padding
	 * @param paddingY Vertical padding
	 */
	public void setPadding(int paddingX, int paddingY) {
		setHorizontalPadding(paddingX);
		setVerticalPadding(paddingY);
	}
	
	public void setHorizontalPadding(int paddingX) {
		this.paddingX = paddingX;
	}
	
	public void setVerticalPadding(int paddingY) {
		this.paddingY = paddingY;
	}
	
	public void setClickCallback(Callback callback) {
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
		
		imageButtonRenderer.paddingX = paddingX;
		imageButtonRenderer.paddingY = paddingY;
		
		if (hoverColor != null) {
			imageButtonRenderer.hoverColor = hoverColor;
		}
		if (backgroundColor != null) {
			imageButtonRenderer.backgroundColor = backgroundColor;
		}
		if (backgroundHoverColor != null) {
			imageButtonRenderer.backgroundHoverColor = backgroundHoverColor;
		}
		
		if (clickCallback != null) {
			imageButtonRenderer.clickCallback = clickCallback;
		}
	}
}
