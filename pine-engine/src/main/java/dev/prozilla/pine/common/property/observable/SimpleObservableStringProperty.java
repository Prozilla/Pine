package dev.prozilla.pine.common.property.observable;

public class SimpleObservableStringProperty extends SimpleObservableObjectProperty<String> implements ObservableStringProperty {
	
	public SimpleObservableStringProperty() {
		this(null);
	}
	
	public SimpleObservableStringProperty(String initialValue) {
		super(initialValue);
	}
	
}
