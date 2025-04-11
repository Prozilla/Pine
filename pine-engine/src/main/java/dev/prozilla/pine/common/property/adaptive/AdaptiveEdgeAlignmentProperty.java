package dev.prozilla.pine.common.property.adaptive;

import dev.prozilla.pine.common.math.vector.EdgeAlignment;
import dev.prozilla.pine.common.property.VariableProperty;

public final class AdaptiveEdgeAlignmentProperty extends AdaptiveProperty<EdgeAlignment> {
	
	public AdaptiveEdgeAlignmentProperty(VariableProperty<EdgeAlignment> variableProperty) {
		super(variableProperty);
	}
	
	public AdaptiveEdgeAlignmentProperty(EdgeAlignment fixedValue) {
		super(fixedValue);
	}
	
	/**
	 * Converts an edge alignment into an adaptive property.
	 * @param value The value of the property
	 */
	public static AdaptiveEdgeAlignmentProperty adapt(EdgeAlignment value) {
		return new AdaptiveEdgeAlignmentProperty(value);
	}
	
	public static AdaptiveEdgeAlignmentProperty adapt(AdaptiveEdgeAlignmentProperty property) {
		return property;
	}
	
	/**
	 * Converts any edge alignment property into an adaptive property.
	 * @param property The property to adapt
	 */
	public static AdaptiveEdgeAlignmentProperty adapt(VariableProperty<EdgeAlignment> property) {
		return new AdaptiveEdgeAlignmentProperty(property);
	}
	
}
