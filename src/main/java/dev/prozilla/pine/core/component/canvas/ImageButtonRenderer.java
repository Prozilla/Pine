package dev.prozilla.pine.core.component.canvas;

import dev.prozilla.pine.common.Callback;
import dev.prozilla.pine.common.system.resource.ResourcePool;
import dev.prozilla.pine.common.system.resource.Texture;
import dev.prozilla.pine.common.system.resource.Color;
import dev.prozilla.pine.core.rendering.Renderer;
import dev.prozilla.pine.core.state.input.CursorType;
import dev.prozilla.pine.core.state.input.Input;
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
	private int paddingX;
	/** Vertical padding around text. */
	private int paddingY;
	
	public Callback clickCallback;
	
	protected boolean isHovering;
	
	public ImageButtonRenderer(String imagePath) {
		this(ResourcePool.loadTexture(imagePath));
	}
	
	public ImageButtonRenderer(Texture image) {
		this(image, Color.WHITE);
	}
	
	public ImageButtonRenderer(Texture image, Color backgroundColor) {
		super("ImageButtonRenderer", image);
		
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
	public void input(float deltaTime) {
		Input input = gameObject.game.input;
		Point cursor = input.getCursor();
		int canvasHeight = getCanvas().getHeight();
		if (cursor != null && isInside(cursor.x, canvasHeight - cursor.y, x, y - paddingY * 2, width, height)) {
			input.setCursorType(CursorType.HAND);
			input.blockCursor(gameObject);
			isHovering = true;
			
			if (clickCallback != null && input.getMouseButtonDown(MouseButton.LEFT)) {
				clickCallback.run();
			}
		} else {
			isHovering = false;
		}
		
		super.input(deltaTime);
	}
	
	@Override
	public void render(Renderer renderer) {
		// Render background
		if (width != 0 && height != 0 && backgroundColor != null) {
			Color backgroundColor = (isHovering && backgroundHoverColor != null) ? backgroundHoverColor : this.backgroundColor;
			renderer.drawRect(x, y - paddingY * 2, width, height, backgroundColor);
		}
		
		// Render image
		Color imageColor = (isHovering && hoverColor != null) ? hoverColor : color;
		int imageX = x + paddingX;
		int imageY = y - paddingY;
		int imageWidth = width - paddingX * 2;
		int imageHeight = height - paddingY * 2;
		renderImage(renderer, imageX, imageY, imageWidth, imageHeight, imageColor);
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
