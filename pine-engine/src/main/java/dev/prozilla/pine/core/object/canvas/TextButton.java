package dev.prozilla.pine.core.object.canvas;

import dev.prozilla.pine.common.Callback;
import dev.prozilla.pine.common.system.resource.ResourcePool;
import dev.prozilla.pine.common.system.resource.text.Font;
import dev.prozilla.pine.core.Game;
import dev.prozilla.pine.core.component.canvas.RectTransform;
import dev.prozilla.pine.core.component.canvas.TextButtonRenderer;
import dev.prozilla.pine.common.system.resource.Color;

/**
 * Represents a button with text inside the canvas.
 * Wrapper object for the {@link TextButtonRenderer} component.
 */
public class TextButton extends CanvasElement {
	
	protected final TextButtonRenderer textButtonRenderer;
	
	public TextButton(Game game, String text) {
		super(game);
		
		textButtonRenderer = new TextButtonRenderer(text);
		addComponent(textButtonRenderer);
	}
	
	@Override
	public String getName() {
		return getName("TextButton");
	}
	
	public TextButton setText(String text) {
		if (text.equals(textButtonRenderer.text)) {
			return this;
		}
		
		textButtonRenderer.text = text;
		textButtonRenderer.calculateSize();
		return this;
	}
	
	@Override
	public TextButton setPosition(RectTransform.Anchor anchor, int x, int y) {
		return (TextButton)super.setPosition(anchor, x, y);
	}
	
	@Override
	public TextButton setAnchor(RectTransform.Anchor anchor) {
		return (TextButton)super.setAnchor(anchor);
	}
	
	@Override
	public TextButton setOffset(int x, int y) {
		return (TextButton)super.setOffset(x, y);
	}
	
	public TextButton setFont(String path) {
		setFont(ResourcePool.loadFont(path));
		return this;
	}
	
	public TextButton setFont(Font font) {
		textButtonRenderer.setFont(font);
		return this;
	}
	
	/**
	 * Sets the padding around the text of this button.
	 */
	public TextButton setPadding(int padding) {
		setPadding(padding, padding);
		return this;
	}
	
	/**
	 * Sets the horizontal and vertical padding around the text of this button.
	 * @param paddingX Horizontal padding
	 * @param paddingY Vertical padding
	 */
	public TextButton setPadding(int paddingX, int paddingY) {
		setHorizontalPadding(paddingX);
		setVerticalPadding(paddingY);
		return this;
	}
	
	public TextButton setHorizontalPadding(int paddingX) {
		textButtonRenderer.paddingX = paddingX;
		textButtonRenderer.calculateSize();
		return this;
	}
	
	public TextButton setVerticalPadding(int paddingY) {
		textButtonRenderer.paddingY = paddingY;
		textButtonRenderer.calculateSize();
		return this;
	}
	
	public TextButton setClickCallback(Callback callback) {
		textButtonRenderer.clickCallback = callback;
		return this;
	}
	
	public TextButton setTextColors(Color normal, Color hover) {
		setTextColor(normal);
		setTextHoverColor(hover);
		return this;
	}
	
	public TextButton setTextColor(Color color) {
		textButtonRenderer.color = color;
		return this;
	}
	
	public TextButton setTextHoverColor(Color color) {
		textButtonRenderer.hoverColor = color;
		return this;
	}
	
	public TextButton setBackgroundColors(Color normal, Color hover) {
		setBackgroundColor(normal);
		setBackgroundHoverColor(hover);
		return this;
	}
	
	public TextButton setBackgroundColor(Color color) {
		textButtonRenderer.backgroundColor = color;
		return this;
	}
	
	public TextButton setBackgroundHoverColor(Color color) {
		textButtonRenderer.backgroundHoverColor = color;
		return this;
	}
}
