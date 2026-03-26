package dev.prozilla.pine.common.property.bindable;

import dev.prozilla.pine.common.property.mutable.MutableProperty;
import dev.prozilla.pine.common.property.observable.ObservableProperty;

public interface BindableProperty<T> extends ObservableProperty<T>, MutableProperty<T> {



}
