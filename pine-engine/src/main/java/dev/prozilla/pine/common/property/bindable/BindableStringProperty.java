package dev.prozilla.pine.common.property.bindable;

import dev.prozilla.pine.common.property.mutable.MutableStringProperty;
import dev.prozilla.pine.common.property.observable.ObservableStringProperty;

public interface BindableStringProperty extends ObservableStringProperty, MutableStringProperty, BindableObjectProperty<String> {
	
	@Override
	default BindableStringProperty toStringProperty() {
		return this;
	}
	
}
