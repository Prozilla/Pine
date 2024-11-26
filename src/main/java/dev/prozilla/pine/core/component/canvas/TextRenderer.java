package dev.prozilla.pine.core.component.canvas;

import dev.prozilla.pine.common.system.resource.text.Font;
import dev.prozilla.pine.common.system.resource.Color;
import dev.prozilla.pine.core.rendering.Renderer;

/**
 * A component for rendering text on the canvas.
 */
public class TextRenderer extends RectTransform {
	
	public String text;
	public Color color;
	public Font font;
	
	public TextRenderer() {
		this("TextRenderer", null);
	}
	
	public TextRenderer(String text) {
		this("TextRenderer", text);
	}
	
	public TextRenderer(String name, String text) {
		this(name, text, Color.BLACK.clone());
	}
	
	public TextRenderer(String name, String text, Color color) {
		super(name);
		
		if (text == null) {
			this.text = "";
		} else {
			this.text = text;
		}
		
		this.color = color;
	}
	
	@Override
	public void init(long window) {
		super.init(window);
		calculateSize();
	}
	
	/**
	 * Renders this text on the screen.
	 */
	@Override
	public void render(Renderer renderer) {
		renderText(renderer, x, y, color);
	}
	
	/**
	 * Renders this text on the screen on a specific position.
	 * @param x X position
	 * @param y Y position
	 */
	protected void renderText(Renderer renderer, int x, int y, Color color) {
		super.render(renderer);
		
		if (text.isBlank() || width == 0 || height == 0) {
			return;
		}
		
		if (font == null) {
			renderer.drawText(text, x, y, color);
		} else {
			renderer.drawText(font, text, x, y, color);
		}
	}
	
	/**
	 * Setter for the font of this text.
	 */
	public void setFont(Font font) {
		this.font = font;
		calculateSize();
	}
	
	/**
	 * Calculates the size of this text.
	 */
	public void calculateSize() {
		Renderer renderer = gameObject.game.getRenderer();
		
		if (font == null) {
			width = renderer.getTextWidth(text);
			height = renderer.getTextHeight(text);
		} else {
			width = renderer.getTextWidth(font, text);
			height = renderer.getTextHeight(font, text);
		}
	}
}
