package dev.prozilla.pine.common.property.observable;

/**
 * @see ObservableBooleanProperty
 */
@FunctionalInterface
public interface BooleanObserver extends Observer<Boolean> {
	
	@Override
	default void observe(Boolean value) {
		observe(value.booleanValue());
	}
	
	/**
	 * Observes a given value.
	 * @param value The value to observe
	 */
	void observe(boolean value);
	
}
