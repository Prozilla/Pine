package dev.prozilla.pine.common.property;

import dev.prozilla.pine.core.component.canvas.RectTransform;

public interface DimensionProperty {
	
	default Integer getValue(RectTransform context, boolean isHorizontal) {
		setContext(context, isHorizontal);
		return getValue();
	}
	
	Integer getValue();
	
	void setContext(RectTransform context, boolean isHorizontal);
	
}
