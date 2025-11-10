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
	
	public @NotNull StringProperty inputProperty() {
		return inputProperty;
	}
	
	public @NotNull Parser<T> getParser() {
		return parser;
	}
	
}
