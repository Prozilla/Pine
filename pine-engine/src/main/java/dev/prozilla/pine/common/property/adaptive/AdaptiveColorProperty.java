package dev.prozilla.pine.common.property.adaptive;

import dev.prozilla.pine.common.property.ColorProperty;
import dev.prozilla.pine.common.property.VariableProperty;
import dev.prozilla.pine.common.system.resource.Color;

public class AdaptiveColorProperty extends AdaptivePropertyBase<Color> implements ColorProperty {
	
	protected final Color fixedValue;
	
	public AdaptiveColorProperty(VariableProperty<Color> variableProperty) {
		super(variableProperty);
		fixedValue = null;
	}
	
	public AdaptiveColorProperty(Color fixedValue) {
		super(null);
		this.fixedValue = fixedValue;
	}
	
	@Override
	public Color getValue() {
		return ColorProperty.super.getColor();
	}
	
	// TO DO: Use AnimatedColorProperty instead and apply directly
	@Override
	public void apply(Color outputColor) {
		outputColor.copyFrom(getValue());
	}
}
