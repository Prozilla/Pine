package dev.prozilla.pine.core.component.canvas;

import dev.prozilla.pine.common.Callback;
import dev.prozilla.pine.common.system.resource.Color;
import dev.prozilla.pine.core.rendering.Renderer;
import dev.prozilla.pine.core.state.input.CursorType;
import dev.prozilla.pine.core.state.input.Input;
import dev.prozilla.pine.core.state.input.MouseButton;

import java.awt.*;

/**
 * A component for rendering buttons with text on the canvas.
 */
public class TextButtonRenderer extends TextRenderer {
	
	public Color hoverColor;
	public Color backgroundHoverColor;
	public Color backgroundColor;
	/** Horizontal padding around text. */
	public int paddingX;
	/** Vertical padding around text. */
	public int paddingY;
	
	public Callback clickCallback;
	
	protected boolean isHovering;
	
	public TextButtonRenderer(String text) {
		this(text, Color.BLACK);
	}
	
	public TextButtonRenderer(String text, Color color) {
		this(text, color, Color.WHITE);
	}
	
	public TextButtonRenderer(String text, Color color, Color backgroundColor) {
		super(text, color);
		
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
		Point cursor = getInput().getCursor();
		int canvasHeight = getCanvas().getHeight();
		if (cursor != null && isInside(cursor.x, canvasHeight - cursor.y, x, y - paddingY * 2, width, height)) {
			getInput().setCursorType(CursorType.HAND);
			getInput().blockCursor(gameObject);
			isHovering = true;
			
			if (clickCallback != null && getInput().getMouseButtonDown(MouseButton.LEFT)) {
				clickCallback.run();
			}
		} else {
			isHovering = false;
		}
		
		super.input(deltaTime);
	}
	
	/**
	 * Renders the button's background and text on the screen.
	 * @param renderer Reference to the renderer
	 */
	@Override
	public void render(Renderer renderer) {
		// Render background
		if (width != 0 && height != 0 && backgroundColor != null) {
			Color backgroundColor = (isHovering && backgroundHoverColor != null) ? backgroundHoverColor : this.backgroundColor;
			renderer.drawRect(x, y - paddingY * 2, width, height, backgroundColor);
		}
		
		// Render text
		Color textColor = (isHovering && hoverColor != null) ? hoverColor : color;
		renderText(renderer, x + paddingX, y - paddingY, textColor);
	}
	
	@Override
	public String getName() {
		return "TextButtonRenderer";
	}
	
	@Override
	public void calculateSize() {
		super.calculateSize();
		width += paddingX * 2;
		height += paddingY * 2;
	}
}
