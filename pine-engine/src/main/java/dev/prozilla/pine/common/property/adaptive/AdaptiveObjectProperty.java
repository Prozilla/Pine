package dev.prozilla.pine.common.property.adaptive;

import dev.prozilla.pine.common.property.VariableProperty;

public final class AdaptiveObjectProperty extends AdaptiveProperty<Object> {
	
	public AdaptiveObjectProperty(VariableProperty<Object> variableProperty) {
		super(variableProperty);
	}
	
	public AdaptiveObjectProperty(Object fixedValue) {
		super(fixedValue);
	}
}
