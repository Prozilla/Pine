package dev.prozilla.pine.common.property.bindable;

import dev.prozilla.pine.common.property.observable.ObservableFloatPropertyBase;

public class SimpleBindableFloatProperty extends ObservableFloatPropertyBase implements BindableFloatProperty {
	
	private float value;
	
	public SimpleBindableFloatProperty() {
		this(0);
	}
	
	/**
	 * Creates a bindable property with an initial value.
	 * @param initialValue The initial value
	 */
	public SimpleBindableFloatProperty(float initialValue) {
		value = initialValue;
	}
	
	@Override
	public float get() {
		return value;
	}
	
	@Override
	public boolean set(float value) {
		if (this.value == value) {
			return false;
		}
		
		this.value = value;
		onValueChange(this.value, value);
		return true;
	}
	
}
