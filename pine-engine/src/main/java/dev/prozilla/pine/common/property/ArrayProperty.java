package dev.prozilla.pine.common.property;

import com.fasterxml.jackson.databind.util.ArrayIterator;
import dev.prozilla.pine.common.util.ArrayUtils;
import org.gradle.internal.impldep.com.google.common.base.Objects;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Iterator;

@FunctionalInterface
public interface ArrayProperty<E> extends Property<E[]>, Iterable<E> {
	
	default E get(int index) {
		if (index < 0) {
			return null;
		}
		E[] array = getValue();
		return array == null || index >= array.length ? null : array[index];
	}
	
	default boolean set(int index, E value) {
		if (index < 0) {
			return false;
		}
		E[] array = getValue();
		if (array == null || index >= array.length || Objects.equal(array[index], value)) {
			return false;
		}
		array[index] = value;
		return true;
	}
	
	default IntProperty lengthProperty() {
		return this::getLength;
	}
	
	default int getLength() {
		return ArrayUtils.lengthOf(getValue());
	}
	
	@Override
	default @NotNull Iterator<E> iterator() {
		E[] value = getValue();
		if (value == null) {
			return Collections.emptyIterator();
		}
		return new ArrayIterator<>(value);
	}
	
}
