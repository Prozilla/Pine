package dev.prozilla.pine.common.property.bindable;

import dev.prozilla.pine.common.property.mutable.MutableIntProperty;
import dev.prozilla.pine.common.property.observable.ObservableIntProperty;

public interface BindableIntProperty extends ObservableIntProperty, MutableIntProperty, BindableProperty<Integer> {
	
}
