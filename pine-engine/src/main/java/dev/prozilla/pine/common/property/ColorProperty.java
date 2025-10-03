package dev.prozilla.pine.common.property;

import dev.prozilla.pine.common.Transmittable;
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
		Transmittable.transmit(getValue(), target);
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
	
	static ColorProperty fromProperty(ColorProperty property) {
		return property;
	}
	
	@Contract("_ -> new")
	static ColorProperty fromProperty(Property<Color> property) {
		return property::getValue;
	}
	
}
