package dev.prozilla.pine.common.property.bindable;

import dev.prozilla.pine.common.property.mutable.MutableFloatProperty;
import dev.prozilla.pine.common.property.observable.ObservableFloatProperty;

public interface BindableFloatProperty extends ObservableFloatProperty, MutableFloatProperty, BindableProperty<Float> {
	
}
