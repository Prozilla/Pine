package dev.prozilla.pine.core.component.ui;

import dev.prozilla.pine.common.asset.image.TextureAsset;
import dev.prozilla.pine.common.event.Event;
import dev.prozilla.pine.common.event.EventDispatcher;
import dev.prozilla.pine.common.event.EventDispatcherProvider;
import dev.prozilla.pine.common.math.dimension.DimensionBase;
import dev.prozilla.pine.common.math.dimension.DualDimension;
import dev.prozilla.pine.common.math.vector.*;
import dev.prozilla.pine.common.property.style.StyleSheet;
import dev.prozilla.pine.common.system.Color;
import dev.prozilla.pine.core.component.Component;
import dev.prozilla.pine.core.component.ComponentQuery;
import dev.prozilla.pine.core.component.animation.AnimationData;
import dev.prozilla.pine.core.component.ui.style.LineStyle;
import dev.prozilla.pine.core.component.ui.style.NodeStyle;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.prefab.Prefab;
import dev.prozilla.pine.core.rendering.mesh.Line;
import dev.prozilla.pine.core.state.input.CursorType;

import java.util.*;

/**
 * Component for nodes that make up the user interface.
 *
 * <p>Nodes are similar to HTML elements and the <a href="https://developer.mozilla.org/en-US/docs/Learn_web_development/Core/Styling_basics/Box_model">CSS box model</a>.</p>
 */
// TODO: Split everything into 4 sides (padding, margin, border, etc.)
public class Node extends Component implements EventDispatcherProvider<NodeEvent.Type, Node, NodeEvent> {
	
	// Current state
	public Vector2f currentPosition;
	public Vector2f currentInnerSize;
	public Vector2f currentOuterSize;
	public Vector2f offset;
	public boolean cursorHit;
	public boolean readyToRender;
	public int iterations;
	
	// Attributes
	public Anchor anchor;
	/** If true, allows the cursor to pass through this element. */
	public boolean passThrough;
	/** If true, this node won't be arranged by a layout node. */
	public boolean absolutePosition;
	/** If true, this node won't be rendered by the node rendering system. */
	public boolean controlledRender;
	public String tooltipText;
	public int tabIndex;
	public boolean autoFocus;
	public boolean alwaysVisibleFocus;
	public String pseudoName;
	
	// Style
	public NodeStyle nodeStyle;
	public Color color;
	public Color backgroundColor;
	public DualDimension size;
	public DualDimension padding;
	public DualDimension margin;
	public CursorType cursor;
	
	// Border style
	public DimensionBase borderWidth;
	public Color borderColor;
	public TextureAsset borderImage;
	public Vector4f borderImageSlice;
	public boolean borderImageSliceFill;
	public LineStyle borderStyle;
	
	// Outline style
	public DimensionBase outlineWidth;
	public Color outlineColor;
	public LineStyle outlineStyle;
	public DimensionBase outlineOffset;
	
	// Meshes
	public Line borderMesh;
	public Line outlineMesh;
	
	public String htmlTag;
	public final Set<String> classes;
	public final Set<String> modifiers;
	
	// Hierarchy
	public NodeRoot root;
	public Node parent;
	public final List<Node> children;
	public final Map<String, Node> pseudoElements;
	
	private final NodeEventDispatcher eventDispatcher;
	
	// Defaults
	public static final Color DEFAULT_COLOR = Color.white();
	public static final Color DEFAULT_BACKGROUND_COLOR = Color.transparent();
	public static final Color DEFAULT_BORDER_COLOR = Color.transparent();
	public static final Color DEFAULT_OUTLINE_COLOR = Color.transparent();
	public static final Anchor DEFAULT_ANCHOR = Anchor.BOTTOM_LEFT;
	
	// Modifiers
	public static final String HOVER_MODIFIER = "hover";
	public static final String FOCUS_MODIFIER = "focus";
	public static final String FOCUS_VISIBLE_MODIFIER = "focus-visible";
	
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
		controlledRender = false;
		tabIndex = -1;
		autoFocus = false;
		alwaysVisibleFocus = false;
		
		size = new DualDimension();
		
		classes = new HashSet<>();
		modifiers = new HashSet<>();
		
		eventDispatcher = new NodeEventDispatcher();
		children = new ArrayList<>();
		pseudoElements = new HashMap<>();
	}
	
	@Override
	protected void onEntityChange(Entity oldEntity, Entity newEntity) {
		if (oldEntity != null) {
			oldEntity.removeListener(Entity.EventType.PARENT_UPDATE, this::handleParentChange);
			oldEntity.removeListener(Entity.EventType.CHILDREN_UPDATE, this::handleChildrenChange);
		}
		if (entity != null) {
			entity.addListener(Entity.EventType.PARENT_UPDATE, this::handleParentChange);
			entity.addListener(Entity.EventType.CHILDREN_UPDATE, this::handleChildrenChange);
		}
	}
	
	public void updateHierarchy() {
		handleParentChange(null);
		handleChildrenChange(null);
	}
	
	private void handleParentChange(Event<Entity.EventType, Entity> event) {
		parent = entity.getComponentAbove(Node.class, ComponentQuery.SHALLOW);
		invalidateSelector();
	}
	
	private void handleChildrenChange(Event<Entity.EventType, Entity> event) {
		children.clear();
		children.addAll(entity.getComponentsBelow(Node.class));
		if (nodeStyle != null) {
			for (Node childNode : children) {
				childNode.addStyleSheets(nodeStyle.getStyleSheets());
			}
		}
		invalidateSelector();
	}
	
	public void updateBorderMesh() {
		float borderWidth = getBorderWidth();
		if (borderWidth <= 0 || borderColor == null) {
			borderMesh = null;
			return;
		}
		
		if (borderMesh == null) {
			borderMesh = new Line();
		}
		
		borderMesh.setThickness(new float[]{ borderWidth });
		borderMesh.setOrigin(new Vector3f(currentPosition.x, currentPosition.y, getTransform().position.z));
		borderMesh.setClosed(true);
		
		float x = borderWidth / 2f;
		float y = borderWidth / 2f;
		float width = currentInnerSize.x - borderWidth;
		float height = currentInnerSize.y - borderWidth;
		borderMesh.setPoints(Line.pointsOnRect(x, y, width, height));
	}
	
	public void updateOutlineMesh() {
		float outlineWidth = getOutlineWidth();
		if (outlineWidth <= 0 || outlineColor == null) {
			outlineMesh = null;
			return;
		}
		
		if (outlineMesh == null) {
			outlineMesh = new Line();
		}
		
		outlineMesh.setThickness(new float[]{ outlineWidth });
		outlineMesh.setOrigin(new Vector3f(currentPosition.x, currentPosition.y, getTransform().position.z));
		outlineMesh.setClosed(true);
		
		float offset = getOutlineOffset();
		float x = outlineWidth / -2f - offset;
		float y = outlineWidth / -2f - offset;
		float width = currentInnerSize.x + outlineWidth + offset * 2f;
		float height = currentInnerSize.y + outlineWidth + offset * 2f;
		outlineMesh.setPoints(Line.pointsOnRect(x, y, width, height));
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
		LayoutNode layoutNode = entity.getComponentAbove(LayoutNode.class);
		if (layoutNode != null) {
			return layoutNode;
		}
		
		return getRoot();
	}
	
	public boolean isInLayout() {
		LayoutNode layoutNode = entity.getComponentAbove(LayoutNode.class, ComponentQuery.SHALLOW);
		
		if (layoutNode == null) {
			return false;
		}
		
		return layoutNode.arrangeChildren;
	}
	
	public boolean isInTooltip() {
		return entity != null && getComponentAbove(TooltipNode.class) != null;
	}
	
	/**
	 * Gets the node root of this node.
	 * @throws IllegalStateException When this entity is not a child of an entity with a node root component.
	 */
	public NodeRoot getRoot() throws IllegalStateException {
		if (root != null) {
			return root;
		}
		
		NodeRoot nodeRoot = entity.getComponentAbove(NodeRoot.class);
		
		if (nodeRoot == null) {
			throw new IllegalStateException("node must be a child of a node root: " + entity);
		}
		
		root = nodeRoot;
		return nodeRoot;
	}
	
	public float getTotalWidth() {
		return getBoxWidth() + getMarginX() * 2;
	}
	
	public float getTotalHeight() {
		return getBoxHeight() + getMarginY() * 2;
	}
	
	public float getBoxWidth() {
		return size.computeX(this) + getBoxX() * 2;
	}
	
	public float getBoxHeight() {
		return size.computeY(this) + getBoxY() * 2;
	}
	
	public float getBoxX() {
		return getPaddingX() + getBorderWidth();
	}
	
	public float getBoxY() {
		return getPaddingY() + getBorderWidth();
	}
	
	private float getPaddingX() {
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
	
	public float getBorderWidth() {
		return borderWidth != null && borderStyle != LineStyle.NONE ? borderWidth.compute(this, true) : 0;
	}
	
	public float getOutlineWidth() {
		return outlineWidth != null && outlineStyle != LineStyle.NONE ? outlineWidth.compute(this, true) : 0;
	}
	
	public float getOutlineOffset() {
		return outlineOffset != null ? outlineOffset.compute(this, true) : 0;
	}
	
	@Override
	public EventDispatcher<NodeEvent.Type, Node, NodeEvent> getEventDispatcher() {
		return eventDispatcher;
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
			invalidateSelector();
		}
	}
	
	public void removeClass(String className) {
		if (classes.remove(className)) {
			invalidateSelector();
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
			invalidateSelector();
		}
	}
	
	public void removeModifier(String modifier) {
		if (modifiers.remove(modifier)) {
			invalidateSelector();
		}
	}
	
	public Node getPseudoElement(String name) {
		return pseudoElements.get(name);
	}
	
	public Node addPseudoElement(String name, Prefab pseudoPrefab) {
		return addPseudoElement(name, getEntity().addChild(pseudoPrefab));
	}
	
	public Node addPseudoElement(String name, Entity pseudoChild) {
		Node pseudoElement = pseudoChild.getComponent(Node.class);
		if (pseudoElement == null) {
			return null;
		}
		addPseudoElement(name, pseudoElement);
		return pseudoElement;
	}
	
	public void addPseudoElement(String name, Node pseudoElement) {
		Node previous = pseudoElements.put(name, pseudoElement);
		if (previous != null) {
			getEntity().removeChild(previous.getEntity());
		}
		pseudoElement.pseudoName = name;
		pseudoElement.invalidateSelector();
		getEntity().addChild(pseudoElement.getEntity());
		if (nodeStyle != null) {
			pseudoElement.addStyleSheets(nodeStyle.getStyleSheets());
		}
	}
	
	public void removePseudoElement(String name) {
		Node pseudoElement = pseudoElements.remove(name);
		if (pseudoElement != null) {
			getEntity().removeChild(pseudoElement.getEntity());
		}
	}
	
	public void addStyleSheets(Set<StyleSheet> styleSheets) {
		for (StyleSheet styleSheet : styleSheets) {
			addStyleSheet(styleSheet);
		}
	}
	
	public void addStyleSheet(StyleSheet styleSheet) {
		getNodeStyle().applyStyleSheet(styleSheet);
	}
	
	public NodeStyle getNodeStyle() {
		if (nodeStyle == null) {
			nodeStyle = getEntity().getOrAddComponent(NodeStyle.class, () -> {
				AnimationData animationData = getEntity().getOrAddComponent(AnimationData.class, AnimationData::new);
				return new NodeStyle(animationData, this);
			});
		}
		return nodeStyle;
	}
	
	private void invalidateSelector() {
		invoke(NodeEvent.Type.SELECTOR_CHANGE);
	}
	
	public void click() {
		invoke(NodeEvent.Type.CLICK);
	}
	
	public void focus() {
		if (getRoot().focusNode(this, false)) {
			invoke(NodeEvent.Type.FOCUS);
		}
	}
	
	public boolean isFocused() {
		Node focusedNode = getRoot().getFocusedNode();
		return focusedNode != null && focusedNode.equals(this);
	}
	
	public boolean canBeRendered() {
		return readyToRender && !controlledRender;
	}
	
	public void invoke(NodeEvent.Type type) {
		invoke(type, this);
	}
	
	@Override
	public void destroy() {
		getRoot().removeNode(this);
		super.destroy();
		eventDispatcher.destroy();
	}
	
}
