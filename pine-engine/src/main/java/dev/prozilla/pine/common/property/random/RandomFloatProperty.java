package dev.prozilla.pine.common.property.random;

import dev.prozilla.pine.common.math.vector.Vector2f;
import dev.prozilla.pine.common.property.FloatProperty;

/**
 * A property with a randomized integer value.
 */
public class RandomFloatProperty extends RandomProperty<Float> implements FloatProperty {
	
	protected final float min, max;
	
	public RandomFloatProperty(Vector2f bounds) {
		this(bounds.x, bounds.y);
	}
	
	public RandomFloatProperty(float min, float max) {
		this.min = min;
		this.max = max;
	}
	
	@Override
	public float get() {
		return getRandom().nextFloat(min, max);
	}
	
}