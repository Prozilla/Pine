package dev.prozilla.pine.core.rendering.mesh;

import dev.prozilla.pine.common.math.vector.Anchor;
import dev.prozilla.pine.common.math.vector.Vector2f;
import dev.prozilla.pine.common.math.vector.Vector3f;
import dev.prozilla.pine.common.util.checks.Checks;

import java.util.Objects;

/**
 * Generates a rectangular shape.
 */
public class Rect extends Mesh {
	
	protected final Vector2f size;
	protected Anchor anchor;
	
	public Rect() {
		this(new Vector3f(), new Vector2f());
	}
	
	public Rect(Vector3f origin, Vector2f size) {
		this(origin, size, Anchor.BOTTOM_LEFT);
	}
	
	public Rect(Vector3f origin, Vector2f size, Anchor anchor) {
		super(origin);
		this.size = Checks.isNotNull(size, "size");
		this.anchor = Checks.isNotNull(anchor, "anchor");
	}
	
	@Override
	protected float[] generateVertices() {
		float x1 = origin.x - (anchor.x * size.x);
		float y1 = origin.y - (anchor.y * size.y);
		float x2 = origin.x + ((1 - anchor.x) * size.x);
		float y2 = origin.y + ((1 - anchor.y) * size.y);
		float z = origin.z;
		
		return new float[] {
			x1, y1, z,
			x1, y2, z,
			x2, y2, z,
			x1, y1, z,
			x2, y2, z,
			x2, y1, z
		};
	}
	
	@Override
	protected float[] generateUVs() {
		return new float[] {
			0, 0,
			0, 1,
			1, 1,
			0, 0,
			1, 1,
			1, 0
		};
	}
	
	/**
	 * Returns the width of this rectangle.
	 * @return The width of this rectangle.
	 */
	public float getWidth() {
		return size.x;
	}
	
	/**
	 * Returns the height of this rectangle.
	 * @return The height of this rectangle.
	 */
	public float getHeight() {
		return size.y;
	}
	
	/**
	 * Sets the width of this rectangle.
	 * @param width The new width
	 */
	public void setWidth(float width) {
		if (width == size.x) {
			return;
		}
		
		size.x = width;
		markAsDirty();
	}
	
	/**
	 * Sets the height of this rectangle.
	 * @param height The new height
	 */
	public void setHeight(float height) {
		if (height == size.y) {
			return;
		}
		
		size.y = height;
		markAsDirty();
	}
	
	/**
	 * Sets the size of this rectangle.
	 * @param size The new size
	 */
	public void setSize(Vector2f size) {
		Checks.isNotNull(size, "size");
		
		if (size.equals(this.size)) {
			return;
		}
		
		this.size.set(size);
		markAsDirty();
	}
	
	/**
	 * Sets the anchor point of this rectangle.
	 * @param anchor The new anchor point
	 */
	public void setAnchor(Anchor anchor) {
		Checks.isNotNull(anchor, "anchor");
		
		if (anchor == this.anchor) {
			return;
		}
		
		this.anchor = anchor;
		markAsDirty();
	}
	
	@Override
	public boolean equals(Object object) {
		return object == this || (object instanceof Rect rect && equals(rect));
	}
	
	@Override
	public boolean equals(Mesh mesh) {
		return mesh == this || (mesh instanceof Rect rect && equals(rect));
	}
	
	public boolean equals(Rect rect) {
		return rect != null && Objects.equals(rect.origin, origin) && Objects.equals(rect.size, size);
	}
	
	@Override
	public Rect clone() {
		return new Rect(origin, size);
	}
	
}
