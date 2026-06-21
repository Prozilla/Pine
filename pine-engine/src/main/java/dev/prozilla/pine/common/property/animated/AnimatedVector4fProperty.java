package dev.prozilla.pine.common.property.animated;

import dev.prozilla.pine.common.math.vector.Vector4f;

public class AnimatedVector4fProperty extends AnimatedObjectProperty<Vector4f> {
	
	protected final Vector4f result;
	
	public AnimatedVector4fProperty(Vector4f start, Vector4f end, AnimationCurve curve) {
		super(start, end, curve);
		result = start;
	}
	
	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		result.set(end.x, end.y, end.z, end.w)
			.subtract(start)
			.scale(getProgress())
			.add(start);
	}
	
	@Override
	public Vector4f getValue() {
		return result;
	}
}
