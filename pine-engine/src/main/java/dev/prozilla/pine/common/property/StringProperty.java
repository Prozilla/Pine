package dev.prozilla.pine.common.property;

import dev.prozilla.pine.common.util.checks.Checks;
import org.jetbrains.annotations.Contract;

@FunctionalInterface
public interface StringProperty extends Property<String> {
	
	@Contract("_ -> new")
	default JoinedStringProperty append(String string) {
		return append(() -> string);
	}
	
	@Contract("_ -> new")
	default JoinedStringProperty append(Property<String> stringProperty) {
		return new JoinedStringProperty(this, stringProperty);
	}
	
	@Contract("_ -> new")
	default JoinedStringProperty prepend(String string) {
		return prepend(() -> string);
	}
	
	@Contract("_ -> new")
	default JoinedStringProperty prepend(Property<String> stringProperty) {
		return new JoinedStringProperty(stringProperty, this);
	}
	
	@Override
	default StringProperty replaceNull(String defaultValue) {
		Checks.isNotNull(defaultValue, "defaultValue");
		return () -> getValueOr(defaultValue);
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
	
	static StringProperty fromProperty(StringProperty property) {
		return property;
	}
	
	@Contract("_ -> new")
	static StringProperty fromProperty(Property<String> property) {
		return property::getValue;
	}

}
