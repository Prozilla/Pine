package dev.prozilla.pine.common.random.property;

public class RandomFloatProperty extends RandomProperty<Float> {
	
	public RandomFloatProperty(float min, float max) {
		super(min, max);
	}
	
	@Override
	public Float getValue() {
		return random.nextFloat(min, max);
	}
}
