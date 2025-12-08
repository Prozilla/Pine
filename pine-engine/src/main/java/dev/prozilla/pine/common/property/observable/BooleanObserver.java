package dev.prozilla.pine.common.property.observable;

import dev.prozilla.pine.common.util.ObjectUtils;

/**
 * @see ObservableBooleanProperty
 */
@FunctionalInterface
public interface BooleanObserver extends Observer<Boolean> {
	
	@Override
	default void observe(Boolean value) {
		observe(ObjectUtils.unbox(value));
	}
	
	/**
	 * Observes a given value.
	 * @param value The value to observe
	 */
	void observe(boolean value);
	
}
