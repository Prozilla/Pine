package dev.prozilla.pine.common.property.mutable;

import dev.prozilla.pine.common.property.ArrayProperty;

public interface MutableArrayProperty<E> extends MutableObjectProperty<E[]>, ArrayProperty<E> {
	
	default boolean set(E... value) {
		return setValue(value);
	}
	
	@Override
	default ArrayProperty<E> viewProperty() {
		return this;
	}
	
}
