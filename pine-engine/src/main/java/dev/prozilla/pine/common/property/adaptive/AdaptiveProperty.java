package dev.prozilla.pine.common.property.adaptive;

import dev.prozilla.pine.common.property.VariableProperty;

/**
 * A generic optimized property that can either have a fixed or dynamic value.
 */
public class AdaptiveProperty<T> extends AdaptivePropertyBase<T> {
	
	private final T fixedValue;
	
	/**
	 * Creates a new property with a dynamic value.
	 * @param variableProperty Variable property that determines the value of this property
	 */
	public AdaptiveProperty(VariableProperty<T> variableProperty) {
		super(variableProperty);
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
		return variableProperty != null ? variableProperty.getValue() : fixedValue;
	}
	
}
