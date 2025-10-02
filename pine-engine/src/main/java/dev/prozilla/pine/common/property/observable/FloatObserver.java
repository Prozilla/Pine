package dev.prozilla.pine.common.property.observable;

/**
 * @see ObservableObjectProperty
 */
@FunctionalInterface
public interface FloatObserver extends Observer<Float> {
	
	@Override
	default void observe(Float value) {
		observe(value.floatValue());
	}
	
	/**
	 * Observes a given value.
	 * @param value The value to observe
	 */
	void observe(float value);
	
}
