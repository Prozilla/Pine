package dev.prozilla.pine.common.property.adaptive;

import dev.prozilla.pine.common.math.vector.Vector3f;
import dev.prozilla.pine.common.property.Property;
import dev.prozilla.pine.common.property.vector.Vector3fProperty;

public final class AdaptiveVector3fProperty extends AdaptiveObjectProperty<Vector3f> implements Vector3fProperty {
	
	/**
	 * Creates a new property with a dynamic value.
	 * @param property Variable property that determines the value of this property
	 */
	public AdaptiveVector3fProperty(Property<Vector3f> property) {
		super(property);
	}
	
	/**
	 * Creates a new property with a fixed value.
	 */
	public AdaptiveVector3fProperty(Vector3f fixedValue) {
		super(fixedValue);
	}
	
	/**
	 * Converts a color into an adaptive property.
	 * @param value The value of the property
	 */
	public static AdaptiveVector3fProperty adapt(Vector3f value) {
		return new AdaptiveVector3fProperty(value);
	}
	
	/**
	 * @see #adapt(Vector3fProperty)
	 */
	public static AdaptiveVector3fProperty adapt(AdaptiveVector3fProperty property) {
		return property;
	}
	
	/**
	 * Converts any color property into an adaptive property.
	 * @param property The property to adapt
	 */
	public static AdaptiveVector3fProperty adapt(Vector3fProperty property) {
		if (property instanceof AdaptiveVector3fProperty adaptiveProperty) {
			return adaptiveProperty;
		}
		return new AdaptiveVector3fProperty(property);
	}
	
}
