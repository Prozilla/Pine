package dev.prozilla.pine.common.property.animated;

import dev.prozilla.pine.common.math.dimension.DimensionBase;
import dev.prozilla.pine.common.math.easing.EasingFunction;
import dev.prozilla.pine.common.property.DimensionProperty;
import dev.prozilla.pine.core.component.canvas.RectTransform;

import java.util.Objects;

public class AnimatedDimensionProperty extends AnimatedIntProperty implements DimensionProperty {
	
	protected final DimensionBase startDimension;
	protected final DimensionBase endDimension;
	
	protected RectTransform context;
	protected boolean isHorizontal;
	
	public AnimatedDimensionProperty(DimensionBase start, DimensionBase end, float duration) {
		this(start, end, duration, DEFAULT_EASING_FUNCTION);
	}
	
	public AnimatedDimensionProperty(DimensionBase start, DimensionBase end, float duration, EasingFunction easingFunction) {
		super(0, 0, duration, easingFunction);
		this.startDimension = Objects.requireNonNull(start, "start must not be null");
		this.endDimension = Objects.requireNonNull(end, "end must not be null");
	}
	
	@Override
	public void setContext(RectTransform context, boolean isHorizontal) {
		this.context = context;
		this.isHorizontal = isHorizontal;
	}
	
	@Override
	public Integer getValue() {
		if (context == null) {
			return 0;
		}
		
		start = startDimension.compute(context, isHorizontal);
		end = endDimension.compute(context, isHorizontal);
		return super.getValue();
	}
}
