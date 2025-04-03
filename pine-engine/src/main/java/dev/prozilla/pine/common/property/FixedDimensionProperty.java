package dev.prozilla.pine.common.property;

import dev.prozilla.pine.common.math.dimension.DimensionBase;
import dev.prozilla.pine.core.component.canvas.RectTransform;

public class FixedDimensionProperty extends VariableProperty<Integer> implements DimensionProperty {
	
	protected final DimensionBase dimension;
	
	protected RectTransform context;
	protected boolean isHorizontal;
	
	public FixedDimensionProperty(DimensionBase dimension) {
		this.dimension = dimension;
	}
	
	@Override
	public void setContext(RectTransform context, boolean isHorizontal) {
		this.context = context;
		this.isHorizontal = isHorizontal;
	}
	
	@Override
	public Integer getValue() {
		return dimension.compute(context, isHorizontal);
	}
}
