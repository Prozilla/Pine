package dev.prozilla.pine.common.property.random;

import dev.prozilla.pine.common.math.vector.Vector2i;
import dev.prozilla.pine.common.property.IntProperty;

/**
 * A property with a randomized integer value.
 */
public class RandomIntProperty extends RandomProperty<Integer> implements IntProperty {
	
	protected final int min, max;
	
	public RandomIntProperty(Vector2i bounds) {
		this(bounds.x, bounds.y);
	}
	
	public RandomIntProperty(int min, int max) {
		this.min = min;
		this.max = max;
	}
	
	@Override
	public int get() {
		return random.nextInt(min, max);
	}
	
}
