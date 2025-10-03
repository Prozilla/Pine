package dev.prozilla.pine.common.property.random;

/**
 * A randomized property that supports null values.
 */
public abstract class RandomObjectProperty<T> extends RandomProperty<T> {
	
	protected final T min, max;
	
	public RandomObjectProperty(T min, T max) {
		this.min = min;
		this.max = max;
	}
	
}
