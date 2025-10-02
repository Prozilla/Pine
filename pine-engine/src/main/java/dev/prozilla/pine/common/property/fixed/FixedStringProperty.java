package dev.prozilla.pine.common.property.fixed;

import dev.prozilla.pine.common.property.StringProperty;

public class FixedStringProperty implements FixedProperty<String>, StringProperty {
	
	protected final String value;
	
	public FixedStringProperty(String value) {
		this.value = value;
	}
	
	@Override
	public String getValue() {
		return value;
	}
	
	@Override
	public FixedStringProperty toStringProperty() {
		return this;
	}
	
}
