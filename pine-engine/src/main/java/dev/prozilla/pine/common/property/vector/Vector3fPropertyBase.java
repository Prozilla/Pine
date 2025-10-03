package dev.prozilla.pine.common.property.vector;

import dev.prozilla.pine.common.math.vector.VectorFloat;
import dev.prozilla.pine.common.property.FloatProperty;

/**
 * A vector property with 3 dimensions.
 */
public interface Vector3fPropertyBase<T extends VectorFloat<T>> extends Vector2fPropertyBase<T> {
	
	default FloatProperty zProperty() {
		return this::getZ;
	}
	
	/**
	 * Returns the z value of the current vector, or {@code 0} if the vector is {@code null}.
	 * @return The z value of the current vector.
	 */
	float getZ();

}
