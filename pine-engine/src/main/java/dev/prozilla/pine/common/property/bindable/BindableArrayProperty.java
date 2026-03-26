package dev.prozilla.pine.common.property.bindable;

import dev.prozilla.pine.common.property.mutable.MutableArrayProperty;
import dev.prozilla.pine.common.property.observable.ObservableArrayProperty;
import dev.prozilla.pine.common.property.observable.ObservableObjectProperty;

public interface BindableArrayProperty<E> extends ObservableArrayProperty<E>, MutableArrayProperty<E>, ObservableObjectProperty<E[]> {

}
