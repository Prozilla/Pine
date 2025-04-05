package dev.prozilla.pine.common.property.adaptive;

import dev.prozilla.pine.common.math.dimension.DimensionBase;
import dev.prozilla.pine.common.property.DimensionProperty;
import dev.prozilla.pine.common.property.VariableProperty;

public final class AdaptiveDimensionProperty extends AdaptiveProperty<DimensionBase> implements DimensionProperty {
	
	public AdaptiveDimensionProperty(VariableProperty<DimensionBase> variableProperty) {
		super(variableProperty);
	}
	
	public AdaptiveDimensionProperty(DimensionBase fixedValue) {
		super(fixedValue);
	}
	
}
