package dev.prozilla.pine.common.property.mutable;

import dev.prozilla.pine.common.property.StringProperty;
import org.jetbrains.annotations.Contract;

/**
 * A property with a string value that can be changed.
 */
public interface MutableStringProperty extends MutableObjectProperty<String>, StringProperty {
	
	default void appendString(String string) {
		setValue(getValue() + string);
	}
	
	default void prependString(String string) {
		setValue(string + getValue());
	}
	
	/**
	 * Returns this property.
	 * @return This property.
	 */
	@Contract("-> this")
	@Override
	default MutableStringProperty toStringProperty() {
		return this;
	}
	
}
