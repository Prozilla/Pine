package dev.prozilla.pine.common.property.random;

import java.util.Random;

public class LocalRandomBooleanProperty extends RandomBooleanProperty {
	
	protected Random localRandom;
	
	public LocalRandomBooleanProperty() {
		localRandom = new Random();
	}
	
	@Override
	protected Random getRandom() {
		return localRandom;
	}
	
}
