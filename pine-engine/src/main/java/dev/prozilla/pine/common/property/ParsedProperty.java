package dev.prozilla.pine.common.property;

import dev.prozilla.pine.common.util.Parser;
import dev.prozilla.pine.common.util.checks.Checks;

public class ParsedProperty<T> extends VariableProperty<T> {
	
	private final VariableProperty<String> inputProperty;
	private final Parser<T> parser;
	
	public ParsedProperty(VariableProperty<String> inputProperty, Parser<T> parser) {
		this.inputProperty = Checks.isNotNull(inputProperty, "inputProperty");
		this.parser = Checks.isNotNull(parser, "parser");
	}
	
	@Override
	public T getValue() {
		parser.parse(inputProperty.getValue());
		return parser.getResult();
	}
	
	public Parser<T> getParser() {
		return parser;
	}
	
}
