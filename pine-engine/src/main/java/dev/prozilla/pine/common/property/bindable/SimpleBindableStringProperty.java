package dev.prozilla.pine.common.property.bindable;

public class SimpleBindableStringProperty extends SimpleBindableObjectProperty<String> implements BindableStringProperty {
	
	public SimpleBindableStringProperty() {
		this(null);
	}
	
	public SimpleBindableStringProperty(String initialValue) {
		super(initialValue);
	}
	
}
