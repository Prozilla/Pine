package dev.prozilla.pine.common.property.random;

public class RandomFloatProperty extends RandomProperty<Float> {
	
	public RandomFloatProperty(float min, float max) {
		super(min, max);
	}
	
	@Override
	public Float getValue() {
		return random.nextFloat(min, max);
	}
}
