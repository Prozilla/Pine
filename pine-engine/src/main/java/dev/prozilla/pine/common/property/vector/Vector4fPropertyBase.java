package dev.prozilla.pine.common.property.vector;

import dev.prozilla.pine.common.math.vector.VectorFloat;
import dev.prozilla.pine.common.property.FloatProperty;

/**
 * A vector property with 4 dimensions.
 */
public interface Vector4fPropertyBase<T extends VectorFloat<T>> extends Vector3fPropertyBase<T> {
	
	default FloatProperty wProperty() {
		return this::getW;
	}
	
	/**
	 * Returns the w value of the current vector, or {@code 0} if the vector is {@code null}.
	 * @return The w value of the current vector.
	 */
	float getW();

}
