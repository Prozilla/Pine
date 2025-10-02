package dev.prozilla.pine.common.property;

import org.jetbrains.annotations.Contract;

@FunctionalInterface
public interface IntProperty extends Property<Integer> {
	
	@Override
	default Integer getValue() {
		return get();
	}
	
	int get();
	
	default boolean has(int value) {
		return get() == value;
	}
	
	@Contract("_ -> new")
	static IntProperty fromProperty(Property<Integer> property) {
		return property::getValue;
	}
	
}
