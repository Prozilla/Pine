package dev.prozilla.pine.common.property.adaptive;

import dev.prozilla.pine.common.property.IntProperty;

/**
 * An optimized adaptive property, that uses a primitive int value.
 */
public final class AdaptiveIntProperty extends AdaptiveProperty<Integer, IntProperty> implements IntProperty {
	
	private final int fixedValue;
	
	/**
	 * Creates a new property with a dynamic value.
	 * @param property Variable property that determines the value of this property
	 */
	public AdaptiveIntProperty(IntProperty property) {
		super(property);
		this.fixedValue = 0;
	}
	
	/**
	 * Creates a new property with a fixed value.
	 */
	public AdaptiveIntProperty(int fixedValue) {
		super(null);
		this.fixedValue = fixedValue;
	}
	
	@Override
	public int get() {
		return isDynamic() ? property.get() : fixedValue;
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
	public static AdaptiveIntProperty adapt(IntProperty property) {
		return new AdaptiveIntProperty(property);
	}
	
}
