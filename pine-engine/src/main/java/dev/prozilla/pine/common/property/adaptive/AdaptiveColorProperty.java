package dev.prozilla.pine.common.property.adaptive;

import dev.prozilla.pine.common.property.ColorProperty;
import dev.prozilla.pine.common.property.Property;
import dev.prozilla.pine.common.system.Color;

public final class AdaptiveColorProperty extends AdaptiveObjectProperty<Color> implements ColorProperty {
	
	/**
	 * Creates a new property with a dynamic value.
	 * @param property Variable property that determines the value of this property
	 */
	public AdaptiveColorProperty(Property<Color> property) {
		super(property);
	}
	
	/**
	 * Creates a new property with a fixed value.
	 */
	public AdaptiveColorProperty(Color fixedValue) {
		super(fixedValue);
	}
	
	// TO DO: Use AnimatedColorProperty instead and apply directly
	@Override
	public void transmit(Color target) {
		getValue().transmit(target);
	}
	
	/**
	 * Converts a color into an adaptive property.
	 * @param value The value of the property
	 */
	public static AdaptiveColorProperty adapt(Color value) {
		return new AdaptiveColorProperty(value);
	}
	
	/**
	 * @see #adapt(ColorProperty)
	 */
	public static AdaptiveColorProperty adapt(AdaptiveColorProperty property) {
		return property;
	}
	
	/**
	 * Converts any color property into an adaptive property.
	 * @param property The property to adapt
	 */
	public static AdaptiveColorProperty adapt(ColorProperty property) {
		return new AdaptiveColorProperty(property);
	}
	
}
