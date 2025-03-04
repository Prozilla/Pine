package dev.prozilla.pine.common.random.property;

import java.util.Random;

public abstract class VariableProperty<T> {
	
	protected static final Random random = new Random();

	public abstract T getValue();

}
