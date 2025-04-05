package dev.prozilla.pine.common.property;

import dev.prozilla.pine.common.system.resource.Color;

/**
 * Utility methods for color properties.
 */
public interface ColorProperty {
	
	/**
	 * @return A new color with the value of this property.
	 */
	default Color getColor() {
		Color output = new Color();
		apply(output);
		return output;
	}
	
	/**
	 * Applies the value of this property to a color without creating a new instance.
	 * @param target The color to override with the value of this property.
	 */
	void apply(Color target);
	
}
