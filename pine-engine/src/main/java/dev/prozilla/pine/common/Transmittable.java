package dev.prozilla.pine.common;

/**
 * Represents an object that can transmit its data.
 * @param <T> The type of the target object that can receive the transmission
 */
public interface Transmittable<T> {
	
	/**
	 * Transmits this object's data to a given target object.
	 * @param target The target object to transmit data to
	 */
	void transmit(T target);
	
	/**
	 * Transmits the data of a source to a given target object, if neither object is {@code null}.
	 * @param source The source to read data from
	 * @param target The target to transmit data to
	 * @param <T> The type of target object
	 */
	static <T> T transmitBetween(Transmittable<T> source, T target) {
		if (source != null && target != null) {
			source.transmit(target);
		}
		return target;
	}
	
}
