package dev.prozilla.pine.common.property.fixed;

import dev.prozilla.pine.common.Cloneable;

import java.util.Objects;

/**
 * A property with a value that never changes.
 */
public class FixedObjectProperty<T> implements FixedProperty<T>, Cloneable<FixedObjectProperty<T>> {
	
	protected final T value;
	
	public FixedObjectProperty(T value) {
		this.value = value;
	}
	
	@Override
	public final T getValue() {
		return value;
	}
	
	@Override
	public boolean equals(Object object) {
		return object == this || (object instanceof FixedObjectProperty<?> fixedProperty && equals(fixedProperty));
	}
	
	@Override
	public boolean equals(FixedObjectProperty<T> fixedProperty) {
		return fixedProperty != null && Objects.equals(fixedProperty.value, value);
	}
	
	@Override
	public FixedObjectProperty<T> clone() {
		return new FixedObjectProperty<>(value);
	}
	
}
