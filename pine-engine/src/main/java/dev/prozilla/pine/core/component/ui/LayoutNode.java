package dev.prozilla.pine.core.component.ui;

import dev.prozilla.pine.common.math.dimension.Dimension;
import dev.prozilla.pine.common.math.dimension.DimensionBase;
import dev.prozilla.pine.common.math.vector.Direction;
import dev.prozilla.pine.common.math.vector.EdgeAlignment;
import dev.prozilla.pine.common.math.vector.Vector2f;
import dev.prozilla.pine.common.util.ArrayUtils;
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
	public boolean arrangeChildren;
	
	public float currentGap;
	/** Distance between elements. */
	public DimensionBase gap;
	
	public Vector2f innerSize;
	public Vector2f totalChildrenSize;
	
	/** Array of node components in children of the attached entity */
	public List<Node> childNodes;
	
	public Node node;
	
	public static final Dimension DEFAULT_GAP = new Dimension();
	public static final Direction DEFAULT_DIRECTION = Direction.UP;
	public static final EdgeAlignment DEFAULT_ALIGNMENT = EdgeAlignment.START;
	public static final Distribution DEFAULT_DISTRIBUTION = Distribution.START;
	
	public enum Distribution {
		START("start"),
		CENTER("center"),
		END("end"),
		SPACE_BETWEEN("space-between");
		
		private final String string;
		
		Distribution(String string) {
			this.string = string;
		}
		
		@Override
		public String toString() {
			return string;
		}
		
		public static Distribution parse(String input) {
			return ArrayUtils.findByString(Distribution.values(), input);
		}
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
		innerSize = new Vector2f();
		totalChildrenSize = new Vector2f();
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
	
	public float getGap() {
		if (gap == null) {
			return 0;
		}
		
		return gap.compute(node, false);
	}
	
	@Override
	public float getX() {
		return getNode().getX();
	}
	
	@Override
	public float getY() {
		return getNode().getY();
	}
	
	@Override
	public float getWidth() {
		return innerSize.x;
	}
	
	@Override
	public float getHeight() {
		return innerSize.y;
	}
}
