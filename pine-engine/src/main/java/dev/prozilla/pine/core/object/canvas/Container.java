package dev.prozilla.pine.core.object.canvas;

import dev.prozilla.pine.core.Game;
import dev.prozilla.pine.core.object.GameObject;
import dev.prozilla.pine.core.component.canvas.CanvasGroup;
import dev.prozilla.pine.core.component.canvas.RectTransform;
import dev.prozilla.pine.common.system.resource.Color;

import java.util.Objects;

/**
 * Represents a group of UI elements on the canvas.
 * Wrapper object for the {@link CanvasGroup} component.
 */
public class Container extends CanvasElement {
	
	protected final CanvasGroup group;
	
	public Container(Game game) {
		this(game, CanvasGroup.Direction.DOWN);
	}
	
	public Container(Game game, CanvasGroup.Direction direction) {
		super(game);
		
		group = new CanvasGroup();
		addComponent(group);
		
		if (direction != null) {
			group.direction = direction;
		}
	}
	
	@Override
	public GameObject addChild(GameObject child) throws IllegalStateException {
		super.addChild(child);
		group.getChildComponents();
		return child;
	}
	
	@Override
	public void removeChild(GameObject child) throws IllegalStateException {
		super.removeChild(child);
		group.getChildComponents();
	}
	
	@Override
	public String getName() {
		return getName("Container");
	}
	
	@Override
	public Container setPosition(RectTransform.Anchor anchor, int x, int y) {
		return (Container)super.setPosition(anchor, x, y);
	}
	
	@Override
	public Container setAnchor(RectTransform.Anchor anchor) {
		return (Container)super.setAnchor(anchor);
	}
	
	@Override
	public Container setOffset(int x, int y) {
		return (Container)super.setOffset(x, y);
	}
	
	/**
	 * Sets the direction in which children are placed.
	 * @param direction Direction
	 */
	public Container setDirection(CanvasGroup.Direction direction) {
		group.direction = Objects.requireNonNullElse(direction, CanvasGroup.DEFAULT_DIRECTION);
		return this;
	}
	
	/**
	 * Sets the background color for this container.
	 * The default value is <code>null</code>, which disables the background of this container.
	 * @param color Background color
	 */
	public Container setBackgroundColor(Color color) {
		group.backgroundColor = color;
		return this;
	}
	
	/**
	 * Sets the distance between children, in pixels.
	 * @param gap Gap, in pixels
	 */
	public Container setGap(int gap) {
		group.gap = gap;
		return this;
	}
	
	/**
	 * Sets the alignment of children relative to the direction of this container.
	 */
	public Container setAlignment(CanvasGroup.Alignment alignment) {
		group.alignment = alignment;
		return this;
	}
	
	/**
	 * Sets the padding around elements in this container.
	 */
	public Container setPadding(int padding) {
		setPadding(padding, padding);
		return this;
	}
	
	/**
	 * Sets the horizontal and vertical padding around elements in this container.
	 * @param paddingX Horizontal padding
	 * @param paddingY Vertical padding
	 */
	public Container setPadding(int paddingX, int paddingY) {
		setHorizontalPadding(paddingX);
		setVerticalPadding(paddingY);
		return this;
	}
	
	public Container setHorizontalPadding(int paddingX) {
		group.paddingX = paddingX;
		return this;
	}
	
	public Container setVerticalPadding(int paddingY) {
		group.paddingY = paddingY;
		return this;
	}
}
