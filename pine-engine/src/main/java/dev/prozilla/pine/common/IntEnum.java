package dev.prozilla.pine.common;

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
	
}
