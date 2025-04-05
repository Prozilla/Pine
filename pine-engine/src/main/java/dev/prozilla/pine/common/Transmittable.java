package dev.prozilla.pine.common;

/**
 * Represents an object that can transmit its data.
 * @param <T> The type of the target object that can receive the transmission
 */
public interface Transmittable<T> {
	
	/**
	 * Transmits this object's data to a given target object.
	 * @param target the target object to which data will be transmitted
	 */
	void transmit(T target);

}
