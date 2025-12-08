package dev.prozilla.pine.core.state.config.option;

import dev.prozilla.pine.common.property.observable.SimpleObservableIntProperty;
import dev.prozilla.pine.common.util.ObjectUtils;

import java.util.function.IntPredicate;

public class IntConfigOption extends SimpleObservableIntProperty implements ConfigOption<Integer> {
	
	private final int initialValue;
	private final IntPredicate validator;
	
	/**
	 * Creates a config option without a validator.
	 * @param value Initial value
	 */
	public IntConfigOption(int value) {
		this(value, null);
	}
	
	/**
	 * Creates a config option with a validator.
	 * @param value Initial value
	 * @throws IllegalArgumentException If <code>validator</code> does not evaluate to <code>true</code> for the initial value.
	 */
	public IntConfigOption(int value, IntPredicate validator) throws IllegalArgumentException {
		super(value);
		this.initialValue = value;
		this.validator = validator;
		
		if (!isValid(this.initialValue)) {
			throw new IllegalArgumentException(INITIAL_VALUE_ERROR);
		}
	}
	
	/**
	 * Sets the value of this option.
	 * @param value New value for this option
	 */
	@Override
	public boolean setValue(Integer value) {
		return set(ObjectUtils.unbox(value));
	}
	
	@Override
	public boolean set(int value) {
		if (!isValid(value)) {
			return false;
		}
		
		return super.set(value);
	}
	
	@Override
	public boolean isValidValue(Integer value) {
		return isValid(ObjectUtils.unbox(value));
	}
	
	public boolean isValid(int value) {
		return validator == null || validator.test(value);
	}
	
	public void transmit(IntConfigOption otherOption) {
		otherOption.set(get());
	}
	
	public void receive(IntConfigOption otherOption) {
		otherOption.transmit(this);
	}
	
	@Override
	public void reset() {
		set(initialValue);
	}
	
}
