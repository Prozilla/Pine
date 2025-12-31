package dev.prozilla.pine.common.property.fixed;

import dev.prozilla.pine.common.property.StringProperty;
import dev.prozilla.pine.common.util.StringUtils;
import dev.prozilla.pine.common.util.checks.Checks;
import dev.prozilla.pine.common.util.parser.Parser;
import org.jetbrains.annotations.Contract;

/**
 * A property with a string value that never changes.
 */
public class FixedStringProperty extends FixedObjectProperty<String> implements StringProperty, FixedProperty<String> {
	
	/**
	 * Creates a fixed property with the given value.
	 */
	public FixedStringProperty(String value) {
		super(value);
	}
	
	@Override
	public <T> FixedProperty<T> parse(Parser<T> parser) {
		Checks.isNotNull(parser, "parser");
		if (!isNotNull()) {
			return new NullProperty<>();
		}
		parser.parse(getValue());
		return FixedProperty.fromValue(parser.getResult());
	}
	
	/**
	 * Returns this property.
	 * @return This property.
	 */
	@Contract("-> this")
	@Override
	public FixedStringProperty toStringProperty() {
		return this;
	}
	
	@Contract("-> this")
	@Override
	public FixedStringProperty snapshot() {
		return this;
	}
	
	@Contract("-> new")
	@Override
	public FixedIntProperty lengthProperty() {
		return new FixedIntProperty(StringUtils.lengthOf(getValue()));
	}
	
	@Override
	public FixedStringProperty toUpperCaseProperty() {
		return new FixedStringProperty(StringUtils.toUpperCase(getValue()));
	}
	
	@Override
	public FixedStringProperty toLowerCaseProperty() {
		return new FixedStringProperty(StringUtils.toLowerCase(getValue()));
	}
	
	@Override
	public FixedStringProperty replaceNull(String defaultValue) {
		if (isNotNull()) {
			return this;
		} else {
			return new FixedStringProperty(defaultValue);
		}
	}
	
}
