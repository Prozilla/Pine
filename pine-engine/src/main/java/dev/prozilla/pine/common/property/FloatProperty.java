package dev.prozilla.pine.common.property;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * A property with a float value.
 */
@FunctionalInterface
public interface FloatProperty extends NonNullProperty<Float> {
	
	@Override
	default Float getValueOr(Float defaultValue) {
		return getValue();
	}
	
	@Override
	@NotNull
	default Float requireValue() {
		return getValue();
	}
	
	@Override
	default Float getValue() {
		return get();
	}
	
	/**
	 * Returns the primitive value of this property.
	 * @return The primitive value of this property.
	 */
	float get();
	
	default boolean has(float value) {
		return get() == value;
	}
	
	@Contract("_ -> this")
	@Override
	default FloatProperty replaceNull(Float defaultValue) {
		return this;
	}
	
	/**
	 * Returns a boolean property whose value is {@code true} if the value of this property is {@code 0}.
	 * @return A boolean property based on whether the value of this property is {@code 0}.
	 * @see #hasProperty(float) 
	 */
	default BooleanProperty isZeroProperty() {
		return hasProperty(0);
	}
	
	/**
	 * Returns a boolean property whose value is {@code true} if the value of this property is equal to {@code value}.
	 * @return A boolean property whose value is {@code true} if the value of this property is equal to {@code value}.
	 * @see #has(float)
	 */
	default BooleanProperty hasProperty(float value) {
		return () -> has(value);
	}
	
	static FloatProperty fromProperty(FloatProperty property) {
		return property;
	}
	
	@Contract("_ -> new")
	static FloatProperty fromProperty(Property<Float> property) {
		return property::getValue;
	}
	
	/**
	 * Returns the value of a given property, or a default value if the property is {@code null}.
	 * @param property The property or {@code null}
	 * @param defaultValue The value to use in case the property is {@code null}.
	 * @return The value
	 */
	@Contract("null, _ -> param2")
	static float getValueOf(FloatProperty property, float defaultValue) {
		if (property == null) {
			return defaultValue;
		} else {
			return property.get();
		}
	}
	
}
