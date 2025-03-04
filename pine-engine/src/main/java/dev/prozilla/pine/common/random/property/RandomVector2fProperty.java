package dev.prozilla.pine.common.random.property;

import dev.prozilla.pine.common.math.vector.Vector2f;

public class RandomVector2fProperty extends RandomProperty<Vector2f> {
	
	public RandomVector2fProperty(float minX, float maxX, float minY, float maxY) {
		this(new Vector2f(minX, minY), new Vector2f(maxX, maxY));
	}
	
	public RandomVector2fProperty(Vector2f min, Vector2f max) {
		super(min, max);
	}
	
	@Override
	public Vector2f getValue() {
		float x = min.x + random.nextFloat() * (max.x - min.x);
		float y = min.y + random.nextFloat() * (max.y - min.y);
		return new Vector2f(x, y);
	}
}
