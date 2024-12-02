package dev.prozilla.pine.core.entity.canvas;

import dev.prozilla.pine.common.Callback;
import dev.prozilla.pine.common.system.resource.ResourcePool;
import dev.prozilla.pine.common.system.resource.Texture;
import dev.prozilla.pine.core.Game;
import dev.prozilla.pine.core.component.canvas.ImageButtonRenderer;
import dev.prozilla.pine.core.component.canvas.RectTransform;
import dev.prozilla.pine.common.system.resource.Color;

/**
 * Represents a button with an image inside the canvas.
 * Wrapper object for the {@link ImageButtonRenderer} component.
 */
public class ImageButton extends CanvasElement {
	
	protected final ImageButtonRenderer imageButtonRenderer;
	
	public ImageButton(Game game, String imagePath) {
		this(game, ResourcePool.loadTexture(imagePath));
	}
	
	public ImageButton(Game game, Texture image) {
		super(game);
		
		imageButtonRenderer = new ImageButtonRenderer(image);
		addComponent(imageButtonRenderer);
	}
	
	@Override
	public String getName() {
		return getName("ImageButton");
	}
	
	@Override
	public ImageButton setPosition(RectTransform.Anchor anchor, int x, int y) {
		return (ImageButton)super.setPosition(anchor, x, y);
	}
	
	@Override
	public ImageButton setAnchor(RectTransform.Anchor anchor) {
		return (ImageButton)super.setAnchor(anchor);
	}
	
	@Override
	public ImageButton setOffset(int x, int y) {
		return (ImageButton)super.setOffset(x, y);
	}
	
	public ImageButton setImage(Texture image) {
		imageButtonRenderer.setImage(image);
		return this;
	}
	
	public ImageButton setSize(int width, int height) {
		setWidth(width);
		setHeight(height);
		return this;
	}
	
	public ImageButton setWidth(int width) {
		imageButtonRenderer.setWidth(width);
		return this;
	}
	
	public ImageButton setHeight(int height) {
		imageButtonRenderer.setHeight(height);
		return this;
	}
	
	public ImageButton setPadding(int padding) {
		setPadding(padding, padding);
		return this;
	}
	
	public ImageButton setPadding(int x, int y) {
		setHorizontalPadding(x);
		setVerticalPadding(y);
		return this;
	}
	
	public ImageButton setHorizontalPadding(int paddingX) {
		imageButtonRenderer.setPaddingX(paddingX);
		return this;
	}
	
	public ImageButton setVerticalPadding(int paddingY) {
		imageButtonRenderer.setPaddingY(paddingY);
		return this;
	}
	
	public ImageButton setClickCallback(Callback callback) {
		imageButtonRenderer.clickCallback = callback;
		return this;
	}
	
	public ImageButton setImageColors(Color normal, Color hover) {
		setImageColor(normal);
		setImageHoverColor(hover);
		return this;
	}
	
	public ImageButton setImageColor(Color color) {
		imageButtonRenderer.color = color;
		return this;
	}
	
	public ImageButton setImageHoverColor(Color color) {
		imageButtonRenderer.hoverColor = color;
		return this;
	}
	
	public ImageButton setBackgroundColors(Color normal, Color hover) {
		setBackgroundColor(normal);
		setBackgroundHoverColor(hover);
		return this;
	}
	
	public ImageButton setBackgroundColor(Color color) {
		imageButtonRenderer.backgroundColor = color;
		return this;
	}
	
	public ImageButton setBackgroundHoverColor(Color color) {
		imageButtonRenderer.backgroundHoverColor = color;
		return this;
	}
	
	public ImageButton setRegion(int x, int y, int width, int height) {
		imageButtonRenderer.regionX = x;
		imageButtonRenderer.regionY = y;
		imageButtonRenderer.regionWidth = width;
		imageButtonRenderer.regionHeight = height;
		return this;
	}
}
