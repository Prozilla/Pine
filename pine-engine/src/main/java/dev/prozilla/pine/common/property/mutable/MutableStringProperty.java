package dev.prozilla.pine.common.property.mutable;

import dev.prozilla.pine.common.property.StringProperty;
import dev.prozilla.pine.common.util.StringUtils;
import org.jetbrains.annotations.Contract;

/**
 * A property with a string value that can be changed.
 */
public interface MutableStringProperty extends StringProperty, MutableObjectProperty<String> {
	
	/**
	 * Appends the given string to the value of this property.
	 * @param string The string to append.
	 */
	default void appendValue(String string) {
		setValue(getValue() + string);
	}
	
	/**
	 * Prepends the given string to the value of this property.
	 * @param string The string to prepend.
	 */
	default void prependValue(String string) {
		setValue(string + getValue());
	}
	
	/**
	 * Converts the value of this property to upper case.
	 */
	default void toUpperCase() {
		setValue(StringUtils.toUpperCase(getValue()));
	}
	
	/**
	 * Converts the value of this property to lower case.
	 */
	default void toLowerCase() {
		setValue(StringUtils.toLowerCase(getValue()));
	}
	
	default void trimValue() {
		setValue(StringUtils.trim(getValue()));
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
	
	@Override
	default StringProperty viewProperty() {
		return this::getValue;
	}
	
}
