package dev.prozilla.pine.core.component.canvas;

import dev.prozilla.pine.common.system.resource.Color;
import dev.prozilla.pine.core.rendering.Renderer;

/**
 * A component for rendering rectangular shapes on the canvas.
 */
public class RectRenderer extends RectTransform {
	
	public Color color;
	
	public RectRenderer(int width, int height) {
		this(width, height, Color.WHITE);
	}
	
	public RectRenderer(int width, int height, Color color) {
		this("RectRenderer", width, height, color);
	}
	
	public RectRenderer(String name, int width, int height, Color color) {
		super(name);
		
		this.width = width;
		this.height = height;
		this.color = color;
	}
	
	@Override
	public void render(Renderer renderer) {
		super.render(renderer);
		
		if (width != 0 && height != 0) {
			if (color == null) {
				renderer.drawRect(x, y, width, height);
			} else {
				renderer.drawRect(x, y, width, height, color);
			}
		}
	}
	
	public void setWidth(int width) {
		this.width = width;
	}
	
	public void setHeight(int height) {
		this.height = height;
	}
}