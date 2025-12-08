package dev.prozilla.pine.core.state.config.option;

import dev.prozilla.pine.common.property.observable.SimpleObservableFloatProperty;
import dev.prozilla.pine.common.util.ObjectUtils;
import dev.prozilla.pine.common.util.function.predicate.FloatPredicate;

public class FloatConfigOption extends SimpleObservableFloatProperty implements ConfigOption<Float> {
	
	private final float initialValue;
	private final FloatPredicate validator;
	
	/**
	 * Creates a config option without a validator.
	 * @param value Initial value
	 */
	public FloatConfigOption(float value) {
		this(value, null);
	}
	
	/**
	 * Creates a config option with a validator.
	 * @param value Initial value
	 * @throws IllegalArgumentException If <code>validator</code> does not evaluate to <code>true</code> for the initial value.
	 */
	public FloatConfigOption(float value, FloatPredicate validator) throws IllegalArgumentException {
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
	public boolean setValue(Float value) {
		return set(ObjectUtils.unbox(value));
	}
	
	@Override
	public boolean set(float value) {
		if (!isValid(value)) {
			return false;
		}
		
		return super.set(value);
	}
	
	@Override
	public boolean isValidValue(Float value) {
		return isValid(ObjectUtils.unbox(value));
	}
	
	public boolean isValid(float value) {
		return validator == null || validator.test(value);
	}
	
	public void transmit(FloatConfigOption otherOption) {
		otherOption.set(get());
	}
	
	public void receive(FloatConfigOption otherOption) {
		otherOption.transmit(this);
	}
	
	@Override
	public void reset() {
		set(initialValue);
	}
	
}
