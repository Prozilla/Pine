package dev.prozilla.pine.common.property.random;

import java.util.Random;

public abstract class LocalRandomObjectProperty<T> extends RandomObjectProperty<T> {
	
	protected Random localRandom;
	
	public LocalRandomObjectProperty(T min, T max) {
		super(min, max);
		localRandom = new Random();
	}
	
	@Override
	protected Random getRandom() {
		return localRandom;
	}
	
}
