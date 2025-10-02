package dev.prozilla.pine.common.property.adaptive;

import dev.prozilla.pine.common.property.Property;

public final class AdaptiveStringProperty extends AdaptiveProperty<String> {
	
	public AdaptiveStringProperty(Property<String> property) {
		super(property);
	}
	
	public AdaptiveStringProperty(String fixedValue) {
		super(fixedValue);
	}
	
	/**
	 * Converts a string into an adaptive property.
	 * @param value The value of the property
	 */
	public static AdaptiveStringProperty adapt(String value) {
		return new AdaptiveStringProperty(value);
	}
	
	public static AdaptiveStringProperty adapt(AdaptiveStringProperty property) {
		return property;
	}
	
	/**
	 * Converts any string property into an adaptive property.
	 * @param property The property to adapt
	 */
	public static AdaptiveStringProperty adapt(Property<String> property) {
		return new AdaptiveStringProperty(property);
	}
	
}
