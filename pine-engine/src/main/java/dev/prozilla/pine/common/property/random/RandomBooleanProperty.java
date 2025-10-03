package dev.prozilla.pine.common.property.random;

import dev.prozilla.pine.common.property.BooleanProperty;

/**
 * A property with a randomized boolean value.
 */
public class RandomBooleanProperty extends RandomProperty<Boolean> implements BooleanProperty {
	
	@Override
	public boolean get() {
		return random.nextBoolean();
	}
	
}