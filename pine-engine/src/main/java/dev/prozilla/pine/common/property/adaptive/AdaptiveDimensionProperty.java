package dev.prozilla.pine.common.property.adaptive;

import dev.prozilla.pine.common.math.dimension.DimensionBase;
import dev.prozilla.pine.common.property.Property;

public final class AdaptiveDimensionProperty extends AdaptiveProperty<DimensionBase> {
	
	public AdaptiveDimensionProperty(Property<DimensionBase> property) {
		super(property);
	}
	
	public AdaptiveDimensionProperty(DimensionBase fixedValue) {
		super(fixedValue);
	}
	
	/**
	 * Converts a dimension into an adaptive property.
	 * @param value The value of the property
	 */
	public static AdaptiveDimensionProperty adapt(DimensionBase value) {
		return new AdaptiveDimensionProperty(value);
	}
	
	public static AdaptiveDimensionProperty adapt(AdaptiveDimensionProperty property) {
		return property;
	}
	
	/**
	 * Converts any dimension property into an adaptive property.
	 * @param property The property to adapt
	 */
	public static AdaptiveDimensionProperty adapt(Property<DimensionBase> property) {
		return new AdaptiveDimensionProperty(property);
	}
	
}
