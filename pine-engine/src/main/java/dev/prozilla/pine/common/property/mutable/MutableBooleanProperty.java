package dev.prozilla.pine.common.property.mutable;

import dev.prozilla.pine.common.property.BooleanProperty;
import dev.prozilla.pine.common.util.ObjectUtils;
import dev.prozilla.pine.common.util.function.mapper.BooleanMapper;

/**
 * A property with a boolean value that can be changed.
 */
public interface MutableBooleanProperty extends BooleanProperty, MutableProperty<Boolean> {
	
	@Override
	default boolean setValue(Boolean value) {
		return set(ObjectUtils.unbox(value));
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
	 * Applies a mapper to the value of this property.
	 * @param mapper The mapper to apply
	 * @return {@code true} if the value was changed.
	 */
	default boolean modify(BooleanMapper mapper) {
		return set(mapper.map(get()));
	}
	
	/**
	 * Negates the value of this property.
	 */
	default void toggle() {
		set(!get());
	}
	
	/**
	 * Sets the value of this property.
	 * @param value The new value
	 * @return {@code true} if the value was changed.
	 */
	boolean set(boolean value);
	
	@Override
	default BooleanProperty viewProperty() {
		return this::get;
	}
	
}
