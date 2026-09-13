package dev.prozilla.pine.common.property.adaptive;

import dev.prozilla.pine.common.property.Property;
import dev.prozilla.pine.common.property.fixed.FixedProperty;

public class AdaptiveObjectProperty<T> extends AdaptiveProperty<T, Property<T>> {
	
	private final T fixedValue;
	
	/**
	 * Creates a new property with a dynamic value.
	 * @param property Variable property that determines the value of this property
	 */
	public AdaptiveObjectProperty(Property<T> property) {
		super(property);
		this.fixedValue = property instanceof FixedProperty<T> fixedProperty ? fixedProperty.getValue() : null;
	}
	
	/**
	 * Creates a new property with a fixed value.
	 */
	public AdaptiveObjectProperty(T fixedValue) {
		super(null);
		this.fixedValue = fixedValue;
	}
	
	@Override
	public T getValue() {
		return isDynamic() ? property.getValue() : fixedValue;
	}
	
	@Override
	public boolean isDynamic() {
		return fixedValue == null;
	}
	
	/**
	 * Converts an object into an adaptive property.
	 * @param value The value of the property
	 */
	public static <T> AdaptiveObjectProperty<T> adapt(T value) {
		return new AdaptiveObjectProperty<>(value);
	}
	
	/**
	 * @see #adapt(Property)
	 */
	public static <T> AdaptiveObjectProperty<T> adapt(AdaptiveObjectProperty<T> property) {
		return property;
	}
	
	/**
	 * Converts any object property into an adaptive property.
	 * @param property The property to adapt
	 */
	public static <T> AdaptiveObjectProperty<T> adapt(Property<T> property) {
		if (property instanceof AdaptiveObjectProperty<T> adaptiveProperty) {
			return adaptiveProperty;
		}
		return new AdaptiveObjectProperty<>(property);
	}
	
}
