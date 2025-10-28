package dev.prozilla.pine.common.property.random;

import dev.prozilla.pine.common.property.Property;

import java.util.Random;

/**
 * A property whose value is randomized.
 */
public abstract class RandomProperty<T> implements Property<T> {
	
	/** The random number generator used to randomize the values of this property. */
	protected static final Random random = new Random();
	
	/**
	 * Sets the seed for the random number generator used for all random properties.
	 * @param seed The seed to use
	 */
	public static void setSeed(long seed) {
		random.setSeed(seed);
	}
	
}
