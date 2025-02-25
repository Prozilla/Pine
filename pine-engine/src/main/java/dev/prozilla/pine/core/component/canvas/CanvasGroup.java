package dev.prozilla.pine.core.component.canvas;

import dev.prozilla.pine.common.math.dimension.DualDimension;
import dev.prozilla.pine.common.math.vector.Vector2i;
import dev.prozilla.pine.common.system.resource.Color;
import dev.prozilla.pine.core.component.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * A component that groups multiple canvas elements together and aligns them.
 */
public class CanvasGroup extends Component implements CanvasContext {
	
	public Direction direction;
	public Alignment alignment;
	public Color backgroundColor;
	/** Distance between elements. */
	public int gap;
	public DualDimension padding;
	public boolean arrangeChildren;
	
	public Vector2i innerSize;
	
	/** Array of rect components in children of the attached entity */
	public List<RectTransform> childRects;
	
	public static final Direction DEFAULT_DIRECTION = Direction.UP;
	public static final Alignment DEFAULT_ALIGNMENT = Alignment.START;
	
	public enum Direction {
		/** From bottom to top. */
		UP,
		/** From top to bottom. */
		DOWN,
		/** From left to right. */
		RIGHT,
		/** From right to left. */
		LEFT
	}
	
	public enum Alignment {
		START,
		END,
		CENTER,
	}
	
	public CanvasGroup() {
		this(DEFAULT_DIRECTION);
	}
	
	public CanvasGroup(Direction direction) {
		this(direction, DEFAULT_ALIGNMENT);
	}
	
	public CanvasGroup(Direction direction, Alignment alignment) {
		this.direction = direction;
		this.alignment = alignment;
		
		childRects = new ArrayList<>();
		gap = 0;
		padding = new DualDimension();
		innerSize = new Vector2i();
		arrangeChildren = true;
	}
	
	@Override
	public String getName() {
		return "CanvasGroup";
	}
	
	public void setPadding(DualDimension padding) {
		this.padding = padding;
	}
	
	/**
	 * Finds and stores rect components in children to be used in calculations later.
	 */
	public void getChildComponents() {
		childRects = entity.getComponentsInChildren(RectTransform.class);
	}
	
	@Override
	public int getWidth() {
		return innerSize.x;
	}
	
	@Override
	public int getHeight() {
		return innerSize.y;
	}
}
