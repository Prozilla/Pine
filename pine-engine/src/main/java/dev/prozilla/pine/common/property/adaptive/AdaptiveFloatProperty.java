package dev.prozilla.pine.common.property.adaptive;

import dev.prozilla.pine.common.property.FloatProperty;

/**
 * An optimized adaptive property, that uses a primitive float value.
 */
public final class AdaptiveFloatProperty extends AdaptiveProperty<Float, FloatProperty> implements FloatProperty {
	
	private final float fixedValue;
	
	/**
	 * Creates a new property with a dynamic value.
	 * @param property Variable property that determines the value of this property
	 */
	public AdaptiveFloatProperty(FloatProperty property) {
		super(property);
		this.fixedValue = 0f;
	}
	
	/**
	 * Creates a new property with a fixed value.
	 */
	public AdaptiveFloatProperty(float fixedValue) {
		super(null);
		this.fixedValue = fixedValue;
	}
	
	@Override
	public float get() {
		return isDynamic() ? property.get() : fixedValue;
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
	public static AdaptiveFloatProperty adapt(FloatProperty property) {
		return new AdaptiveFloatProperty(property);
	}
	
}
