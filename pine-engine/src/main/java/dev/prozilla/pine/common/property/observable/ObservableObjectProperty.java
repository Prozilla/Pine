package dev.prozilla.pine.common.property.observable;

import dev.prozilla.pine.common.property.mutable.MutableObjectProperty;

/**
 * An observable property that supports null values.
 */
public interface ObservableObjectProperty<T> extends ObservableProperty<T>, MutableObjectProperty<T> {

}
