package dev.prozilla.pine.common.property.observable;

/**
 * @see ObservableObjectProperty
 * @param <T> The type of value to observe
 */
@FunctionalInterface
public interface Observer<T> {
	
	/**
	 * Observes a given value.
	 * @param value The value to observe
	 */
	void observe(T value);
	
}
