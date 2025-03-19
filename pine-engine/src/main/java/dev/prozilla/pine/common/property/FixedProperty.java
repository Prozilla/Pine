package dev.prozilla.pine.common.property;

/**
 * Represents a property with a value that never changes.
 */
public class FixedProperty<T> extends VariableProperty<T> {
	
	protected final T value;
	
	public FixedProperty(T value) {
		this.value = value;
	}
	
	@Override
	public T getValue() {
		return value;
	}
}
