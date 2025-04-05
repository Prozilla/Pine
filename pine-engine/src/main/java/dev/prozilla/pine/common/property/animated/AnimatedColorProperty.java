package dev.prozilla.pine.common.property.animated;

import dev.prozilla.pine.common.math.easing.EasingFunction;
import dev.prozilla.pine.common.property.ColorProperty;
import dev.prozilla.pine.common.system.resource.Color;

public class AnimatedColorProperty extends AnimatedProperty<Color> implements ColorProperty {
	
	public AnimatedColorProperty(Color start, Color end, float duration) {
		this(start, end, duration, AnimatedProperty.DEFAULT_EASING_FUNCTION);
	}
	
	public AnimatedColorProperty(Color start, Color end, float duration, EasingFunction easingFunction) {
		this(start, end, duration, easingFunction, DEFAULT_DIRECTION);
	}
	
	public AnimatedColorProperty(Color start, Color end, float duration, EasingFunction easingFunction, AnimationDirection direction) {
		super(start, end, duration, easingFunction, direction);
	}
	
	@Override
	public Color getValue() {
		return getColor();
	}
	
	public void applyUpdatedValue(float deltaTime, Color outputColor) {
		update(deltaTime);
		transmit(outputColor);
	}
	
	@Override
	public void transmit(Color target) {
		target.receive(start);
		target.mix(end, getFactor());
	}
}
