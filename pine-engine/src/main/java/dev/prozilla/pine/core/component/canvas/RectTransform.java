package dev.prozilla.pine.core.component.canvas;

import dev.prozilla.pine.core.component.Component;
import dev.prozilla.pine.core.object.GameObject;
import dev.prozilla.pine.core.object.canvas.Canvas;
import dev.prozilla.pine.core.object.canvas.CanvasElement;
import dev.prozilla.pine.core.state.input.Input;

import java.awt.*;

/**
 * A base component for positioning canvas elements.
 * Every canvas element is handled as a rectangle and is anchored to a position on the canvas,
 * bottom left corner by default.
 */
public class RectTransform extends Component {
	
	protected int x;
	protected int y;
	
	protected int offsetX;
	protected int offsetY;
	
	protected int width;
	protected int height;
	
	public Anchor anchor;
	
	/** If true, allows the cursor to pass through this element. */
	public boolean passThrough;
	
	private Canvas canvas;
	
	public enum Anchor {
		TOP_LEFT,
		TOP_RIGHT,
		TOP_CENTER,
		BOTTOM_LEFT,
		BOTTOM_RIGHT,
		BOTTOM_CENTER,
		CENTER
	}
	
	public RectTransform() {
		this("RectTransform");
	}
	
	public RectTransform(String name) {
		super();
		
		x = 0;
		y = 0;
		width = 0;
		height = 0;
		offsetX = 0;
		offsetY = 0;
		anchor = Anchor.BOTTOM_LEFT;
		passThrough = false;
	}
	
	/**
	 * Checks if the cursor is hovering this element and
	 * whether the cursor is allowed to pass through.
	 */
	@Override
	public void input(float deltaTime) {
		super.input(deltaTime);
		
		if (!passThrough) {
			Point cursor = getInput().getCursor();
			int canvasHeight = getCanvas().getHeight();
			if (cursor != null && isInside(cursor.x, canvasHeight - cursor.y, x, y, width, height)) {
				getInput().blockCursor(gameObject);
			}
		}
	}
	
	/**
	 * Updates the position of the rect inside the canvas.
	 */
	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		
		if (width == 0 || height == 0 || anchor == null) {
			return;
		}
		
		int canvasWidth = getCanvas().getWidth();
		int canvasHeight = getCanvas().getHeight();
		
		switch (anchor) {
			case BOTTOM_LEFT:
				x = offsetX;
				y = offsetY;
				break;
			case BOTTOM_RIGHT:
				x = canvasWidth - width - offsetX;
				y = offsetY;
				break;
			case BOTTOM_CENTER:
				x = Math.round((float)(canvasWidth - width) / 2f) + offsetX;
				y = offsetY;
				break;
			case CENTER:
				x = Math.round((float)(canvasWidth - width) / 2f) + offsetX;
				y = Math.round((float)(canvasHeight - height) / 2f) + offsetY;
				break;
			case TOP_LEFT:
				x = offsetX;
				y = canvasHeight - height - offsetY;
				break;
			case TOP_RIGHT:
				x = canvasWidth - width - offsetX;
				y = canvasHeight - height - offsetY;
				break;
			case TOP_CENTER:
				x = Math.round((float)(canvasWidth - width) / 2f) + offsetX;
				y = canvasHeight - height - offsetY;
				break;
		}
	}
	
	@Override
	public void attach(GameObject gameObject) throws IllegalArgumentException {
		if (!(gameObject instanceof CanvasElement)) {
			throw new IllegalArgumentException("TextRenderer component must be attached to a CanvasElement");
		}
		
		super.attach(gameObject);
	}
	
	@Override
	public String getName() {
		return "RectTransform";
	}
	
	public boolean isInside(int x, int y) {
		return isInside(x, y, this.x, this.y, getWidth(), getHeight());
	}
	
	public boolean isInside(int x, int y, int rectX, int rectY, int rectWidth, int rectHeight) {
		return x >= rectX && x < rectX + rectWidth
			&& y >= rectY && y < rectY + rectHeight;
	}
	
	protected Canvas getCanvas() throws IllegalStateException {
		if (canvas != null) {
			return canvas;
		}
		
		CanvasRenderer canvasRenderer = gameObject.getComponentInParent(CanvasRenderer.class);
		
		if (canvasRenderer == null || !(canvasRenderer.getGameObject() instanceof Canvas)) {
			throw new IllegalStateException("Canvas element must be a child of a canvas.");
		}
		
		canvas = (Canvas)canvasRenderer.getGameObject();
		return canvas;
	}
	
	public void setPosition(Anchor anchor, int x, int y) {
		setAnchor(anchor);
		setOffset(x, y);
	}
	
	public void setAnchor(Anchor anchor) {
		this.anchor = anchor;
	}
	
	public void setOffset(int x, int y) {
		setOffsetX(x);
		setOffsetY(y);
	}
	
	public void setOffsetX(int offsetX) {
		this.offsetX = offsetX;
	}
	
	public void setOffsetY(int offsetY) {
		this.offsetY = offsetY;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	@Override
	public String toString() {
		return "RectTransform{" +
			"x=" + x +
			", y=" + y +
			", offsetX=" + offsetX +
			", offsetY=" + offsetY +
			", width=" + width +
			", height=" + height +
			'}';
	}
	
	public void print() {
		System.out.println(this);
	}
}
