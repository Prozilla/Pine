package dev.prozilla.pine.common.property;

import org.jetbrains.annotations.Contract;

@FunctionalInterface
public interface FloatProperty extends Property<Float> {
	
	@Override
	default Float getValue() {
		return get();
	}
	
	@Override
	default Float getValueOr(Float defaultValue) {
		return get();
	}
	
	float get();
	
	@Contract("-> true")
	@Override
	default boolean exists() {
		return true;
	}
	
	default boolean has(float value) {
		return get() == value;
	}
	
	/**
	 * Returns this property.
	 * @return This property.
	 */
	@Contract("_ -> this")
	@Override
	default FloatProperty replaceNull(Float defaultValue) {
		return this;
	}
	
	static FloatProperty fromProperty(FloatProperty property) {
		return property;
	}
	
	@Contract("_ -> new")
	static FloatProperty fromProperty(Property<Float> property) {
		return property::getValue;
	}
	
}
