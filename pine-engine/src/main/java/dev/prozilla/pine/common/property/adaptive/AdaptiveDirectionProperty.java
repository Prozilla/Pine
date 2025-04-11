package dev.prozilla.pine.common.property.adaptive;

import dev.prozilla.pine.common.math.vector.Direction;
import dev.prozilla.pine.common.property.VariableProperty;

public final class AdaptiveDirectionProperty extends AdaptiveProperty<Direction> {
	
	public AdaptiveDirectionProperty(VariableProperty<Direction> variableProperty) {
		super(variableProperty);
	}
	
	public AdaptiveDirectionProperty(Direction fixedValue) {
		super(fixedValue);
	}
	
	/**
	 * Converts a direction into an adaptive property.
	 * @param value The value of the property
	 */
	public static AdaptiveDirectionProperty adapt(Direction value) {
		return new AdaptiveDirectionProperty(value);
	}
	
	public static AdaptiveDirectionProperty adapt(AdaptiveDirectionProperty property) {
		return property;
	}
	
	/**
	 * Converts any direction property into an adaptive property.
	 * @param property The property to adapt
	 */
	public static AdaptiveDirectionProperty adapt(VariableProperty<Direction> property) {
		return new AdaptiveDirectionProperty(property);
	}
	
}
