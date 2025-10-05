package dev.prozilla.pine.common;

import dev.prozilla.pine.common.property.fixed.FixedIntProperty;

public interface IntEnum {
	
	/**
	 * Returns the integer value of this instance.
	 * @return The integer value.
	 */
	int getValue();
	
	default FixedIntProperty toProperty() {
		return new FixedIntProperty(getValue());
	}
	
}
