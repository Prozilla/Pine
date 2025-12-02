package dev.prozilla.pine.common.property.animated.transitioned;

import dev.prozilla.pine.common.property.ColorProperty;
import dev.prozilla.pine.common.property.animated.AnimationCurve;
import dev.prozilla.pine.common.system.Color;

public class TransitionedColorProperty extends TransitionedObjectProperty<Color> implements ColorProperty {
	
	public TransitionedColorProperty(Color initialValue, AnimationCurve curve) {
		super(initialValue, curve);
	}
	
	@Override
	public void transmit(Color target) {
		target.receive(start);
		target.mix(end, getFactor());
	}
	
	@Override
	public Color getValue() {
		return getColor();
	}
	
}
