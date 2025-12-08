package dev.prozilla.pine.core.state.config.option;

import dev.prozilla.pine.common.property.observable.SimpleObservableBooleanProperty;
import dev.prozilla.pine.common.util.ObjectUtils;
import dev.prozilla.pine.common.util.function.predicate.BooleanPredicate;

public class BooleanConfigOption extends SimpleObservableBooleanProperty implements ConfigOption<Boolean> {
	
	private final boolean initialValue;
	private final BooleanPredicate validator;
	
	/**
	 * Creates a config option without a validator.
	 * @param value Initial value
	 */
	public BooleanConfigOption(boolean value) {
		this(value, null);
	}
	
	/**
	 * Creates a config option with a validator.
	 * @param value Initial value
	 * @throws IllegalArgumentException If <code>validator</code> does not evaluate to <code>true</code> for the initial value.
	 */
	public BooleanConfigOption(boolean value, BooleanPredicate validator) throws IllegalArgumentException {
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
	public boolean setValue(Boolean value) {
		return set(ObjectUtils.unbox(value));
	}
	
	@Override
	public boolean set(boolean value) {
		if (!isValid(value)) {
			return false;
		}
		
		return super.set(value);
	}
	
	@Override
	public boolean isValidValue(Boolean value) {
		return isValid(ObjectUtils.unbox(value));
	}
	
	public boolean isValid(boolean value) {
		return validator == null || validator.test(value);
	}
	
	public void transmit(BooleanConfigOption otherOption) {
		otherOption.set(get());
	}
	
	public void receive(BooleanConfigOption otherOption) {
		otherOption.transmit(this);
	}
	
	@Override
	public void reset() {
		set(initialValue);
	}
	
}
