package dev.prozilla.pine.common.property;

import dev.prozilla.pine.common.util.checks.Checks;
import dev.prozilla.pine.common.util.parser.Parser;
import org.jetbrains.annotations.NotNull;

/**
 * A property whose value is determined using the value of a string property and a parser.
 */
public class ParsedProperty<T> implements Property<T> {
	
	private final StringProperty inputProperty;
	private final Parser<T> parser;
	
	/**
	 * Creates a property whose value is determined using the value of a string property and a parser.
	 * @param inputProperty The string property to parse
	 * @param parser The parser to use
	 */
	public ParsedProperty(StringProperty inputProperty, Parser<T> parser) {
		this.inputProperty = Checks.isNotNull(inputProperty, "inputProperty");
		this.parser = Checks.isNotNull(parser, "parser");
	}
	
	/**
	 * Returns the parsed value, or {@code null} if the parsing failed.
	 * @return The parsed value, or {@code null} if the parsing failed.
	 */
	@Override
	public T getValue() {
		String input = inputProperty.getValue();
		if (input == null) {
			return null;
		}
		parser.parse(input);
		return parser.getResult();
	}
	
	/**
	 * Returns the input property.
	 * @return The input property.
	 * @see #inputProperty()
	 */
	@Override
	public StringProperty toStringProperty() {
		return inputProperty;
	}
	
	/**
	 * Returns the input property.
	 * @return The input property.
	 */
	public @NotNull StringProperty inputProperty() {
		return inputProperty;
	}
	
	/**
	 * Returns the parser used by this property.
	 * @return The parser used by this property.
	 */
	public @NotNull Parser<T> getParser() {
		return parser;
	}
	
}
