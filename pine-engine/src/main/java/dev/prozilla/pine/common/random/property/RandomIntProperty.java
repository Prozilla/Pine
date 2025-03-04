package dev.prozilla.pine.common.random.property;

public class RandomIntProperty extends RandomProperty<Integer> {
	
	public RandomIntProperty(int min, int max) {
		super(min, max);
	}
	
	@Override
	public Integer getValue() {
		return random.nextInt(min, max);
	}
}
