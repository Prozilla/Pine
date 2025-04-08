package dev.prozilla.pine.common.property.adaptive;

import dev.prozilla.pine.common.property.VariableProperty;

/**
 * An optimized adaptive property, that uses a primitive float value.
 */
public final class AdaptiveFloatProperty extends AdaptivePropertyBase<Float> {
	
	private final float fixedPrimitiveValue;
	
	/**
	 * Creates a new property with a dynamic value.
	 * @param variableProperty Variable property that determines the value of this property
	 */
	public AdaptiveFloatProperty(VariableProperty<Float> variableProperty) {
		super(variableProperty);
		this.fixedPrimitiveValue = 0f;
	}
	
	/**
	 * Creates a new property with a fixed value.
	 */
	public AdaptiveFloatProperty(float fixedValue) {
		super(null);
		this.fixedPrimitiveValue = fixedValue;
	}
	
	@Override
	public Float getValue() {
		return isDynamic() ? variableProperty.getValue() : fixedPrimitiveValue;
	}
	
	public float getPrimitiveValue() {
		return isDynamic() ? variableProperty.getValue() : fixedPrimitiveValue;
	}
	
	/**
	 * Converts a float into an adaptive property.
	 * @param value The value of the property
	 */
	public static AdaptiveFloatProperty adapt(float value) {
		return new AdaptiveFloatProperty(value);
	}
	
	public static AdaptiveFloatProperty adapt(AdaptiveFloatProperty property) {
		return property;
	}
	
	/**
	 * Converts any float property into an adaptive property.
	 * @param property The property to adapt
	 */
	public static AdaptiveFloatProperty adapt(VariableProperty<Float> property) {
		return new AdaptiveFloatProperty(property);
	}
	
}
