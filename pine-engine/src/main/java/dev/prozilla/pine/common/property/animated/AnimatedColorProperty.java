package dev.prozilla.pine.common.property.animated;

import dev.prozilla.pine.common.math.Easing;
import dev.prozilla.pine.common.property.ColorProperty;
import dev.prozilla.pine.common.system.resource.Color;

public class AnimatedColorProperty extends AnimatedProperty<Color> implements ColorProperty {
	
	public AnimatedColorProperty(Color start, Color end, float duration) {
		this(start, end, duration, AnimatedProperty.DEFAULT_EASING);
	}
	
	public AnimatedColorProperty(Color start, Color end, float duration, Easing easing) {
		super(start, end, duration, easing);
	}
	
	@Override
	public Color getValue() {
		return getColor();
	}
	
	@Override
	public void apply(Color outputColor) {
		float factor = easing.get(time / duration);
		outputColor.copyFrom(start).mix(end, factor);
	}
}
