package dev.prozilla.pine.common.property;

import dev.prozilla.pine.common.Transmittable;
import dev.prozilla.pine.common.system.Color;
import org.jetbrains.annotations.Contract;

/**
 * Utility methods for color properties.
 */
public interface ColorProperty extends Transmittable<Color> {
	
	/**
	 * @return A new color with the value of this property.
	 */
	@Contract("-> new")
	default Color getColor() {
		Color output = new Color();
		transmit(output);
		return output;
	}
	
}
