package dev.prozilla.pine.common.property.adaptive;

import dev.prozilla.pine.common.property.ColorProperty;
import dev.prozilla.pine.common.property.Property;
import dev.prozilla.pine.common.system.Color;

public final class AdaptiveColorProperty extends AdaptiveProperty<Color> implements ColorProperty {
	
	public AdaptiveColorProperty(Property<Color> property) {
		super(property);
	}
	
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
	
	public static AdaptiveColorProperty adapt(AdaptiveColorProperty property) {
		return property;
	}
	
	/**
	 * Converts any color property into an adaptive property.
	 * @param property The property to adapt
	 */
	public static AdaptiveColorProperty adapt(Property<Color> property) {
		return new AdaptiveColorProperty(property);
	}
	
}
