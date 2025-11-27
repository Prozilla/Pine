package dev.prozilla.pine.common.property;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * A property with an integer value.
 */
@FunctionalInterface
public interface IntProperty extends NonNullProperty<Integer> {
	
	@Override
	default Integer getValueOr(Integer defaultValue) {
		return getValue();
	}
	
	@Override
	@NotNull
	default Integer requireValue() {
		return getValue();
	}
	
	@Override
	default Integer getValue() {
		return get();
	}
	
	/**
	 * Returns the primitive value of this property.
	 * @return The primitive value of this property.
	 */
	int get();
	
	default boolean has(int value) {
		return get() == value;
	}
	
	@Contract("_ -> this")
	@Override
	default IntProperty replaceNull(Integer defaultValue) {
		return this;
	}
	
	/**
	 * Returns a boolean property whose value is {@code true} if the value of this property is {@code 0}.
	 * @return A boolean property based on whether the value of this property is {@code 0}.
	 */
	default BooleanProperty isZeroProperty() {
		return () -> get() == 0;
	}
	
	static IntProperty fromProperty(IntProperty property) {
		return property;
	}
	
	@Contract("_ -> new")
	static IntProperty fromProperty(Property<Integer> property) {
		return property::getValue;
	}
	
	/**
	 * Returns the value of a given property, or a default value if the property is {@code null}.
	 * @param property The property or {@code null}
	 * @param defaultValue The value to use in case the property is {@code null}.
	 * @return The value
	 */
	@Contract("null, _ -> param2")
	static int getValueOf(IntProperty property, int defaultValue) {
		if (property == null) {
			return defaultValue;
		} else {
			return property.get();
		}
	}
	
}
