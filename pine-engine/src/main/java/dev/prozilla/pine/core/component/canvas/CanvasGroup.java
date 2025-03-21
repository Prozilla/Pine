package dev.prozilla.pine.core.component.canvas;

import dev.prozilla.pine.common.math.dimension.DualDimension;
import dev.prozilla.pine.common.math.vector.Direction;
import dev.prozilla.pine.common.math.vector.EdgeAlignment;
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
	public EdgeAlignment alignment;
	public Distribution distribution;
	public Color backgroundColor;
	/** Distance between elements. */
	public int gap;
	public DualDimension padding;
	public boolean arrangeChildren;
	
	public Vector2i innerSize;
	public Vector2i totalChildrenSize;
	
	/** Array of rect components in children of the attached entity */
	public List<RectTransform> childRects;
	
	public RectTransform rect;
	
	public static final Direction DEFAULT_DIRECTION = Direction.UP;
	public static final EdgeAlignment DEFAULT_ALIGNMENT = EdgeAlignment.START;
	public static final Distribution DEFAULT_DISTRIBUTION = Distribution.START;
	
	public enum Distribution {
		START,
		CENTER,
		END,
		SPACE_BETWEEN
	}
	
	public CanvasGroup() {
		this(DEFAULT_DIRECTION);
	}
	
	public CanvasGroup(Direction direction) {
		this(direction, DEFAULT_ALIGNMENT);
	}
	
	public CanvasGroup(Direction direction, EdgeAlignment alignment) {
		this(direction, alignment, DEFAULT_DISTRIBUTION);
	}
	
	public CanvasGroup(Direction direction, EdgeAlignment alignment, Distribution distribution) {
		this.direction = direction;
		this.alignment = alignment;
		this.distribution = distribution;
		
		childRects = new ArrayList<>();
		gap = 0;
		padding = new DualDimension();
		innerSize = new Vector2i();
		totalChildrenSize = new Vector2i();
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
	
	public RectTransform getRect() {
		if (rect != null) {
			return rect;
		}
		
		rect = getComponent(RectTransform.class);
		
		if (rect == null) {
			throw new IllegalStateException("CanvasGroup component requires RectTransform component");
		}
		
		return rect;
	}
	
	@Override
	public int getX() {
		return getRect().getPositionX();
	}
	
	@Override
	public int getY() {
		return getRect().getPositionY();
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
