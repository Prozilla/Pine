package dev.prozilla.pine.core.state.config.option;

import dev.prozilla.pine.common.property.StringProperty;

import java.util.function.Predicate;

public class StringConfigOption extends ObjectConfigOption<String> implements StringProperty {
	
	/**
	 * Creates a config option without a validator.
	 * @param value Initial value
	 */
	public StringConfigOption(String value) {
		this(value, null);
	}
	
	/**
	 * Creates a config option with a validator.
	 * @param value Initial value
	 * @throws IllegalArgumentException If <code>validator</code> does not evaluate to <code>true</code> for the initial value.
	 */
	public StringConfigOption(String value, Predicate<String> validator) throws IllegalArgumentException {
		super(value, validator);
	}
	
}
