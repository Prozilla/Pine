package dev.prozilla.pine.common.property.adaptive;

import dev.prozilla.pine.common.property.Property;
import dev.prozilla.pine.common.property.StringProperty;

public final class AdaptiveStringProperty extends AdaptiveObjectProperty<String> implements StringProperty {
	
	/**
	 * Creates a new property with a dynamic value.
	 * @param property Variable property that determines the value of this property
	 */
	public AdaptiveStringProperty(Property<String> property) {
		super(property);
	}
	
	/**
	 * Creates a new property with a fixed value.
	 */
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
	
	/**
	 * @see #adapt(StringProperty)
	 */
	public static AdaptiveStringProperty adapt(AdaptiveStringProperty property) {
		return property;
	}
	
	/**
	 * Converts any string property into an adaptive property.
	 * @param property The property to adapt
	 */
	public static AdaptiveStringProperty adapt(StringProperty property) {
		if (property instanceof AdaptiveStringProperty adaptiveProperty) {
			return adaptiveProperty;
		}
		return new AdaptiveStringProperty(property);
	}
	
}
