package dev.prozilla.pine.common.property.style;

import dev.prozilla.pine.common.property.ColorProperty;
import dev.prozilla.pine.common.property.adaptive.AdaptiveProperty;
import dev.prozilla.pine.common.property.animated.AnimationCurve;
import dev.prozilla.pine.common.property.animated.transitioned.TransitionedColorProperty;
import dev.prozilla.pine.common.system.resource.Color;
import dev.prozilla.pine.core.component.canvas.RectTransform;

import java.util.List;

public final class StyledColorProperty extends StyledProperty<Color> implements ColorProperty {
	
	public StyledColorProperty(StyledPropertyName name, RectTransform context, List<StyleRule<Color>> styleRules, AdaptiveProperty<Color> defaultValue) {
		this(name, context, styleRules, defaultValue, null);
	}
	
	public StyledColorProperty(StyledPropertyName name, RectTransform context, List<StyleRule<Color>> styleRules, AdaptiveProperty<Color> defaultValue, List<StyleRule<AnimationCurve>> transitionRules) {
		super(name, context, styleRules, defaultValue, transitionRules);
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
