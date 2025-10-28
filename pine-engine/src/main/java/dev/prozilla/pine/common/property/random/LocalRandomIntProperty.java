package dev.prozilla.pine.common.property.random;

import dev.prozilla.pine.common.math.vector.Vector2i;

import java.util.Random;

public class LocalRandomIntProperty extends RandomIntProperty {
	
	protected Random localRandom;
	
	public LocalRandomIntProperty(Vector2i bounds) {
		this(bounds.x, bounds.y);
	}
	
	public LocalRandomIntProperty(int min, int max) {
		super(min, max);
		localRandom = new Random();
	}
	
	@Override
	protected Random getRandom() {
		return localRandom;
	}
	
}
