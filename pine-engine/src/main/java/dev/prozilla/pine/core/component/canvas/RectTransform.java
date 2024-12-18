package dev.prozilla.pine.core.component.canvas;

import dev.prozilla.pine.core.component.Component;

/**
 * A base component for positioning canvas elements.
 * Every canvas element is handled as a rectangle and is anchored to a position on the canvas,
 * bottom left corner by default.
 */
public class RectTransform extends Component {
	
	public int x;
	public int y;
	
	public int offsetX;
	public int offsetY;
	
	public int width;
	public int height;
	
	/** If true, the width and height of this rect will be changed each update to fill its container or the entire canvas.  */
	public boolean fillContainer;
	
	public Anchor anchor;
	
	/** If true, allows the cursor to pass through this element. */
	public boolean passThrough;
	public boolean cursorHit;
	
	public CanvasRenderer canvas;
	
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
		x = 0;
		y = 0;
		width = 0;
		height = 0;
		offsetX = 0;
		offsetY = 0;
		anchor = Anchor.BOTTOM_LEFT;
		passThrough = false;
		cursorHit = false;
		fillContainer = false;
	}
	
//	@Override
//	public void attach(Entity entity) throws IllegalArgumentException {
//		if (!(entity instanceof CanvasElement)) {
//			throw new IllegalArgumentException("TextRenderer component must be attached to a CanvasElement");
//		}
//
//		super.attach(entity);
//	}
	
	@Override
	public String getName() {
		return "RectTransform";
	}
	
	public boolean isInside(int x, int y) {
		return isInside(x, y, this.x, this.y, width, height);
	}
	
	public boolean isInside(int x, int y, int rectX, int rectY, int rectWidth, int rectHeight) {
		return x >= rectX && x < rectX + rectWidth
			&& y >= rectY && y < rectY + rectHeight;
	}
	
	public CanvasRenderer getCanvas() throws IllegalStateException {
		if (canvas != null) {
			return canvas;
		}
		
		CanvasRenderer canvasRenderer = entity.getComponentInParent(CanvasRenderer.class);
		
		if (canvasRenderer == null) {
			throw new IllegalStateException("Canvas element must be a child of a canvas.");
		}
		
		canvas = canvasRenderer;
		return canvasRenderer;
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
