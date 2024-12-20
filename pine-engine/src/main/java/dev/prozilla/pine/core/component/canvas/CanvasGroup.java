package dev.prozilla.pine.core.component.canvas;

import dev.prozilla.pine.common.math.vector.Vector2i;
import dev.prozilla.pine.common.math.vector.Vector4i;
import dev.prozilla.pine.common.system.resource.Color;
import dev.prozilla.pine.core.component.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * A component that groups multiple canvas elements together and aligns them.
 */
public class CanvasGroup extends Component {
	
	public Direction direction;
	public Alignment alignment;
	public Color backgroundColor;
	/** Distance between elements. */
	public int gap;
	public Vector2i padding;
	
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
		padding = new Vector2i();
		innerSize = new Vector2i();
	}
	
	@Override
	public String getName() {
		return "CanvasGroup";
	}
	
	/**
	 * Finds and stores rect components in children to be used in calculations later.
	 */
	public void getChildComponents() {
		childRects = entity.getComponentsInChildren(RectTransform.class);
	}
}
