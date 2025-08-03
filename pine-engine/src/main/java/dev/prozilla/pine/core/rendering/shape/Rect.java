package dev.prozilla.pine.core.rendering.shape;

import dev.prozilla.pine.common.math.vector.Vector2f;

public class Rect extends Shape {
	
	private Vector2f position;
	private Vector2f size;
	
	public Rect(Vector2f position, Vector2f size) {
		this.position = position;
		this.size = size;
	}
	
	@Override
	protected float[] generateVertices() {
		float x1 = position.x;
		float y1 = position.y;
		float x2 = position.x + size.x;
		float y2 = position.y + size.y;
		
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
			1,1,
			0, 0,
			1, 1,
			1, 0
		};
	}
	
	public void setPosition(Vector2f position) {
		this.position = position;
		isDirty = true;
	}
	
	public void setSize(Vector2f size) {
		this.size = size;
		isDirty = true;
	}
	
}
