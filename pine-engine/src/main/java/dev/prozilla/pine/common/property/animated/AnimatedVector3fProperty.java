package dev.prozilla.pine.common.property.animated;

import dev.prozilla.pine.common.math.vector.Vector3f;

public class AnimatedVector3fProperty extends AnimatedObjectProperty<Vector3f> {
	
	protected final Vector3f result;
	
	public AnimatedVector3fProperty(Vector3f start, Vector3f end, AnimationCurve curve) {
		super(start, end, curve);
		result = start;
	}
	
	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		result.set(end.x, end.y, end.z)
			.subtract(start)
			.scale(getProgress())
			.add(start);
	}
	
	@Override
	public Vector3f getValue() {
		return result;
	}
}
