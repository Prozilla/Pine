package dev.prozilla.pine.core.component.canvas;

import dev.prozilla.pine.common.Callback;
import dev.prozilla.pine.common.system.resource.ResourcePool;
import dev.prozilla.pine.common.system.resource.Texture;
import dev.prozilla.pine.common.system.resource.Color;
import dev.prozilla.pine.core.rendering.Renderer;
import dev.prozilla.pine.core.state.input.CursorType;
import dev.prozilla.pine.core.state.input.MouseButton;

import java.awt.*;

/**
 * A component for rendering buttons with images on the canvas.
 */
public class ImageButtonRenderer extends ImageRenderer {
	
	public Color hoverColor;
	public Color backgroundHoverColor;
	public Color backgroundColor;
	
	/** Horizontal padding around text. */
	public int paddingX;
	/** Vertical padding around text. */
	public int paddingY;
	
	public Callback clickCallback;
	
	public boolean isHovering;
	
	public ImageButtonRenderer(String imagePath) {
		this(ResourcePool.loadTexture(imagePath));
	}
	
	public ImageButtonRenderer(Texture image) {
		this(image, Color.WHITE);
	}
	
	public ImageButtonRenderer(Texture image, Color backgroundColor) {
		super(image);
		
		this.backgroundColor = backgroundColor;
		
		if (backgroundColor != null) {
			backgroundHoverColor = backgroundColor.clone();
			backgroundHoverColor.setAlpha(backgroundHoverColor.getAlpha() * 0.75f);
		}
		
		paddingX = 0;
		paddingY = 0;
		isHovering = false;
	}
	
	@Override
	public String getName() {
		return "ImageButtonRenderer";
	}
	
	@Override
	public void setWidth(int width) {
		super.setWidth(width + paddingX * 2);
	}
	
	@Override
	public void setHeight(int height) {
		super.setHeight(height + paddingY * 2);
	}
	
	public void setPaddingX(int paddingX) {
		width = width - this.paddingX * 2;
		this.paddingX = paddingX;
		setWidth(width);
	}
	
	public void setPaddingY(int paddingY) {
		height = height - this.paddingY * 2;
		this.paddingY = paddingY;
		setHeight(height);
	}
}
