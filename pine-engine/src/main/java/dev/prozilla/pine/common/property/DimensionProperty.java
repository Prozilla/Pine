package dev.prozilla.pine.common.property;

import dev.prozilla.pine.common.math.dimension.DimensionBase;
import dev.prozilla.pine.core.component.canvas.RectTransform;

/**
 * Utility methods for dimension properties.
 */
public interface DimensionProperty {
	
	default int compute(RectTransform context, boolean isHorizontal) {
		return getValue().compute(context, isHorizontal);
	}
	
	default boolean isDirty(RectTransform context, boolean isHorizontal) {
		return getValue().isDirty(context, isHorizontal);
	}
	
	DimensionBase getValue();
	
}
