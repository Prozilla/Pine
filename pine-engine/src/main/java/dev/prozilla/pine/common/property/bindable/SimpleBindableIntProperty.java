package dev.prozilla.pine.common.property.bindable;

import dev.prozilla.pine.common.property.observable.ObservableIntPropertyBase;

public class SimpleBindableIntProperty extends ObservableIntPropertyBase implements BindableIntProperty {
	
	private int value;
	
	public SimpleBindableIntProperty() {
		this(0);
	}
	
	/**
	 * Creates a bindable property with an initial value.
	 * @param initialValue The initial value
	 */
	public SimpleBindableIntProperty(int initialValue) {
		value = initialValue;
	}
	
	@Override
	public int get() {
		return value;
	}
	
	@Override
	public boolean set(int value) {
		if (this.value == value) {
			return false;
		}
		
		onValueChange(this.value, value);
		this.value = value;
		return true;
	}
	
}
