package dev.prozilla.pine.common.property.observable;

import dev.prozilla.pine.common.util.ObjectUtils;

/**
 * @see ObservableFloatProperty
 */
@FunctionalInterface
public interface FloatObserver extends Observer<Float> {
	
	@Override
	default void observe(Float value) {
		observe(ObjectUtils.unbox(value));
	}
	
	/**
	 * Observes a given value.
	 * @param value The value to observe
	 */
	void observe(float value);
	
}
