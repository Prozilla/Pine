package dev.prozilla.pine.common.property.style;

import dev.prozilla.pine.common.property.ColorProperty;
import dev.prozilla.pine.common.property.adaptive.AdaptiveColorProperty;
import dev.prozilla.pine.common.property.animated.AnimationCurve;
import dev.prozilla.pine.common.property.animated.transitioned.TransitionedColorProperty;
import dev.prozilla.pine.common.system.resource.Color;
import dev.prozilla.pine.core.component.canvas.RectTransform;

import java.util.List;

public class StyleColorProperty extends StyleProperty<Color> implements ColorProperty {
	
	public StyleColorProperty(RectTransform context, List<StyleRule<Color>> styleRules, AdaptiveColorProperty adaptiveProperty) {
		super(context, styleRules, adaptiveProperty);
	}
	
	@Override
	public void transmit(Color target) {
		target.receive(getValue());
	}
	
	@Override
	protected TransitionedColorProperty createTransitionedProperty(Color initialValue, AnimationCurve curve) {
		return new TransitionedColorProperty(initialValue, curve);
	}
	
}
