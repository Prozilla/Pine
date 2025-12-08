package dev.prozilla.pine.core.state.config.option;

import dev.prozilla.pine.common.Transceivable;
import dev.prozilla.pine.common.property.observable.SimpleObservableProperty;
import dev.prozilla.pine.core.state.config.Config;

/**
 * Represents an option of the application's configuration.
 * @param <T> The type of the value
 * @see Config
 */
public interface ConfigOption<T> extends SimpleObservableProperty<T>, Transceivable<ConfigOption<T>> {
	
	String INITIAL_VALUE_ERROR = "initial value must be a valid value";

	/**
	 * Copies the value of this option to another option.
	 */
	@Override
	default void transmit(ConfigOption<T> target) {
		target.setValue(getValue());
	}
	
	/**
	 * Copies the value of another option to this option.
	 */
	@Override
	default void receive(ConfigOption<T> source) {
		Transceivable.super.receive(source);
	}
	
	/**
	 * Checks whether a value is a valid value for this option.
	 * @param value Value to check.
	 * @return True if the value is valid
	 */
	boolean isValidValue(T value);
	
	/**
	 * Resets this option to its initial value.
	 */
	void reset();
	
	@Override
	default ConfigOption<T> self() {
		return this;
	}
	
}
