package dev.prozilla.pine.common.property.observable;

/**
 * @see ObservableIntProperty
 */
@FunctionalInterface
public interface IntObserver extends Observer<Integer> {
	
	@Override
	default void observe(Integer value) {
		observe((int)value);
	}
	
	/**
	 * Observes a given value.
	 * @param value The value to observe
	 */
	void observe(int value);
	
}
