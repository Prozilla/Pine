package dev.prozilla.pine.common.property.adaptive;

import dev.prozilla.pine.common.math.vector.GridAlignment;
import dev.prozilla.pine.common.property.VariableProperty;

public final class AdaptiveGridAlignmentProperty extends AdaptiveProperty<GridAlignment> {
	
	public AdaptiveGridAlignmentProperty(VariableProperty<GridAlignment> variableProperty) {
		super(variableProperty);
	}
	
	public AdaptiveGridAlignmentProperty(GridAlignment fixedValue) {
		super(fixedValue);
	}
	
	/**
	 * Converts a grid alignment into an adaptive property.
	 * @param value The value of the property
	 */
	public static AdaptiveGridAlignmentProperty adapt(GridAlignment value) {
		return new AdaptiveGridAlignmentProperty(value);
	}
	
	public static AdaptiveGridAlignmentProperty adapt(AdaptiveGridAlignmentProperty property) {
		return property;
	}
	
	/**
	 * Converts any grid alignment property into an adaptive property.
	 * @param property The property to adapt
	 */
	public static AdaptiveGridAlignmentProperty adapt(VariableProperty<GridAlignment> property) {
		return new AdaptiveGridAlignmentProperty(property);
	}
	
}
