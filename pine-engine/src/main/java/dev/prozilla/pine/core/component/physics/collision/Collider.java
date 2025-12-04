package dev.prozilla.pine.core.component.physics.collision;

import dev.prozilla.pine.common.math.vector.Vector2f;
import dev.prozilla.pine.common.util.checks.Checks;
import dev.prozilla.pine.core.component.Component;

public class Collider extends Component {
	
	/** The offset of this collider from the entity's position. */
	public Vector2f offset;
	
	/**
	 * Creates a collider with no offset.
	 */
	public Collider() {
		this(new Vector2f());
	}
	
	/**
	 * Creates a collider with a given offset.
	 * @param offset The offset of the collider
	 */
	public Collider(Vector2f offset) {
		this.offset = Checks.isNotNull(offset, "offset");
	}
	
	/**
	 * Checks if this collider collides with another collider.
	 * @param other The other collider
	 * @return {@code true} if the colliders are colliding.
	 */
	public boolean collidesWith(Collider other) {
		return false;
	}
	
	public Vector2f getOrigin() {
		return new Vector2f(getOriginX(), getOriginY());
	}
	
	public float getOriginX() {
		return getTransform().position.x + offset.x;
	}
	
	public float getOriginY() {
		return getTransform().position.y + offset.y;
	}

	
	
}
