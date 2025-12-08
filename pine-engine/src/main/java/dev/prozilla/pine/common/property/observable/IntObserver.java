package dev.prozilla.pine.common.property.observable;

import dev.prozilla.pine.common.util.ObjectUtils;

/**
 * @see ObservableIntProperty
 */
@FunctionalInterface
public interface IntObserver extends Observer<Integer> {
	
	@Override
	default void observe(Integer value) {
		observe(ObjectUtils.unbox(value));
	}
	
	/**
	 * Observes a given value.
	 * @param value The value to observe
	 */
	void observe(int value);
	
}
