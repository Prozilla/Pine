package dev.prozilla.pine.common.property.style;

import dev.prozilla.pine.common.property.ColorProperty;
import dev.prozilla.pine.common.property.VariableProperty;
import dev.prozilla.pine.common.property.adaptive.AdaptiveColorProperty;
import dev.prozilla.pine.common.property.adaptive.AdaptiveProperty;
import dev.prozilla.pine.common.property.adaptive.AdaptivePropertyBase;
import dev.prozilla.pine.common.property.animated.AnimationCurve;
import dev.prozilla.pine.common.property.animated.transitioned.TransitionedColorProperty;
import dev.prozilla.pine.common.system.resource.Color;
import dev.prozilla.pine.core.component.ui.Node;

import java.util.List;

public final class StyledColorProperty extends StyledProperty<Color> implements ColorProperty {
	
	public StyledColorProperty(StyledPropertyKey<Color> name, Node node, List<StyleRule<Color>> styleRules, AdaptivePropertyBase<Color> defaultValue) {
		this(name, node, styleRules, defaultValue, null);
	}
	
	public StyledColorProperty(StyledPropertyKey<Color> name, Node node, List<StyleRule<Color>> styleRules, AdaptivePropertyBase<Color> defaultValue, List<StyleRule<AnimationCurve>> transitionRules) {
		super(name, node, styleRules, defaultValue, transitionRules);
	}
	
	@Override
	public void transmit(Color target) {
		target.receive(getValue());
	}
	
	@Override
	protected AdaptiveProperty<Color> createAdaptiveProperty(Color value) {
		return AdaptiveColorProperty.adapt(value);
	}
	
	@Override
	protected AdaptiveProperty<Color> createAdaptiveProperty(VariableProperty<Color> property) {
		return AdaptiveColorProperty.adapt(property);
	}
	
	@Override
	protected TransitionedColorProperty createTransitionedProperty(Color initialValue, AnimationCurve curve) {
		return new TransitionedColorProperty(initialValue, curve);
	}
	
}
