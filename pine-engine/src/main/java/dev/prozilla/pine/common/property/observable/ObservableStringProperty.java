package dev.prozilla.pine.common.property.observable;

import dev.prozilla.pine.common.property.mutable.MutableStringProperty;
import org.jetbrains.annotations.Contract;

/**
 * A property with a string value that can be observed.
 */
public interface ObservableStringProperty extends ObservableObjectProperty<String>, MutableStringProperty {
	
	/**
	 * Returns this property.
	 * @return This property.
	 */
	@Contract("-> this")
	@Override
	default ObservableStringProperty toStringProperty() {
		return this;
	}
	
}
