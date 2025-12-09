package dev.prozilla.pine.core.component.physics.collision;

import dev.prozilla.pine.Pine;
import dev.prozilla.pine.common.Experimental;
import dev.prozilla.pine.common.math.vector.Vector2f;
import dev.prozilla.pine.common.system.Color;
import dev.prozilla.pine.common.util.checks.Checks;
import dev.prozilla.pine.core.rendering.Renderer;

/**
 * An axis-aligned bounding box collider.
 */
public class RectCollider extends Collider {

	public Vector2f size;
	
	public RectCollider(Vector2f size) {
		this(size, new Vector2f());
	}
	
	public RectCollider(Vector2f size, Vector2f offset) {
		super(offset);
		this.size = Checks.isNotNull(size, "size");
	}
	
	@Experimental
	public boolean collidesWith(RectCollider other) {
		Pine.useExperimentalFeature();
		return !(getLeft() > other.getRight()
			|| getRight() < other.getLeft()
			|| getBottom() < other.getTop()
			|| getTop() > other.getBottom());
	}
	
	@Experimental
	public boolean collidesWith(CircleCollider other) {
		return other.collidesWith(this);
	}
	
	@Experimental
	@Override
	public boolean collidesWith(Collider other) {
		return other.collidesWith(this);
	}
	
	/**
	 * Checks if a given point is inside this rectangle.
	 * @return {@code true} if the point is inside this rectangle.
	 */
	public boolean isInside(Vector2f point) {
		return isInside(point.x, point.y);
	}
	
	/**
	 * Checks if a given point is inside this rectangle.
	 * @param x The x value of the point
	 * @param y The y value of the point
	 * @return {@code true} if the point is inside this rectangle.
	 */
	public boolean isInside(float x, float y) {
		return (x >= getLeft() && x <= getRight()
			&& y >= getBottom() && y <= getTop());
	}
	
	/**
	 * Calculates the point at the center of this rectangle.
	 * @return The point at the center of this rectangle.
	 */
	public Vector2f getCenter() {
		return new Vector2f(getCenterX(), getCenterY());
	}
	
	/**
	 * Calculates the x value of the center of this rectangle.
	 * @return The horizontal center of this rectangle.
	 */
	public float getCenterX() {
		return getOriginX() + size.x / 2f;
	}
	
	/**
	 * Calculates the y value of the center of this rectangle.
	 * @return The vertical center of this rectangle.
	 */
	public float getCenterY() {
		return getOriginY() + size.y / 2f;
	}
	
	/**
	 * Calculates the x value of the right edge of this rectangle.
	 * @return The maximum x value.
	 */
	public float getRight() {
		return getLeft() + size.x;
	}
	
	/**
	 * Calculates the x value of the left edge of this rectangle.
	 * @return The minimum x value.
	 */
	public float getLeft() {
		return getOriginX();
	}
	
	/**
	 * Calculates the y value of the top edge of this rectangle.
	 * @return The maximum y value.
	 */
	public float getTop() {
		return getBottom() + size.y;
	}
	
	/**
	 * Calculates the y value of the bottom edge of this rectangle.
	 * @return The minimum y value.
	 */
	public float getBottom() {
		return getOriginY();
	}
	
	@Override
	public void draw(Renderer renderer, Color color, float depth) {
		Vector2f position = getScene().getCameraData().applyTransform(getOrigin());
		renderer.drawRect(position.x, position.y, depth, size.x, size.y, color);
	}
	
}
