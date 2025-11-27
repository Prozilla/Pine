package dev.prozilla.pine.common.util.function;

/**
 * An executable function with no arguments and no return value.
 */
@FunctionalInterface
public interface Callback {
	
	/**
	 * Executes the callback function.
	 */
	void run();
	
}
