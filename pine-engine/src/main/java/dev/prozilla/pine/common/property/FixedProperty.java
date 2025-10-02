package dev.prozilla.pine.common.property;

import dev.prozilla.pine.common.Cloneable;

import java.util.Objects;

/**
 * A property with a value that never changes.
 */
public class FixedProperty<T> implements Property<T>, Cloneable<FixedProperty<T>> {
	
	protected final T value;
	
	public FixedProperty(T value) {
		this.value = value;
	}
	
	@Override
	public final T getValue() {
		return value;
	}
	
	@Override
	public boolean equals(Object object) {
		return object == this || (object instanceof FixedProperty<?> fixedProperty && equals(fixedProperty));
	}
	
	@Override
	public boolean equals(FixedProperty<T> fixedProperty) {
		return fixedProperty != null && Objects.equals(fixedProperty.value, value);
	}
	
	@Override
	public FixedProperty<T> clone() {
		return new FixedProperty<>(value);
	}
	
}
