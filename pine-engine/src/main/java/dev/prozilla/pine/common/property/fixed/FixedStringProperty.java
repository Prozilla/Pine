package dev.prozilla.pine.common.property.fixed;

import dev.prozilla.pine.common.property.StringProperty;
import dev.prozilla.pine.common.util.Parser;
import dev.prozilla.pine.common.util.StringUtils;
import dev.prozilla.pine.common.util.checks.Checks;
import org.jetbrains.annotations.Contract;

public class FixedStringProperty extends FixedObjectProperty<String> implements FixedProperty<String>, StringProperty {
	
	public FixedStringProperty(String value) {
		super(value);
	}
	
	@Override
	public <T> FixedObjectProperty<T> parse(Parser<T> parser) {
		Checks.isNotNull(parser, "parser");
		parser.parse(getValue());
		return new FixedObjectProperty<>(parser.getResult());
	}
	
	@Contract("-> this")
	@Override
	public FixedStringProperty toStringProperty() {
		return this;
	}
	
	@Contract("-> new")
	@Override
	public FixedIntProperty lengthProperty() {
		return new FixedIntProperty(StringUtils.lengthOf(getValue()));
	}
	
}
