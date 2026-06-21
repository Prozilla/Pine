package dev.prozilla.pine.common.property.adaptive;

import dev.prozilla.pine.common.math.vector.Vector4f;
import dev.prozilla.pine.common.property.Property;
import dev.prozilla.pine.common.property.vector.Vector4fProperty;

public final class AdaptiveVector4fProperty extends AdaptiveObjectProperty<Vector4f> implements Vector4fProperty {
	
	/**
	 * Creates a new property with a dynamic value.
	 * @param property Variable property that determines the value of this property
	 */
	public AdaptiveVector4fProperty(Property<Vector4f> property) {
		super(property);
	}
	
	/**
	 * Creates a new property with a fixed value.
	 */
	public AdaptiveVector4fProperty(Vector4f fixedValue) {
		super(fixedValue);
	}
	
	/**
	 * Converts a color into an adaptive property.
	 * @param value The value of the property
	 */
	public static AdaptiveVector4fProperty adapt(Vector4f value) {
		return new AdaptiveVector4fProperty(value);
	}
	
	/**
	 * @see #adapt(Vector4fProperty)
	 */
	public static AdaptiveVector4fProperty adapt(AdaptiveVector4fProperty property) {
		return property;
	}
	
	/**
	 * Converts any color property into an adaptive property.
	 * @param property The property to adapt
	 */
	public static AdaptiveVector4fProperty adapt(Vector4fProperty property) {
		if (property instanceof AdaptiveVector4fProperty adaptiveProperty) {
			return adaptiveProperty;
		}
		return new AdaptiveVector4fProperty(property);
	}
	
}
