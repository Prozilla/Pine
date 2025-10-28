package dev.prozilla.pine.common.property.random;

import dev.prozilla.pine.common.math.vector.Vector2f;

import java.util.Random;

public class LocalRandomFloatProperty extends RandomFloatProperty {
	
	protected Random localRandom;
	
	public LocalRandomFloatProperty(Vector2f bounds) {
		this(bounds.x, bounds.y);
	}
	
	public LocalRandomFloatProperty(float min, float max) {
		super(min, max);
		localRandom = new Random();
	}
	
	@Override
	protected Random getRandom() {
		return localRandom;
	}
	
}
