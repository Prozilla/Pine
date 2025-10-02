package dev.prozilla.pine.common.property;

import dev.prozilla.pine.common.property.fixed.FixedBooleanProperty;
import org.jetbrains.annotations.Contract;

@FunctionalInterface
public interface BooleanProperty extends Property<Boolean> {
	
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
		public FixedBooleanProperty existsProperty() {
			return this;
		}
	};
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
		public FixedBooleanProperty existsProperty() {
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
	
	boolean get();
	
	@Contract("-> true")
	@Override
	default boolean exists() {
		return true;
	}
	
	default boolean has(boolean value) {
		return get() == value;
	}
	
	default BooleanProperty not() {
		return () -> !get();
	}
	
	/**
	 * Returns this property.
	 * @return This property.
	 */
	@Contract("_ -> this")
	@Override
	default BooleanProperty replaceNull(Boolean defaultValue) {
		return this;
	}
	
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
	
}