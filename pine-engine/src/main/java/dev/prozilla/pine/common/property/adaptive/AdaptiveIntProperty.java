package dev.prozilla.pine.common.property.adaptive;

import dev.prozilla.pine.common.property.VariableProperty;

/**
 * An optimized adaptive property, that uses a primitive int value.
 */
public final class AdaptiveIntProperty extends AdaptivePropertyBase<Integer> {
	
	private final int fixedPrimitiveValue;
	
	/**
	 * Creates a new property with a dynamic value.
	 * @param variableProperty Variable property that determines the value of this property
	 */
	public AdaptiveIntProperty(VariableProperty<Integer> variableProperty) {
		super(variableProperty);
		this.fixedPrimitiveValue = 0;
	}
	
	/**
	 * Creates a new property with a fixed value.
	 */
	public AdaptiveIntProperty(int fixedValue) {
		super(null);
		this.fixedPrimitiveValue = fixedValue;
	}
	
	@Override
	public Integer getValue() {
		return isDynamic() ? variableProperty.getValue() : fixedPrimitiveValue;
	}
	
	public int getPrimitiveValue() {
		return isDynamic() ? variableProperty.getValue() : fixedPrimitiveValue;
	}
	
	/**
	 * Converts an integer into an adaptive property.
	 * @param value The value of the property
	 */
	public static AdaptiveIntProperty adapt(int value) {
		return new AdaptiveIntProperty(value);
	}
	
	public static AdaptiveIntProperty adapt(AdaptiveIntProperty property) {
		return property;
	}
	
	/**
	 * Converts any integer property into an adaptive property.
	 * @param property The property to adapt
	 */
	public static AdaptiveIntProperty adapt(VariableProperty<Integer> property) {
		return new AdaptiveIntProperty(property);
	}
	
}
