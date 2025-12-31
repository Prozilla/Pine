package dev.prozilla.pine.common.property.observable;

import dev.prozilla.pine.common.property.mutable.SimpleMutableArrayProperty;

public class SimpleObservableArrayProperty<E> extends SimpleObservableObjectProperty<E[]> implements ObservableArrayProperty<E> {
	
	@Override
	public boolean set(int index, E value) {
		return SimpleMutableArrayProperty.setItem(index, value, this, this::onValueChange);
	}
	
}
