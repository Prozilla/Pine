package dev.prozilla.pine.common.random.property;

public abstract class RandomProperty<T> extends VariableProperty<T> {
	
	protected final T min, max;
	
	public RandomProperty(T min, T max) {
		this.min = min;
		this.max = max;
	}
	
}
