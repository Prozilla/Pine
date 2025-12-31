package dev.prozilla.pine.common.property.mutable;

import dev.prozilla.pine.common.property.ArrayProperty;

import java.util.function.BiConsumer;

public class SimpleMutableArrayProperty<E> extends SimpleMutableObjectProperty<E[]> implements MutableArrayProperty<E> {
	
	@Override
	public boolean set(int index, E value) {
		return setItem(index, value, this, this::onValueChange);
	}
	
	public static <E> boolean setItem(int index, E item, ArrayProperty<E> property, BiConsumer<E[], E[]> onValueChange) {
		E[] previousValue = property.getValue();
		boolean changed = property.set(index, item);
		if (changed) {
			onValueChange.accept(previousValue, property.getValue());
		}
		return changed;
	}
	
}
