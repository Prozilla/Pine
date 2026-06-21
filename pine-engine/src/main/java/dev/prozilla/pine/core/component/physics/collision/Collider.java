package dev.prozilla.pine.core.component.physics.collision;

import dev.prozilla.pine.Pine;
import dev.prozilla.pine.common.Experimental;
import dev.prozilla.pine.common.math.vector.Vector3f;
import dev.prozilla.pine.common.system.Color;
import dev.prozilla.pine.common.util.checks.Checks;
import dev.prozilla.pine.core.component.Component;
import dev.prozilla.pine.core.rendering.Renderer;
import dev.prozilla.pine.core.rendering.mesh.ColoredRenderable;

public class Collider extends Component implements ColoredRenderable {
	
	/** The offset of this collider from the entity's position. */
	public Vector3f offset;
	
	public static final Color DEFAULT_COLOR = new Color(140, 255, 140, 175);
	
	/**
	 * Creates a collider with a given offset.
	 * @param offset The offset of the collider
	 */
	public Collider(Vector3f offset) {
		this.offset = Checks.isNotNull(offset, "offset");
	}
	
	/**
	 * Checks if this collider collides with another collider.
	 * @param other The other collider
	 * @return {@code true} if the colliders are colliding.
	 */
	@Experimental
	public boolean collidesWith(Collider other) {
		Pine.useExperimentalFeature();
		return false;
	}
	
	public Vector3f getOrigin() {
		return new Vector3f(getOriginX(), getOriginY(), getOriginZ());
	}
	
	public float getOriginX() {
		return getTransform().position.x + offset.x;
	}
	
	public float getOriginY() {
		return getTransform().position.y + offset.y;
	}
	
	public float getOriginZ() {
		return getTransform().position.z + offset.z;
	}
	
	/**
	 * Draws this collider in a green tint.
	 * @param renderer The renderer
	 */
	@Override
	public void draw(Renderer renderer) {
		draw(renderer, DEFAULT_COLOR);
	}
	
	/**
	 * Draws this collider in a given color.
	 * @param renderer The renderer
	 * @param color The color to draw with
	 */
	@Override
	public void draw(Renderer renderer, Color color) {
	
	}
	
}
