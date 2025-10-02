package dev.prozilla.pine.common.property.random;

import dev.prozilla.pine.common.property.Property;

import java.util.Random;

/**
 * Represents a property that is picked randomly between two bounds.
 */
public abstract class RandomProperty<T> implements Property<T> {
	
	protected static final Random random = new Random();
	
	protected final T min, max;
	
	public RandomProperty(T min, T max) {
		this.min = min;
		this.max = max;
	}
	
}
