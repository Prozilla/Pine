package dev.prozilla.pine.common.property.adaptive;

import dev.prozilla.pine.common.property.Property;

/**
 * A generic optimized property that can either have a fixed or dynamic value.
 */
public abstract class AdaptiveProperty<T> extends AdaptivePropertyBase<T> {
	
	private final T fixedValue;
	
	/**
	 * Creates a new property with a dynamic value.
	 * @param property Variable property that determines the value of this property
	 */
	public AdaptiveProperty(Property<T> property) {
		super(property);
		this.fixedValue = null;
	}
	
	/**
	 * Creates a new property with a fixed value.
	 */
	public AdaptiveProperty(T fixedValue) {
		super(null);
		this.fixedValue = fixedValue;
	}
	
	@Override
	public T getValue() {
		return property != null ? property.getValue() : fixedValue;
	}
	
}
