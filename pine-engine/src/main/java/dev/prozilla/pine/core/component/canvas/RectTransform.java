package dev.prozilla.pine.core.component.canvas;

import dev.prozilla.pine.common.math.vector.Vector2i;
import dev.prozilla.pine.core.component.Component;

/**
 * A base component for positioning canvas elements.
 * Every canvas element is handled as a rectangle and is anchored to a position on the canvas,
 * bottom left corner by default.
 */
public class RectTransform extends Component {
	
	public Vector2i position;
	public Vector2i offset;
	public Vector2i size;
	
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
		position = new Vector2i();
		offset = new Vector2i();
		size = new Vector2i();
		anchor = Anchor.BOTTOM_LEFT;
		passThrough = false;
		cursorHit = false;
		fillContainer = false;
	}
	
	@Override
	public String getName() {
		return "RectTransform";
	}
	
	public boolean isInside(Vector2i point) {
		return isInsideRect(point, position, size);
	}
	
	/**
	 * Checks if a given points is inside a given rectangle.
	 * @param rectPosition Position of the rectangle
	 * @param rectSize Size of the rectangle
	 * @return True if the point is inside the rectangle
	 */
	public static boolean isInsideRect(Vector2i point, Vector2i rectPosition, Vector2i rectSize) {
		return point.x >= rectPosition.x && point.x < rectPosition.x + rectSize.x
			&& point.y >= rectPosition.y && point.y < rectPosition.y + rectSize.y;
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
	
	public void setOffset(Vector2i offset) {
		setOffset(offset.x, offset.y);
	}
	
	public void setOffset(int x, int y) {
		setOffsetX(x);
		setOffsetY(y);
	}
	
	public void setOffsetX(int offsetX) {
		offset.x = offsetX;
	}
	
	public void setOffsetY(int offsetY) {
		offset.y = offsetY;
	}
	
	@Override
	public String toString() {
		return "RectTransform{" +
		        "cursorHit=" + cursorHit +
		        ", passThrough=" + passThrough +
		        ", anchor=" + anchor +
		        ", fillContainer=" + fillContainer +
		        ", size=" + size +
		        ", offset=" + offset +
		        ", position=" + position +
		        '}';
	}
	
	public void print() {
		System.out.println(this);
	}
}
