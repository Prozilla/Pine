package dev.prozilla.pine.core.component.ui;

import dev.prozilla.pine.common.event.EventDispatcher;
import dev.prozilla.pine.common.event.EventDispatcherContext;
import dev.prozilla.pine.common.event.EventListener;
import dev.prozilla.pine.common.math.dimension.DualDimension;
import dev.prozilla.pine.common.math.vector.GridAlignment;
import dev.prozilla.pine.common.math.vector.Vector2f;
import dev.prozilla.pine.common.math.vector.Vector2i;
import dev.prozilla.pine.common.system.resource.Color;
import dev.prozilla.pine.core.component.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * Component for nodes that make up the user interface.
 *
 * <p>Nodes are similar to HTML elements and the <a href="https://developer.mozilla.org/en-US/docs/Learn_web_development/Core/Styling_basics/Box_model">CSS box model</a>.</p>
 */
public class Node extends Component implements EventDispatcherContext<NodeEvent> {
	
	// Current state
	public Vector2f currentPosition;
	public Vector2f currentInnerSize;
	public Vector2f currentOuterSize;
	public Vector2f offset;
	public boolean cursorHit;
	public boolean readyToRender;
	public int iterations;
	
	// Attributes
	public GridAlignment anchor;
	/** If true, allows the cursor to pass through this element. */
	public boolean passThrough;
	/** If true, this node won't be arranged by a layout node. */
	public boolean absolutePosition;
	public String tooltipText;
	
	// Style
	public Color color;
	public Color backgroundColor;
	public DualDimension size;
	public DualDimension padding;
	public DualDimension margin;
	
	public final Set<String> classes;
	public final Set<String> modifiers;
	
	public NodeRoot root;
	
	public final EventDispatcher<NodeEvent> eventDispatcher;
	
	public static final Color DEFAULT_COLOR = Color.white();
	public static final Color DEFAULT_BACKGROUND_COLOR = Color.transparent();
	public static final GridAlignment DEFAULT_ANCHOR = GridAlignment.BOTTOM_LEFT;
	
	public Node() {
		currentPosition = new Vector2f();
		currentInnerSize = new Vector2f();
		currentOuterSize = new Vector2f();
		offset = new Vector2f();
		cursorHit = false;
		readyToRender = false;
		iterations = 0;
		
		anchor = DEFAULT_ANCHOR;
		passThrough = false;
		absolutePosition = false;
		
		size = new DualDimension();
		
		classes = new HashSet<>();
		modifiers = new HashSet<>();
		
		eventDispatcher = new EventDispatcher<>();
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
		return isInsideRect(x, y, currentPosition, currentInnerSize);
	}
	
	/**
	 * Checks if a point is inside a given rectangle.
	 * @param rectPosition Position of the rectangle
	 * @param rectSize Size of the rectangle
	 * @return True if the point is inside the rectangle
	 */
	public static boolean isInsideRect(Vector2f point, Vector2f rectPosition, Vector2f rectSize) {
		return isInsideRect(point.x, point.y, rectPosition, rectSize);
	}
	
	/**
	 * Checks if a point is inside a given rectangle.
	 * @param rectPosition Position of the rectangle
	 * @param rectSize Size of the rectangle
	 * @return True if the point is inside the rectangle
	 */
	public static boolean isInsideRect(float x, float y, Vector2f rectPosition, Vector2f rectSize) {
		return x >= rectPosition.x && x < rectPosition.x + rectSize.x
			&& y >= rectPosition.y && y < rectPosition.y + rectSize.y;
	}
	
	public NodeContext getContext() {
		LayoutNode layoutNode = entity.getComponentInParent(LayoutNode.class);
		if (layoutNode != null) {
			return layoutNode;
		}
		
		return getRoot();
	}
	
	public boolean isInLayout() {
		LayoutNode layoutNode = entity.getComponentInParent(LayoutNode.class);
		
		if (layoutNode == null) {
			return false;
		}
		
		return layoutNode.arrangeChildren;
	}
	
	public boolean isInTooltip() {
		return entity != null && getComponentInParent(TooltipNode.class) != null;
	}
	
	/**
	 * Gets the node root of this node.
	 * @throws IllegalStateException When this entity is not a child of an entity with a node root component.
	 */
	public NodeRoot getRoot() throws IllegalStateException {
		if (root != null) {
			return root;
		}
		
		NodeRoot nodeRoot = entity.getComponentInParent(NodeRoot.class);
		
		if (nodeRoot == null) {
			throw new IllegalStateException("node must be a child of a node root: " + entity);
		}
		
		root = nodeRoot;
		return nodeRoot;
	}
	
	public float getOuterSizeX() {
		return getInnerSizeX() + getMarginX() * 2;
	}
	
	public float getOuterSizeY() {
		return getInnerSizeY() + getMarginY() * 2;
	}
	
	public float getInnerSizeX() {
		return size.computeX(this) + getPaddingX() * 2;
	}
	
	public float getInnerSizeY() {
		return size.computeY(this) + getPaddingY() * 2;
	}
	
	public float getPaddingX() {
		return padding != null ? padding.computeX(this) : 0;
	}
	
	public float getPaddingY() {
		return padding != null ? padding.computeY(this) : 0;
	}
	
	public float getX() {
		return getMarginX() + offset.x;
	}
	
	public float getY() {
		return getMarginY() + offset.y;
	}
	
	public float getMarginX() {
		return margin != null ? margin.computeX(this) : 0;
	}
	
	public float getMarginY() {
		return margin != null ? margin.computeY(this) : 0;
	}
	
	@Override
	public void addListener(NodeEvent eventType, EventListener listener) {
		eventDispatcher.addListener(eventType, listener);
	}
	
	@Override
	public void removeListener(NodeEvent eventType, EventListener listener) {
		eventDispatcher.removeListener(eventType, listener);
	}
	
	@Override
	public void invoke(NodeEvent eventType) {
		eventDispatcher.invoke(eventType);
	}
	
	public void toggleClass(String className) {
		toggleClass(className, classes.contains(className));
	}
	
	public void toggleClass(String className, boolean active) {
		if (active) {
			addClass(className);
		} else {
			removeClass(className);
		}
	}
	
	public void addClass(String className) {
		if (classes.add(className)) {
			invoke(NodeEvent.SELECTOR_CHANGE);
		}
	}
	
	public void removeClass(String className) {
		if (classes.remove(className)) {
			invoke(NodeEvent.SELECTOR_CHANGE);
		}
	}
	
	public void toggleModifier(String modifier) {
		toggleClass(modifier, modifiers.contains(modifier));
	}
	
	public void toggleModifier(String modifier, boolean active) {
		if (active) {
			addModifier(modifier);
		} else {
			removeModifier(modifier);
		}
	}
	
	public void addModifier(String modifier) {
		if (modifiers.add(modifier)) {
			invoke(NodeEvent.SELECTOR_CHANGE);
		}
	}
	
	public void removeModifier(String modifier) {
		if (modifiers.remove(modifier)) {
			invoke(NodeEvent.SELECTOR_CHANGE);
		}
	}
}
