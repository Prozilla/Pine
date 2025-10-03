package dev.prozilla.pine.common.property;

import dev.prozilla.pine.common.property.fixed.FixedBooleanProperty;
import org.jetbrains.annotations.Contract;

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
		public FixedBooleanProperty existenceProperty() {
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
		public FixedBooleanProperty existenceProperty() {
			return TRUE;
		}
	};
	
	@Override
	default Boolean getValue() {
		return get();
	}
	
	@Override
	default Boolean getValueOr(Boolean defaultValue) {
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
	static boolean getPropertyValue(BooleanProperty property, boolean defaultValue) {
		if (property == null) {
			return defaultValue;
		} else {
			return property.get();
		}
	}
	
}