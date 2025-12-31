package dev.prozilla.pine.common.property;

import dev.prozilla.pine.common.Transmittable;
import dev.prozilla.pine.common.property.fixed.FixedColorProperty;
import dev.prozilla.pine.common.system.Color;
import dev.prozilla.pine.common.util.checks.Checks;
import org.jetbrains.annotations.Contract;

import java.util.Objects;

/**
 * A property with a color value.
 */
@FunctionalInterface
public interface ColorProperty extends Property<Color>, Transmittable<Color> {
	
	@Override
	default void transmit(Color target) {
		Transmittable.transmitBetween(getValue(), target);
	}
	
	/**
	 * @return A new color with the value of this property.
	 */
	@Contract("-> new")
	default Color getColor() {
		Color output = new Color();
		transmit(output);
		return output;
	}
	
	@Override
	default ColorProperty replaceNull(Color defaultValue) {
		Checks.isNotNull(defaultValue, "defaultValue");
		return () -> Objects.requireNonNullElse(getValue(), defaultValue);
	}
	
	@Override
	default FixedColorProperty snapshot() {
		return new FixedColorProperty(getValue());
	}
	
	/**
	 * @see #fromProperty(Property)
	 */
	static ColorProperty fromProperty(ColorProperty property) {
		return property;
	}
	
	/**
	 * Converts a property to a color property.
	 * @param property The property to convert
	 * @return The converted color property.
	 */
	@Contract("_ -> new")
	static ColorProperty fromProperty(Property<Color> property) {
		return property::getValue;
	}
	
}
