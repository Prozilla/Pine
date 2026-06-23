package dev.prozilla.pine.core.component.physics.collision;

import dev.prozilla.pine.common.math.vector.Vector3f;

public class MockedCircleCollider extends CircleCollider {
	
	public MockedCircleCollider(float radius) {
		super(radius);
	}
	
	public MockedCircleCollider(float radius, Vector3f offset) {
		super(radius, offset);
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
