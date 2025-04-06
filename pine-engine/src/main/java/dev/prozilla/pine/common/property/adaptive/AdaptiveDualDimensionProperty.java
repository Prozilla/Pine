package dev.prozilla.pine.common.property.adaptive;

import dev.prozilla.pine.common.math.dimension.DualDimension;
import dev.prozilla.pine.common.property.VariableProperty;

public final class AdaptiveDualDimensionProperty extends AdaptiveProperty<DualDimension> {
	
	public AdaptiveDualDimensionProperty(VariableProperty<DualDimension> variableProperty) {
		super(variableProperty);
	}
	
	public AdaptiveDualDimensionProperty(DualDimension fixedValue) {
		super(fixedValue);
	}
	
}
