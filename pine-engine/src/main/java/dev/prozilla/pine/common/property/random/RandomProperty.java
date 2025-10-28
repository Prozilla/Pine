package dev.prozilla.pine.common.property.random;

import dev.prozilla.pine.common.property.Property;

import java.util.Random;

/**
 * A property whose value is randomized.
 */
public abstract class RandomProperty<T> implements Property<T> {
	
	/** The random number generator used to randomize the values of all random properties. */
	protected static final Random globalRandom = new Random();
	
	/**
	 * Sets the seed for the random number generator used for all random properties.
	 * @param seed The seed to use
	 */
	public static void setGlobalSeed(long seed) {
		globalRandom.setSeed(seed);
	}
	
	/**
	 * Sets the seed for the random number generator of this property.
	 * @param seed The seed to use
	 */
	public void setSeed(long seed) {
		getRandom().setSeed(seed);
	}
	
	/**
	 * Returns the random number generator of this property.
	 * @return The random number generator of this property.
	 */
	protected Random getRandom() {
		return globalRandom;
	}
	
}
