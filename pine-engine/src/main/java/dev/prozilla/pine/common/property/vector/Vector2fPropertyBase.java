package dev.prozilla.pine.common.property.vector;

import dev.prozilla.pine.common.math.vector.VectorFloat;
import dev.prozilla.pine.common.property.FloatProperty;
import dev.prozilla.pine.common.property.Property;

/**
 * A vector property with 2 dimensions.
 */
public interface Vector2fPropertyBase<T extends VectorFloat<T>> extends Property<T> {
	
	default FloatProperty xProperty() {
		return this::getX;
	}
	
	/**
	 * Returns the x value of the current vector, or {@code 0} if the vector is {@code null}.
	 * @return The x value of the current vector.
	 */
	float getX();
	
	default FloatProperty yProperty() {
		return this::getY;
	}
	
	/**
	 * Returns the y value of the current vector, or {@code 0} if the vector is {@code null}.
	 * @return The y value of the current vector.
	 */
	float getY();
	
}
