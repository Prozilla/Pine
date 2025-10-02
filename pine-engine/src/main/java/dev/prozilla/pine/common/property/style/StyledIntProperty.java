package dev.prozilla.pine.common.property.style;

import dev.prozilla.pine.common.property.Property;
import dev.prozilla.pine.common.property.adaptive.AdaptiveIntProperty;
import dev.prozilla.pine.common.property.adaptive.AdaptivePropertyBase;
import dev.prozilla.pine.common.property.animated.AnimationCurve;
import dev.prozilla.pine.common.property.animated.transitioned.TransitionedIntProperty;
import dev.prozilla.pine.core.component.ui.Node;

import java.util.List;

public final class StyledIntProperty extends StyledProperty<Integer> {
	
	public StyledIntProperty(StyledPropertyKey<Integer> name, Node node, List<StyleRule<Integer>> styleRules, AdaptivePropertyBase<Integer> defaultValue) {
		this(name, node, styleRules, defaultValue, null);
	}
	
	public StyledIntProperty(StyledPropertyKey<Integer> name, Node node, List<StyleRule<Integer>> styleRules, AdaptivePropertyBase<Integer> defaultValue, List<StyleRule<AnimationCurve>> transitionRules) {
		super(name, node, styleRules, defaultValue, transitionRules);
	}
	
	@Override
	protected AdaptivePropertyBase<Integer> createAdaptiveProperty(Integer value) {
		return AdaptiveIntProperty.adapt(value);
	}
	
	@Override
	protected AdaptivePropertyBase<Integer> createAdaptiveProperty(Property<Integer> property) {
		return AdaptiveIntProperty.adapt(property);
	}
	
	@Override
	protected TransitionedIntProperty createTransitionedProperty(Integer initialValue, AnimationCurve curve) {
		return new TransitionedIntProperty(initialValue, curve);
	}
	
}
