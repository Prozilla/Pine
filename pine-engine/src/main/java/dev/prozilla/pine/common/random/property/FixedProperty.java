package dev.prozilla.pine.common.random.property;

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
