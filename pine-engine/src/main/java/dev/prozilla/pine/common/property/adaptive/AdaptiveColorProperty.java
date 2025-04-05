package dev.prozilla.pine.common.property.adaptive;

import dev.prozilla.pine.common.property.ColorProperty;
import dev.prozilla.pine.common.property.VariableProperty;
import dev.prozilla.pine.common.system.resource.Color;

public class AdaptiveColorProperty extends AdaptiveProperty<Color> implements ColorProperty {
	
	public AdaptiveColorProperty(VariableProperty<Color> variableProperty) {
		super(variableProperty);
	}
	
	public AdaptiveColorProperty(Color fixedValue) {
		super(fixedValue);
	}
	
	// TO DO: Use AnimatedColorProperty instead and apply directly
	@Override
	public void apply(Color target) {
		target.copyFrom(getValue());
	}
}
