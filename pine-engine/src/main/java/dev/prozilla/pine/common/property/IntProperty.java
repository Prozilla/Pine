package dev.prozilla.pine.common.property;

import org.jetbrains.annotations.Contract;

@FunctionalInterface
public interface IntProperty extends Property<Integer> {
	
	@Override
	default Integer getValue() {
		return get();
	}
	
	@Override
	default Integer getValueOr(Integer defaultValue) {
		return get();
	}
	
	int get();
	
	@Contract("-> true")
	@Override
	default boolean exists() {
		return true;
	}
	
	default boolean has(int value) {
		return get() == value;
	}
	
	/**
	 * Returns this property.
	 * @return This property.
	 */
	@Contract("_ -> this")
	@Override
	default IntProperty replaceNull(Integer defaultValue) {
		return this;
	}
	
	static IntProperty fromProperty(IntProperty property) {
		return property;
	}
	
	@Contract("_ -> new")
	static IntProperty fromProperty(Property<Integer> property) {
		return property::getValue;
	}
	
}
