package dev.prozilla.pine.core.component.canvas;

import dev.prozilla.pine.common.math.dimension.DualDimension;
import dev.prozilla.pine.common.math.vector.GridAlignment;
import dev.prozilla.pine.common.math.vector.Vector2i;
import dev.prozilla.pine.core.component.Component;

/**
 * A base component for positioning canvas elements.
 * Every canvas element is handled as a rectangle and is anchored to a position on the canvas,
 * bottom left corner by default.
 */
public class RectTransform extends Component {
	
	// Current state
	public Vector2i currentPosition;
	public Vector2i currentSize;
	public boolean cursorHit;
	
	// Attributes
	public DualDimension position;
	public DualDimension size;
	public GridAlignment anchor;
	/** If true, allows the cursor to pass through this element. */
	public boolean passThrough;
	/** If true, this rect won't be arranged by a canvas group */
	public boolean absolutePosition;
	
	public CanvasRenderer canvas;
	
	public static final GridAlignment DEFAULT_ANCHOR = GridAlignment.BOTTOM_LEFT;
	
	public RectTransform() {
		currentPosition = new Vector2i();
		currentSize = new Vector2i();
		position = new DualDimension();
		size = new DualDimension();
		anchor = DEFAULT_ANCHOR;
		passThrough = false;
		cursorHit = false;
		absolutePosition = false;
	}
	
	@Override
	public String getName() {
		return "RectTransform";
	}
	
	/**
	 * Checks if a point is inside this rectangle.
	 * @return True if the point is inside the rectangle
	 */
	public boolean isInside(Vector2i point) {
		return isInside(point.x, point.y);
	}
	
	/**
	 * Checks if a point is inside this rectangle.
	 * @param x X position
	 * @param y Y position
	 * @return True if the point is inside the rectangle
	 */
	public boolean isInside(int x, int y) {
		return isInsideRect(x, y, currentPosition, currentSize);
	}
	
	/**
	 * Checks if a point is inside a given rectangle.
	 * @param rectPosition Position of the rectangle
	 * @param rectSize Size of the rectangle
	 * @return True if the point is inside the rectangle
	 */
	public static boolean isInsideRect(Vector2i point, Vector2i rectPosition, Vector2i rectSize) {
		return isInsideRect(point.x, point.y, rectPosition, rectSize);
	}
	
	/**
	 * Checks if a point is inside a given rectangle.
	 * @param rectPosition Position of the rectangle
	 * @param rectSize Size of the rectangle
	 * @return True if the point is inside the rectangle
	 */
	public static boolean isInsideRect(int x, int y, Vector2i rectPosition, Vector2i rectSize) {
		return x >= rectPosition.x && x < rectPosition.x + rectSize.x
			&& y >= rectPosition.y && y < rectPosition.y + rectSize.y;
	}
	
	public CanvasContext getContext() {
		CanvasGroup canvasGroup = entity.getComponentInParent(CanvasGroup.class);
		if (canvasGroup != null) {
			return canvasGroup;
		}
		
		return getCanvas();
	}
	
	/**
	 * Gets the canvas component in a parent entity.
	 * @throws IllegalStateException When this entity is not a child of an entity with a canvas component.
	 */
	public CanvasRenderer getCanvas() throws IllegalStateException {
		if (canvas != null) {
			return canvas;
		}
		
		CanvasRenderer canvasRenderer = entity.getComponentInParent(CanvasRenderer.class);
		
		if (canvasRenderer == null) {
			entity.printHierarchy();
			throw new IllegalStateException("Canvas element must be a child of a canvas: " + entity);
		}
		
		canvas = canvasRenderer;
		return canvasRenderer;
	}
	
	public void setPosition(DualDimension position) {
		this.position = position;
	}
	
	public void setSize(DualDimension size) {
		this.size = size;
	}
	
	public void setAnchor(GridAlignment anchor) {
		this.anchor = anchor;
	}
	
	public int getPositionX() {
		return position.computeX(this);
	}
	
	public int getPositionY() {
		return position.computeY(this);
	}
	
	public int getSizeX() {
		return size.computeX(this);
	}
	
	public int getSizeY() {
		return size.computeY(this);
	}
	
	public void computeCurrentSize(DualDimension size) {
		currentSize.x = size.computeX(this);
		currentSize.y = size.computeY(this);
	}
}
