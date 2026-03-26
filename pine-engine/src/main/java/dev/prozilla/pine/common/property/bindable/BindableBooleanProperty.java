package dev.prozilla.pine.common.property.bindable;

import dev.prozilla.pine.common.property.mutable.MutableBooleanProperty;
import dev.prozilla.pine.common.property.observable.ObservableBooleanProperty;

public interface BindableBooleanProperty extends ObservableBooleanProperty, MutableBooleanProperty, BindableProperty<Boolean> {

}
