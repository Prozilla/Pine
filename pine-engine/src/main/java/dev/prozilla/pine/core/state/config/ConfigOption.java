package dev.prozilla.pine.core.state.config;

import dev.prozilla.pine.common.property.ObservableProperty;

import java.util.function.Predicate;

/**
 * Represents an option of the application's configuration.
 * @param <T> Type of the value of the option
 * @see Config
 */
public class ConfigOption<T> extends ObservableProperty<T> {
	
	private final T initialValue;
	private final Predicate<T> validator;
	
	/**
	 * Creates a config option without a validator.
	 * @param value Initial value
	 */
	public ConfigOption(T value) {
		this(value, null);
	}
	
	/**
	 * Creates a config option with a validator.
	 * @param value Initial value
	 * @throws IllegalArgumentException If <code>validator</code> does not evaluate to <code>true</code> for the initial value.
	 */
	public ConfigOption(T value, Predicate<T> validator) throws IllegalArgumentException {
		super(value);
		this.initialValue = value;
		this.validator = validator;
		
		if (!isValidValue(this.initialValue)) {
			throw new IllegalArgumentException("validator must evaluate to true for initial value");
		}
	}
	
	/**
	 * Sets the value of this option.
	 * @param value New value for this option
	 */
	@Override
	public void setValue(T value) {
		if (!isValidValue(value)) {
			throw new IllegalArgumentException("invalid value for option");
		}
		
		super.setValue(value);
	}
	
	/**
	 * Copies the value of this option to another option.
	 */
	public void copyTo(ConfigOption<T> otherOption) {
		otherOption.setValue(getValue());
	}
	
	/**
	 * Copies the value of another option to this option.
	 */
	public void copyFrom(ConfigOption<T> otherOption) {
		otherOption.copyTo(this);
	}
	
	/**
	 * Checks whether a value is a valid value for this option.
	 * @param value Value to check.
	 * @return True if the value is valid
	 */
	public boolean isValidValue(T value) {
		return validator == null || validator.test(value);
	}
	
	/**
	 * Resets this option to its initial value.
	 */
	public void reset() {
		setValue(initialValue);
	}
	
}
