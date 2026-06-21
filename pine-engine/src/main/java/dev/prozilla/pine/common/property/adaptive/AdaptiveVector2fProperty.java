package dev.prozilla.pine.common.property.adaptive;

import dev.prozilla.pine.common.math.vector.Vector2f;
import dev.prozilla.pine.common.property.Property;
import dev.prozilla.pine.common.property.vector.Vector2fProperty;

public final class AdaptiveVector2fProperty extends AdaptiveObjectProperty<Vector2f> implements Vector2fProperty {
	
	/**
	 * Creates a new property with a dynamic value.
	 * @param property Variable property that determines the value of this property
	 */
	public AdaptiveVector2fProperty(Property<Vector2f> property) {
		super(property);
	}
	
	/**
	 * Creates a new property with a fixed value.
	 */
	public AdaptiveVector2fProperty(Vector2f fixedValue) {
		super(fixedValue);
	}
	
	/**
	 * Converts a color into an adaptive property.
	 * @param value The value of the property
	 */
	public static AdaptiveVector2fProperty adapt(Vector2f value) {
		return new AdaptiveVector2fProperty(value);
	}
	
	/**
	 * @see #adapt(Vector2fProperty)
	 */
	public static AdaptiveVector2fProperty adapt(AdaptiveVector2fProperty property) {
		return property;
	}
	
	/**
	 * Converts any color property into an adaptive property.
	 * @param property The property to adapt
	 */
	public static AdaptiveVector2fProperty adapt(Vector2fProperty property) {
		if (property instanceof AdaptiveVector2fProperty adaptiveProperty) {
			return adaptiveProperty;
		}
		return new AdaptiveVector2fProperty(property);
	}
	
}
