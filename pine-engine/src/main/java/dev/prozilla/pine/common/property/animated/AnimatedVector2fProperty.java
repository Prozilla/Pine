package dev.prozilla.pine.common.property.animated;

import dev.prozilla.pine.common.math.vector.Vector2f;

public class AnimatedVector2fProperty extends AnimatedObjectProperty<Vector2f> {
	
	protected final Vector2f result;
	
	public AnimatedVector2fProperty(Vector2f start, Vector2f end, AnimationCurve curve) {
		super(start, end, curve);
		result = start;
	}
	
	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		result.set(end.x, end.y)
			.subtract(start)
			.scale(getProgress())
			.add(start);
	}
	
	@Override
	public Vector2f getValue() {
		return result;
	}
}
