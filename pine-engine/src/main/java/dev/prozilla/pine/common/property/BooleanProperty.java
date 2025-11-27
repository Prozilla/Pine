package dev.prozilla.pine.common.property;

import dev.prozilla.pine.common.property.fixed.FixedBooleanProperty;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * A property with a boolean value.
 */
@FunctionalInterface
public interface BooleanProperty extends NonNullProperty<Boolean> {
	
	/** A boolean property whose value is always {@code true}. */
	FixedBooleanProperty TRUE = new FixedBooleanProperty() {
		@Override
		public boolean get() {
			return true;
		}
		
		@Override
		public FixedBooleanProperty not() {
			return FALSE;
		}
		
		@Override
		public FixedBooleanProperty isNotNullProperty() {
			return this;
		}
	};
	/** A boolean property whose value is always {@code false}. */
	FixedBooleanProperty FALSE = new FixedBooleanProperty() {
		@Override
		public boolean get() {
			return false;
		}
		
		@Override
		public FixedBooleanProperty not() {
			return TRUE;
		}
		
		@Override
		public FixedBooleanProperty isNotNullProperty() {
			return TRUE;
		}
	};
	
	@Override
	default Boolean getValueOr(Boolean defaultValue) {
		return getValue();
	}
	
	@Override
	@NotNull
	default Boolean requireValue() {
		return getValue();
	}
	
	@Override
	default Boolean getValue() {
		return get();
	}
	
	/**
	 * Returns the primitive value of this property.
	 * @return The primitive value of this property.
	 */
	boolean get();
	
	default boolean has(boolean value) {
		return get() == value;
	}
	
	/**
	 * Returns a boolean property whose value is the negation of the value of this property.
	 * @return A boolean property that negates this property.
	 */
	default BooleanProperty not() {
		return () -> !get();
	}
	
	/**
	 * Returns a boolean property whose value is the AND operator applied to the value of this property and another property.
	 * @param booleanProperty The other boolean property
	 * @return A boolean property representing the AND operator.
	 */
	default BooleanProperty and(FixedBooleanProperty booleanProperty) {
		if (booleanProperty.get()) {
			return this;
		} else {
			return BooleanProperty.FALSE;
		}
	}
	
	/**
	 * Returns a boolean property whose value is the AND operator applied to the value of this property and another property.
	 * @param booleanProperty The other boolean property
	 * @return A boolean property representing the AND operator.
	 */
	default BooleanProperty and(BooleanProperty booleanProperty) {
		return () -> get() && booleanProperty.get();
	}
	
	/**
	 * Returns a boolean property whose value is the OR operator applied to the value of this property and another property.
	 * @param booleanProperty The other boolean property
	 * @return A boolean property representing the OR operator.
	 */
	default BooleanProperty or(FixedBooleanProperty booleanProperty) {
		if (!booleanProperty.get()) {
			return this;
		} else {
			return BooleanProperty.TRUE;
		}
	}
	
	/**
	 * Returns a boolean property whose value is the OR operator applied to the value of this property and another property.
	 * @param booleanProperty The other boolean property
	 * @return A boolean property representing the OR operator
	 */
	default BooleanProperty or(BooleanProperty booleanProperty) {
		return () -> get() || booleanProperty.get();
	}
	
	/**
	 * Returns a boolean property whose value is the XOR operator applied to the value of this property and another property.
	 * @param booleanProperty The other boolean property
	 * @return A boolean property representing the XOR operator
	 */
	default BooleanProperty xor(FixedBooleanProperty booleanProperty) {
		if (booleanProperty.get()) {
			return not();
		} else {
			return this;
		}
	}
	
	/**
	 * Returns a boolean property whose value is the XOR operator applied to the value of this property and another property.
	 * @param booleanProperty The other boolean property
	 * @return A boolean property representing the XOR operator
	 */
	default BooleanProperty xor(BooleanProperty booleanProperty) {
		return () -> get() ^ booleanProperty.get();
	}
	
	/**
	 * Returns a conditional property using this boolean property as the condition.
	 * @return The conditional property.
	 * @param <T> The type of property
	 */
	default <T> Property<T> ifElse(Property<T> propertyTrue, Property<T> propertyFalse) {
		return new ConditionalProperty<>(this, propertyTrue, propertyFalse);
	}
	
	@Contract("_ -> this")
	@Override
	default BooleanProperty replaceNull(Boolean defaultValue) {
		return this;
	}
	
	/**
	 * Converts a boolean to a boolean property.
	 * @param value The boolean value
	 * @return The boolean property with the given value.
	 */
	static FixedBooleanProperty fromValue(boolean value) {
		if (value) {
			return TRUE;
		} else {
			return FALSE;
		}
	}
	
	static BooleanProperty fromProperty(BooleanProperty property) {
		return property;
	}
	
	@Contract("_ -> new")
	static BooleanProperty fromProperty(Property<Boolean> property) {
		return property::getValue;
	}
	
	/**
	 * Returns the value of a given property, or a default value if the property is {@code null}.
	 * @param property The property or {@code null}
	 * @param defaultValue The value to use in case the property is {@code null}.
	 * @return The value
	 */
	@Contract("null, _ -> param2")
	static boolean getValueOf(BooleanProperty property, boolean defaultValue) {
		if (property == null) {
			return defaultValue;
		} else {
			return property.get();
		}
	}
	
}