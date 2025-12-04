package dev.prozilla.pine.core.component.physics.collision;

import dev.prozilla.pine.common.math.vector.Vector2f;
import dev.prozilla.pine.common.util.checks.Checks;

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
	
	public boolean collidesWith(RectCollider other) {
		return !(getLeft() > other.getRight()
			|| getRight() < other.getLeft()
			|| getBottom() < other.getTop()
			|| getTop() > other.getBottom());
	}
	
	public boolean collidesWith(CircleCollider other) {
		return other.collidesWith(this);
	}
	
	@Override
	public boolean collidesWith(Collider other) {
		return other.collidesWith(this);
	}
	
	public boolean isInside(Vector2f point) {
		return isInside(point.x, point.y);
	}
	
	public boolean isInside(float x, float y) {
		return (x >= getLeft() && x <= getRight()
			&& y >= getBottom() && y <= getTop());
	}
	
	public Vector2f getCenter() {
		return new Vector2f(getCenterX(), getCenterY());
	}
	
	public float getCenterX() {
		return getOriginX() + size.x / 2f;
	}
	
	public float getCenterY() {
		return getOriginY() + size.y / 2f;
	}
	
	public float getRight() {
		return getLeft() + size.x;
	}
	
	public float getLeft() {
		return getOriginX();
	}
	
	public float getTop() {
		return getBottom() + size.y;
	}
	
	public float getBottom() {
		return getOriginY();
	}
	
}
