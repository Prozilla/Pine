package dev.prozilla.pine.core.component.canvas;

import dev.prozilla.pine.common.system.resource.Color;
import dev.prozilla.pine.core.rendering.Renderer;

import java.util.ArrayList;

/**
 * A component that groups multiple canvas elements together and aligns them.
 */
public class CanvasGroup extends RectTransform {
	
	public Direction direction;
	public Alignment alignment;
	public Color backgroundColor;
	/** Distance between elements. */
	public int gap;
	/** Horizontal padding around elements. */
	public int paddingX;
	/** Vertical padding around elements. */
	public int paddingY;
	
	private int innerWidth;
	private int innerHeight;
	
	/** Array of rect components in children of the attached game object */
	protected ArrayList<RectTransform> childRects;
	
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
		this("CanvasGroup");
	}
	
	public CanvasGroup(String name) {
		this(name, DEFAULT_DIRECTION);
	}
	
	public CanvasGroup(String name, Direction direction) {
		this(name, direction, DEFAULT_ALIGNMENT);
	}
	
	public CanvasGroup(String name, Direction direction, Alignment alignment) {
		super(name);
		
		this.direction = direction;
		this.alignment = alignment;
		
		childRects = new ArrayList<>();
		gap = 0;
		paddingX = 0;
		paddingY = 0;
		innerWidth = 0;
		innerHeight = 0;
	}
	
	@Override
	public void input(float deltaTime) {
		passThrough = (backgroundColor == null);
		super.input(deltaTime);
	}
	
	@Override
	public void init(long window) {
		super.init(window);
		getChildComponents();
	}
	
	/**
	 * Calculates this group's dimensions and sets the position of this group
	 * on the screen based on the dimensions and anchor point.
	 */
	@Override
	public void update(float deltaTime) {
		int newWidth = 0;
		int newHeight = 0;
		
		if (!childRects.isEmpty()) {
			for (RectTransform childRect : childRects) {
				switch (direction) {
					case UP:
					case DOWN:
						newWidth = Math.max(newWidth, childRect.getWidth());
						newHeight += childRect.getHeight() + gap;
						break;
					case LEFT:
					case RIGHT:
						newWidth += childRect.getWidth() + gap;
						newHeight = Math.max(newHeight, childRect.getHeight());
						break;
				}
			}
			
			// Subtract gap for last element
			if (direction == Direction.UP || direction == Direction.DOWN) {
				newHeight -= gap;
			} else {
				newWidth -= gap;
			}
		}
		
		innerWidth = newWidth;
		innerHeight = newHeight;
		
		width = innerWidth + paddingX * 2;
		height = innerHeight + paddingY * 2;
		
		super.update(deltaTime);
		
		arrangeChildren();
	}
	
	/**
	 * Renders a rectangle with the background color if background color is set.
	 */
	@Override
	public void render(Renderer renderer) {
		if (width != 0 && height != 0 && backgroundColor != null) {
			renderer.drawRect(x, y, width, height, backgroundColor);
		}
		
		super.render(renderer);
	}
	
	/**
	 * Arranges this group's children by setting their offset values relative to the
	 * bottom left corner of the screen.
	 */
	private void arrangeChildren() {
		if (childRects.isEmpty()) {
			return;
		}
		
		int offsetX = x + paddingX;
		int offsetY = y + paddingY;
		
		if (direction == Direction.DOWN) {
			offsetY += innerHeight - childRects.get(0).getHeight();
		} else if (direction == Direction.LEFT) {
			offsetX += innerWidth - childRects.get(0).getWidth();
		}
		
		for (RectTransform childRect : childRects) {
			childRect.setPosition(Anchor.BOTTOM_LEFT, offsetX, offsetY);
			
			// Calculate alignments
			if (direction == Direction.UP || direction == Direction.DOWN) {
				if (alignment == Alignment.END) {
					childRect.setOffsetX(offsetX + (innerWidth - childRect.getWidth()));
				} else if (alignment == Alignment.CENTER) {
					childRect.setOffsetX(offsetX + (innerWidth - childRect.getWidth()) / 2);
				}
			} else if (direction == Direction.LEFT || direction == Direction.RIGHT) {
				if (alignment == Alignment.END) {
					childRect.setOffsetY(offsetY + (innerHeight - childRect.getHeight()));
				} else if (alignment == Alignment.CENTER) {
					childRect.setOffsetY(offsetY + (innerHeight - childRect.getHeight()) / 2);
				}
			}

			// Move offset for next child rect
			switch (direction) {
				case RIGHT:
					offsetX += childRect.getWidth() + gap;
					break;
				case LEFT:
					offsetX -= (innerWidth - childRect.getWidth());
					break;
				case UP:
					offsetY += childRect.getHeight() + gap;
					break;
				case DOWN:
					offsetY -= childRect.getHeight() + gap;
					break;
			}
		}
	}
	
	/**
	 * Finds and stores rect components in children to be used in calculations later.
	 */
	public void getChildComponents() {
		childRects = gameObject.getComponentsInChildren(RectTransform.class);
	}
}
