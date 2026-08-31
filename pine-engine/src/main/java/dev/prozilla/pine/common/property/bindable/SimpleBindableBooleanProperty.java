package dev.prozilla.pine.common.property.bindable;

import dev.prozilla.pine.common.property.observable.ObservableBooleanPropertyBase;

public class SimpleBindableBooleanProperty extends ObservableBooleanPropertyBase implements BindableBooleanProperty {
	
	private boolean value;
	
	public SimpleBindableBooleanProperty() {
		this(false);
	}
	
	/**
	 * Creates a bindable property with an initial value.
	 * @param initialValue The initial value
	 */
	public SimpleBindableBooleanProperty(boolean initialValue) {
		value = initialValue;
	}
	
	@Override
	public boolean get() {
		return value;
	}
	
	@Override
	public boolean set(boolean value) {
		if (this.value == value) {
			return false;
		}
		
		this.value = value;
		onValueChange(this.value, value);
		return true;
	}
	
}
