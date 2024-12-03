package dev.prozilla.pine.core.entity.canvas;

import dev.prozilla.pine.core.World;
import dev.prozilla.pine.core.component.canvas.RectRenderer;
import dev.prozilla.pine.core.component.canvas.RectTransform;
import dev.prozilla.pine.common.system.resource.Color;

/**
 * Represents a rectangular shape inside the canvas.
 * Wrapper object for the {@link RectRenderer} component.
 */
public class Rect extends CanvasElement {
	
	protected final RectRenderer rectRenderer;
	
	public Rect(World world, int width, int height) {
		this(world, width, height, Color.WHITE);
	}
	
	public Rect(World world, int width, int height, Color color) {
		super(world);
		
		rectRenderer = new RectRenderer(width, height, color);
		addComponent(rectRenderer);
	}
	
	@Override
	public String getName() {
		return getName("Rect");
	}
	
	@Override
	public Rect setPosition(RectTransform.Anchor anchor, int x, int y) {
		return (Rect)super.setPosition(anchor, x, y);
	}
	
	@Override
	public Rect setAnchor(RectTransform.Anchor anchor) {
		return (Rect)super.setAnchor(anchor);
	}
	
	@Override
	public Rect setOffset(int x, int y) {
		return (Rect)super.setOffset(x, y);
	}
	
	public Rect setSize(int width, int height) {
		setWidth(width);
		setHeight(height);
		return this;
	}
	
	public Rect setWidth(int width) {
		rectRenderer.setWidth(width);
		return this;
	}
	
	public Rect setHeight(int height) {
		rectRenderer.setHeight(height);
		return this;
	}
	
	public Rect setColor(Color color) {
		rectRenderer.color = color;
		return this;
	}
}
