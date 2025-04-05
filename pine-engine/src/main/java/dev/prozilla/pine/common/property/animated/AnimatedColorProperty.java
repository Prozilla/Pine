package dev.prozilla.pine.common.property.animated;

import dev.prozilla.pine.common.math.easing.EasingFunction;
import dev.prozilla.pine.common.property.ColorProperty;
import dev.prozilla.pine.common.system.resource.Color;

public class AnimatedColorProperty extends AnimatedProperty<Color> implements ColorProperty {
	
	public AnimatedColorProperty(Color start, Color end, float duration) {
		this(start, end, duration, AnimatedProperty.DEFAULT_EASING_FUNCTION);
	}
	
	public AnimatedColorProperty(Color start, Color end, float duration, EasingFunction easingFunction) {
		super(start, end, duration, easingFunction);
	}
	
	@Override
	public Color getValue() {
		return getColor();
	}
	
	public void applyUpdatedValue(float deltaTime, Color outputColor) {
		update(deltaTime);
		apply(outputColor);
	}
	
	@Override
	public void apply(Color target) {
		target.copyFrom(start).mix(end, getFactor());
	}
}
