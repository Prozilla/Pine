package dev.prozilla.pine.common.property.mutable;

import dev.prozilla.pine.common.property.StringProperty;
import dev.prozilla.pine.common.util.StringUtils;
import dev.prozilla.pine.common.util.function.mapper.Mapper;
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
	 * @see StringUtils#toUpperCase(String)
	 */
	default void toUpperCase() {
		setValue(StringUtils.toUpperCase(getValue()));
	}
	
	/**
	 * Converts the value of this property to lower case.
	 * @see StringUtils#toLowerCase(String) 
	 */
	default void toLowerCase() {
		setValue(StringUtils.toLowerCase(getValue()));
	}
	
	/**
	 * Removes spaces around the value of this property.
	 * @see StringUtils#trim(String) 
	 */
	default void trimValue() {
		setValue(StringUtils.trim(getValue()));
	}
	
	default boolean buildValue(Mapper<StringBuilder, Object> mapper) {
		StringBuilder stringBuilder = isNotNull() ? new StringBuilder(getValue()) : new StringBuilder();
		
		Object object = mapper.map(stringBuilder);
		if (object == null) {
			return false;
		}
		
		return setValue(object.toString());
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
