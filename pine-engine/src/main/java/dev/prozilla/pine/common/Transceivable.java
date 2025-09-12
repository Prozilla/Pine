package dev.prozilla.pine.common;

import dev.prozilla.pine.common.util.checks.Checks;

/**
 * Represents an object that can both transmit and receive data.
 * @param <T> The type of the transceivable object
 */
public interface Transceivable<T extends Transceivable<T>> extends Transmittable<T> {
	
	/**
	 * Transmits data from the <code>source</code> object to this object.
	 * @param source The object from which data will be received
	 */
	default void receive(T source) {
		Checks.isNotNull(source, "source");
		source.transmit(self());
	}
	
	T self();
	
}
