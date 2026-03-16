package dev.prozilla.pine.common.property.bindable;

import dev.prozilla.pine.common.property.observable.ObservableObjectPropertyBase;

import java.util.Objects;

public class SimpleBindableObjectProperty<T> extends ObservableObjectPropertyBase<T> implements BindableObjectProperty<T> {
	
	private T value;
	
	public SimpleBindableObjectProperty() {
		this(null);
	}
	
	/**
	 * Creates a bindable property with an initial value.
	 * @param initialValue The initial value
	 */
	public SimpleBindableObjectProperty(T initialValue) {
		value = initialValue;
	}
	
	@Override
	public T getValue() {
		return value;
	}
	
	@Override
	public boolean setValue(T value) {
		if (Objects.equals(this.value, value)) {
			return false;
		}
		
		onValueChange(this.value, value);
		this.value = value;
		return true;
	}
	
}
