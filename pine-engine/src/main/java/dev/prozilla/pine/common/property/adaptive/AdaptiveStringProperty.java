package dev.prozilla.pine.common.property.adaptive;

import dev.prozilla.pine.common.property.VariableProperty;

public final class AdaptiveStringProperty extends AdaptiveProperty<String> {
	
	public AdaptiveStringProperty(VariableProperty<String> variableProperty) {
		super(variableProperty);
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
	public static AdaptiveStringProperty adapt(VariableProperty<String> property) {
		return new AdaptiveStringProperty(property);
	}
	
}
