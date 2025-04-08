package dev.prozilla.pine.core.component.ui;

import dev.prozilla.pine.common.math.vector.Direction;
import dev.prozilla.pine.common.math.vector.EdgeAlignment;
import dev.prozilla.pine.common.math.vector.Vector2i;
import dev.prozilla.pine.core.component.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * A component that groups multiple nodes together and arranges them.
 */
public class LayoutNode extends Component implements NodeContext {
	
	public Direction direction;
	public EdgeAlignment alignment;
	public Distribution distribution;
	/** Distance between elements. */
	public int gap;
	public boolean arrangeChildren;
	
	public Vector2i innerSize;
	public Vector2i totalChildrenSize;
	
	/** Array of node components in children of the attached entity */
	public List<Node> childNodes;
	
	public Node node;
	
	public static final Direction DEFAULT_DIRECTION = Direction.UP;
	public static final EdgeAlignment DEFAULT_ALIGNMENT = EdgeAlignment.START;
	public static final Distribution DEFAULT_DISTRIBUTION = Distribution.START;
	
	public enum Distribution {
		START,
		CENTER,
		END,
		SPACE_BETWEEN
	}
	
	public LayoutNode() {
		this(DEFAULT_DIRECTION);
	}
	
	public LayoutNode(Direction direction) {
		this(direction, DEFAULT_ALIGNMENT);
	}
	
	public LayoutNode(Direction direction, EdgeAlignment alignment) {
		this(direction, alignment, DEFAULT_DISTRIBUTION);
	}
	
	public LayoutNode(Direction direction, EdgeAlignment alignment, Distribution distribution) {
		this.direction = direction;
		this.alignment = alignment;
		this.distribution = distribution;
		
		childNodes = new ArrayList<>();
		gap = 0;
		innerSize = new Vector2i();
		totalChildrenSize = new Vector2i();
		arrangeChildren = true;
	}
	
	@Override
	public String getName() {
		return "CanvasGroup";
	}
	
	/**
	 * Finds and stores node components in children to be used in calculations later.
	 */
	public void getChildComponents() {
		childNodes = entity.getComponentsInChildren(Node.class);
	}
	
	public Node getNode() {
		if (node != null) {
			return node;
		}
		
		node = getComponent(Node.class);
		
		if (node == null) {
			throw new IllegalStateException("CanvasGroup component requires RectTransform component");
		}
		
		return node;
	}
	
	@Override
	public int getX() {
		return getNode().getPositionX();
	}
	
	@Override
	public int getY() {
		return getNode().getPositionY();
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
