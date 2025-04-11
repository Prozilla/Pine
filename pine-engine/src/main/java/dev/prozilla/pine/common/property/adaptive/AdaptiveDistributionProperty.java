package dev.prozilla.pine.common.property.adaptive;

import dev.prozilla.pine.common.property.VariableProperty;
import dev.prozilla.pine.core.component.ui.LayoutNode;

public final class AdaptiveDistributionProperty extends AdaptiveProperty<LayoutNode.Distribution> {
	
	public AdaptiveDistributionProperty(VariableProperty<LayoutNode.Distribution> variableProperty) {
		super(variableProperty);
	}
	
	public AdaptiveDistributionProperty(LayoutNode.Distribution fixedValue) {
		super(fixedValue);
	}
	
	/**
	 * Converts a distribution into an adaptive property.
	 * @param value The value of the property
	 */
	public static AdaptiveDistributionProperty adapt(LayoutNode.Distribution value) {
		return new AdaptiveDistributionProperty(value);
	}
	
	public static AdaptiveDistributionProperty adapt(AdaptiveDistributionProperty property) {
		return property;
	}
	
	/**
	 * Converts any distribution property into an adaptive property.
	 * @param property The property to adapt
	 */
	public static AdaptiveDistributionProperty adapt(VariableProperty<LayoutNode.Distribution> property) {
		return new AdaptiveDistributionProperty(property);
	}
	
}
