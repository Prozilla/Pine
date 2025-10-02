package dev.prozilla.pine.common.property.adaptive;

import dev.prozilla.pine.common.property.Property;

public final class AdaptiveObjectProperty extends AdaptiveProperty<Object> {
	
	public AdaptiveObjectProperty(Property<Object> property) {
		super(property);
	}
	
	public AdaptiveObjectProperty(Object fixedValue) {
		super(fixedValue);
	}
	
	/**
	 * Converts an object into an adaptive property.
	 * @param value The value of the property
	 */
	public static AdaptiveObjectProperty adapt(Object value) {
		return new AdaptiveObjectProperty(value);
	}
	
	public static AdaptiveObjectProperty adapt(AdaptiveObjectProperty property) {
		return property;
	}
	
	/**
	 * Converts any object property into an adaptive property.
	 * @param property The property to adapt
	 */
	public static AdaptiveObjectProperty adapt(Property<Object> property) {
		return new AdaptiveObjectProperty(property);
	}
	
}
