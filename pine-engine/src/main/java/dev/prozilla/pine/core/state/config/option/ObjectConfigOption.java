package dev.prozilla.pine.core.state.config.option;

import dev.prozilla.pine.common.property.observable.SimpleObservableObjectProperty;

import java.util.function.Predicate;

public class ObjectConfigOption<T> extends SimpleObservableObjectProperty<T> implements ConfigOption<T> {
	
	private final T initialValue;
	private final Predicate<T> validator;
	
	/**
	 * Creates a config option without a validator.
	 * @param value Initial value
	 */
	public ObjectConfigOption(T value) {
		this(value, null);
	}
	
	/**
	 * Creates a config option with a validator.
	 * @param value Initial value
	 * @throws IllegalArgumentException If <code>validator</code> does not evaluate to <code>true</code> for the initial value.
	 */
	public ObjectConfigOption(T value, Predicate<T> validator) throws IllegalArgumentException {
		super(value);
		this.initialValue = value;
		this.validator = validator;
		
		if (!isValidValue(this.initialValue)) {
			throw new IllegalArgumentException(INITIAL_VALUE_ERROR);
		}
	}
	
	@Override
	public boolean setValue(T value) {
		if (!isValidValue(value)) {
			return false;
		}
		
		return super.setValue(value);
	}
	
	@Override
	public boolean isValidValue(T value) {
		return validator == null || validator.test(value);
	}
	
	@Override
	public void reset() {
		setValue(initialValue);
	}
	
}
