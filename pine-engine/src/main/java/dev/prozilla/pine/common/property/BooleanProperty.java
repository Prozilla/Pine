package dev.prozilla.pine.common.property;

import org.jetbrains.annotations.Contract;

@FunctionalInterface
public interface BooleanProperty extends Property<Boolean> {
	
	@Override
	default Boolean getValue() {
		return get();
	}
	
	boolean get();
	
	default boolean has(boolean value) {
		return get() == value;
	}
	
	@Contract("_ -> new")
	static BooleanProperty fromProperty(Property<Boolean> property) {
		return property::getValue;
	}
	
}