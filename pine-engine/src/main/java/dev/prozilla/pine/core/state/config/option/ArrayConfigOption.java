package dev.prozilla.pine.core.state.config.option;

import dev.prozilla.pine.common.property.bindable.BindableArrayProperty;
import dev.prozilla.pine.common.property.bindable.SimpleBindableArrayProperty;

import java.util.function.Predicate;

public class ArrayConfigOption<E> extends ObjectConfigOption<E[]> implements BindableArrayProperty<E> {
	
	/**
	 * Creates a config option without a validator.
	 * @param value Initial value
	 */
	@SafeVarargs
	public ArrayConfigOption(E... value) {
		super(value);
	}
	
	/**
	 * Creates a config option with a validator.
	 * @param value Initial value
	 * @throws IllegalArgumentException If <code>validator</code> does not evaluate to <code>true</code> for the initial value.
	 */
	public ArrayConfigOption(E[] value, Predicate<E[]> validator) throws IllegalArgumentException {
		super(value, validator);
	}
	
	@Override
	public boolean set(int index, E value) {
		return SimpleBindableArrayProperty.setItem(index, value, this, this::onValueChange);
	}
	
}
