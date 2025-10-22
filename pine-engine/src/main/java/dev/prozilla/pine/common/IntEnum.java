package dev.prozilla.pine.common;

import dev.prozilla.pine.common.property.fixed.FixedIntProperty;

/**
 * An enum that is represented by integers.
 *
 * <p>Often used by enums that wrap around GLFW constants.</p>
 */
public interface IntEnum {
	
	/**
	 * Returns the integer value of this instance.
	 * @return The integer value.
	 */
	int getValue();
	
	/**
	 * Returns an int property whose value is the integer value of this instance.
	 * @return An int property with the value of this instance.
	 */
	default FixedIntProperty toProperty() {
		return new FixedIntProperty(getValue());
	}
	
}
