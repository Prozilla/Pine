package dev.prozilla.pine.common.property.adaptive;

import dev.prozilla.pine.common.math.dimension.DimensionBase;
import dev.prozilla.pine.common.property.DimensionProperty;
import dev.prozilla.pine.common.property.VariableProperty;
import dev.prozilla.pine.common.property.animated.AnimatedDimensionProperty;
import dev.prozilla.pine.core.component.canvas.RectTransform;

import java.util.Objects;

public final class AdaptiveDimensionProperty extends VariableProperty<Integer> implements DimensionProperty {
	
	private final AnimatedDimensionProperty animatedProperty;
	private final DimensionBase fixedDimension;
	
	private RectTransform context;
	private boolean isHorizontal;
	
	public AdaptiveDimensionProperty(AnimatedDimensionProperty animatedProperty) {
		this.animatedProperty = Objects.requireNonNull(animatedProperty, "animatedProperty must not be null");
		fixedDimension = null;
	}
	
	public AdaptiveDimensionProperty(DimensionBase dimension) {
		this.fixedDimension = Objects.requireNonNull(dimension, "dimension must not be null");
		animatedProperty = null;
	}
	
	public void restartAnimation() {
		if (animatedProperty != null) {
			animatedProperty.restart();
		}
	}
	
	public void updateAnimation(float deltaTime) {
		if (animatedProperty != null) {
			animatedProperty.update(deltaTime);
		}
	}
	
	@Override
	public void setContext(RectTransform context, boolean isHorizontal) {
		this.context = context;
		this.isHorizontal = isHorizontal;
	}
	
	@Override
	public Integer getValue() {
		if (animatedProperty != null) {
			return animatedProperty.getValue(context, isHorizontal);
		} else if (fixedDimension != null) {
			return fixedDimension.compute(context, isHorizontal);
		} else {
			return 0;
		}
	}
	
	public boolean isAnimated() {
		return animatedProperty != null;
	}
}
