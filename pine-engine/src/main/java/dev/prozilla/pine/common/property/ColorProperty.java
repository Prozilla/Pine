package dev.prozilla.pine.common.property;

import dev.prozilla.pine.common.Transmittable;
import dev.prozilla.pine.common.system.resource.Color;

/**
 * Utility methods for color properties.
 */
public interface ColorProperty extends Transmittable<Color> {
	
	/**
	 * @return A new color with the value of this property.
	 */
	default Color getColor() {
		Color output = new Color();
		transmit(output);
		return output;
	}
	
}
