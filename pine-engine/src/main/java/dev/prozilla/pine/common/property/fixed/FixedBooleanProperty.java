package dev.prozilla.pine.common.property.fixed;

import dev.prozilla.pine.common.property.BooleanProperty;
import dev.prozilla.pine.common.property.Property;
import org.jetbrains.annotations.Contract;

public abstract class FixedBooleanProperty implements BooleanProperty, FixedProperty<Boolean> {
	
	protected FixedBooleanProperty() {}
	
	@Override
	abstract public FixedBooleanProperty not();
	
	@Override
	public BooleanProperty and(BooleanProperty booleanProperty) {
		if (!get()) {
			return BooleanProperty.FALSE;
		} else {
			return booleanProperty;
		}
	}
	
	@Override
	public BooleanProperty or(BooleanProperty booleanProperty) {
		if (get()) {
			return BooleanProperty.TRUE;
		} else {
			return booleanProperty;
		}
	}
	
	@Override
	public BooleanProperty xor(BooleanProperty booleanProperty) {
		if (get()) {
			return booleanProperty.not();
		} else {
			return booleanProperty;
		}
	}
	
	@Override
	public <T> Property<T> ifElse(Property<T> propertyTrue, Property<T> propertyFalse) {
		return get() ? propertyTrue : propertyFalse;
	}
	
	@Override
	public FixedBooleanProperty isNotNullProperty() {
		return BooleanProperty.TRUE;
	}
	
	@Contract("-> this")
	@Override
	public FixedBooleanProperty snapshot() {
		return this;
	}
	
	@Contract("_ -> this")
	@Override
	public FixedBooleanProperty replaceNull(Boolean defaultValue) {
		return this;
	}
	
	@Override
	public FixedBooleanProperty hasProperty(boolean value) {
		return BooleanProperty.fromValue(has(value));
	}
	
}
