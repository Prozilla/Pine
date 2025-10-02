package dev.prozilla.pine.common.property;

import org.jetbrains.annotations.Contract;

@FunctionalInterface
public interface FloatProperty extends Property<Float> {
	
	@Override
	default Float getValue() {
		return get();
	}
	
	float get();
	
	default boolean has(float value) {
		return get() == value;
	}
	
	@Contract("_ -> new")
	static FloatProperty fromProperty(Property<Float> property) {
		return property::getValue;
	}
	
}
