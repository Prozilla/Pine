package dev.prozilla.pine.core.rendering.shape;

import dev.prozilla.pine.common.Cloneable;
import dev.prozilla.pine.common.math.vector.GridAlignment;
import dev.prozilla.pine.common.math.vector.Vector2f;
import dev.prozilla.pine.common.util.checks.Checks;

import java.util.Objects;

public class Rect extends Shape implements Cloneable<Rect> {
	
	protected Vector2f position;
	protected Vector2f size;
	protected GridAlignment anchor;
	
	public Rect(Vector2f position, Vector2f size) {
		this(position, size, GridAlignment.BOTTOM_LEFT);
	}
	
	public Rect(Vector2f position, Vector2f size, GridAlignment anchor) {
		this.position = Checks.isNotNull(position, "position");
		this.size = Checks.isNotNull(size, "size");
		this.anchor = Checks.isNotNull(anchor, "anchor");
	}
	
	@Override
	protected float[] generateVertices() {
		float x1 = position.x - (anchor.x * size.x);
		float y1 = position.y - (anchor.y * size.y);
		float x2 = position.x + ((1 - anchor.x) * size.x);
		float y2 = position.y + ((1 - anchor.y) * size.y);
		
		return new float[] {
			x1, y1,
			x1, y2,
			x2, y2,
			x1, y1,
			x2, y2,
			x2, y1
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
	
	public float getX() {
		return position.x;
	}
	
	public float getY() {
		return position.y;
	}
	
	public void setX(float x) {
		if (x == position.x) {
			return;
		}
		
		position.x = x;
		isDirty = true;
	}
	
	public void setY(float y) {
		if (y == position.y) {
			return;
		}
		
		position.y = y;
		isDirty = true;
	}
	
	public void setPosition(Vector2f position) {
		Checks.isNotNull(position, "position");
		
		if (position.equals(this.position)) {
			return;
		}
		
		this.position = position;
		isDirty = true;
	}
	
	public float getWidth() {
		return size.x;
	}
	
	public float getHeight() {
		return size.y;
	}
	
	public void setWidth(float width) {
		if (width == size.x) {
			return;
		}
		
		size.x = width;
		isDirty = true;
	}
	
	public void setHeight(float height) {
		if (height == size.y) {
			return;
		}
		
		size.y = height;
		isDirty = true;
	}
	
	public void setSize(Vector2f size) {
		Checks.isNotNull(size, "size");
		
		if (size.equals(this.size)) {
			return;
		}
		
		this.size = size;
		isDirty = true;
	}
	
	public void setAnchor(GridAlignment anchor) {
		Checks.isNotNull(anchor, "anchor");
		
		if (anchor == this.anchor) {
			return;
		}
		
		this.anchor = anchor;
		isDirty = true;
	}
	
	@Override
	public boolean equals(Rect rect) {
		return rect != null && Objects.equals(rect.position, position) && Objects.equals(rect.size, size);
	}
	
	@Override
	public Rect clone() {
		return new Rect(position, size);
	}
	
}
