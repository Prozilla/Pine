package dev.prozilla.pine.common.property.mutable;

public class SimpleMutableStringProperty extends SimpleMutableObjectProperty<String> implements MutableStringProperty {
	
	public SimpleMutableStringProperty() {
		this(null);
	}
	
	public SimpleMutableStringProperty(String initialValue) {
		super(initialValue);
	}
	
}
