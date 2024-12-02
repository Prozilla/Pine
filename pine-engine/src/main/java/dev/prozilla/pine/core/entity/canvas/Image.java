package dev.prozilla.pine.core.entity.canvas;

import dev.prozilla.pine.common.system.resource.Texture;
import dev.prozilla.pine.core.Game;
import dev.prozilla.pine.core.component.canvas.ImageRenderer;
import dev.prozilla.pine.core.component.canvas.RectTransform;

/**
 * Represents an image element inside the canvas.
 * Wrapper object for the {@link ImageRenderer} component.
 */
public class Image extends CanvasElement {
	
	protected final ImageRenderer imageRenderer;
	
	public Image(Game game, String imagePath) {
		super(game);
		
		imageRenderer = new ImageRenderer(imagePath);
		addComponent(imageRenderer);
	}
	
	public Image(Game game, Texture image) {
		super(game);
		
		imageRenderer = new ImageRenderer(image);
		addComponent(imageRenderer);
	}
	
	@Override
	public String getName() {
		return getName("Image");
	}
	
	@Override
	public Image setPosition(RectTransform.Anchor anchor, int x, int y) {
		return (Image)super.setPosition(anchor, x, y);
	}
	
	@Override
	public Image setAnchor(RectTransform.Anchor anchor) {
		return (Image)super.setAnchor(anchor);
	}
	
	@Override
	public Image setOffset(int x, int y) {
		return (Image)super.setOffset(x, y);
	}
	
	public Image setImage(Texture image) {
		imageRenderer.setImage(image);
		return this;
	}
	
	public Image setSize(int width, int height) {
		setWidth(width);
		setHeight(height);
		return this;
	}
	
	public Image setWidth(int width) {
		imageRenderer.setWidth(width);
		return this;
	}
	
	public Image setHeight(int height) {
		imageRenderer.setHeight(height);
		return this;
	}
	
	public Image setRegion(int x, int y, int width, int height) {
		imageRenderer.regionX = x;
		imageRenderer.regionY = y;
		imageRenderer.regionWidth = width;
		imageRenderer.regionHeight = height;
		return this;
	}
}
