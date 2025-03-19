package dev.prozilla.pine.common.property.random;

import dev.prozilla.pine.common.property.VariableProperty;

public class RandomFloatProperty extends RandomProperty<Float> {
	
	public RandomFloatProperty(float min, float max) {
		super(min, max);
	}
	
	@Override
	public Float getValue() {
		return VariableProperty.random.nextFloat(min, max);
	}
}
