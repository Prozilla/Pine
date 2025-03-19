package dev.prozilla.pine.common.property.random;

import dev.prozilla.pine.common.property.VariableProperty;

public class RandomIntProperty extends RandomProperty<Integer> {
	
	public RandomIntProperty(int min, int max) {
		super(min, max);
	}
	
	@Override
	public Integer getValue() {
		return VariableProperty.random.nextInt(min, max);
	}
}
