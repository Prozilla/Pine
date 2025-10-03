package dev.prozilla.pine.common.property;

import dev.prozilla.pine.common.property.fixed.FixedObjectProperty;
import dev.prozilla.pine.common.property.fixed.FixedStringProperty;
import dev.prozilla.pine.common.util.Parser;
import dev.prozilla.pine.common.util.StringUtils;
import dev.prozilla.pine.common.util.checks.Checks;
import org.jetbrains.annotations.Contract;

/**
 * A property with a string value.
 */
@FunctionalInterface
public interface StringProperty extends Property<String> {
	
	/**
	 * @see #append(StringProperty)
	 */
	@Contract("_ -> new")
	default JoinedStringProperty append(String string) {
		return append(() -> string);
	}
	
	/**
	 * Returns a string property whose value is the value of this property, followed by the value of {@code stringProperty}.
	 * @param stringProperty The string property to append to this property
	 * @return A joined string property that combines both string properties.
	 */
	@Contract("_ -> new")
	default JoinedStringProperty append(StringProperty stringProperty) {
		return new JoinedStringProperty(this, stringProperty);
	}
	
	/**
	 * @see #prepend(StringProperty)
	 */
	@Contract("_ -> new")
	default JoinedStringProperty prepend(String string) {
		return prepend(() -> string);
	}
	
	/**
	 * Returns a string property whose value is the value of {@code stringProperty}, followed by the value of this property.
	 * @param stringProperty The string property to prepend to this property
	 * @return A joined string property that combines both string properties.
	 */
	@Contract("_ -> new")
	default JoinedStringProperty prepend(StringProperty stringProperty) {
		return new JoinedStringProperty(stringProperty, this);
	}
	
	@Override
	default StringProperty replaceNull(String defaultValue) {
		Checks.isNotNull(defaultValue, "defaultValue");
		return () -> getValueOr(defaultValue);
	}
	
	/**
	 * Returns a property whose value is the result of parsing the value of this property.
	 *
	 * <p>The return value of this method is not restricted to {@link ParsedProperty}, because some string properties use a different implementation.
	 * For example, the default implementation of {@link FixedStringProperty#parse(Parser)} returns a {@link FixedObjectProperty}, because the input string,
	 * and therefore the result of the parser, is never going to change.</p>
	 *
	 * <p>In case you rely on the return value being of type {@link ParsedProperty}, use {@link Parser#parseProperty(StringProperty)} instead.</p>
	 * @param parser The parser to use
	 * @return The parsed property.
	 * @param <T> The type of result of the parser
	 * @see Parser#parseProperty(StringProperty)
	 */
	default <T> Property<T> parse(Parser<T> parser) {
		Checks.isNotNull(parser, "parser");
		return parser.parseProperty(this);
	}
	
	/**
	 * Returns this property.
	 * @return This property.
	 */
	@Contract("-> this")
	@Override
	default StringProperty toStringProperty() {
		return this;
	}
	
	/**
	 * Returns an integer property whose value is the length of the value of this property.
	 * @return An integer property representing the length of the value of this property.
	 */
	default IntProperty lengthProperty() {
		return () -> StringUtils.lengthOf(getValue());
	}
	
	static StringProperty fromProperty(StringProperty property) {
		return property;
	}
	
	@Contract("_ -> new")
	static StringProperty fromProperty(Property<String> property) {
		return property::getValue;
	}

}
