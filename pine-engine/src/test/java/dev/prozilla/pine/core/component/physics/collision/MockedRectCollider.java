package dev.prozilla.pine.core.component.physics.collision;

import dev.prozilla.pine.common.math.vector.Vector2f;

public class MockedRectCollider extends RectCollider {
	
	public MockedRectCollider(Vector2f size) {
		super(size);
	}
	
	public MockedRectCollider(Vector2f size, Vector2f offset) {
		super(size, offset);
	}
	
	@Override
	public float getOriginX() {
		return offset.x;
	}
	
	@Override
	public float getOriginY() {
		return offset.y;
	}
}
