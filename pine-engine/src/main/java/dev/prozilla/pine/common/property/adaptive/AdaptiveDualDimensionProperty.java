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
	
	/**
	 * Converts a dual dimension into an adaptive property.
	 * @param value The value of the property
	 */
	public static AdaptiveDualDimensionProperty adapt(DualDimension value) {
		return new AdaptiveDualDimensionProperty(value);
	}
	
	public static AdaptiveDualDimensionProperty adapt(AdaptiveDualDimensionProperty property) {
		return property;
	}
	
	/**
	 * Converts any dual dimension property into an adaptive property.
	 * @param property The property to adapt
	 */
	public static AdaptiveDualDimensionProperty adapt(VariableProperty<DualDimension> property) {
		return new AdaptiveDualDimensionProperty(property);
	}
	
}
