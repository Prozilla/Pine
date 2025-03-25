package dev.prozilla.pine.common.property;

import java.util.Random;

/**
 * A property with a value that may change in certain circumstances.
 * @param <T> The type of property
 */
public abstract class VariableProperty<T> {
	
	protected static final Random random = new Random();
	
	/**
	 * @return The value of this property.
	 */
	public abstract T getValue();

}
