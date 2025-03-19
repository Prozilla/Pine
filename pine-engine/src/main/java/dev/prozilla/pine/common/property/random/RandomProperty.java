package dev.prozilla.pine.common.property.random;

import dev.prozilla.pine.common.property.VariableProperty;

/**
 * Represents a property that is picked randomly between two bounds.
 */
public abstract class RandomProperty<T> extends VariableProperty<T> {
	
	protected final T min, max;
	
	public RandomProperty(T min, T max) {
		this.min = min;
		this.max = max;
	}
	
}
