package dev.prozilla.pine.common.property.mutable;

import dev.prozilla.pine.common.property.BooleanProperty;

/**
 * A property with a boolean value that can be changed.
 */
public interface MutableBooleanProperty extends BooleanProperty, MutableProperty<Boolean> {
	
	@Override
	default boolean setValue(Boolean value) {
		return set(value);
	}
	
	/**
	 * Replaces the value with a new value and returns its previous value.
	 * @param value The new value
	 * @return The previous value.
	 */
	default boolean swap(boolean value) {
		boolean previousValue = get();
		set(value);
		return previousValue;
	}
	
	/**
	 * Negates the value of this property.
	 */
	default void toggle() {
		set(!get());
	}
	
	boolean set(boolean value);
	
	@Override
	default BooleanProperty viewProperty() {
		return this::get;
	}
	
}
