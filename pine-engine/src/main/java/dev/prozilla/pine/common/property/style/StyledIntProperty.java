package dev.prozilla.pine.common.property.style;

import dev.prozilla.pine.common.property.IntProperty;
import dev.prozilla.pine.common.property.Property;
import dev.prozilla.pine.common.property.adaptive.AdaptiveIntProperty;
import dev.prozilla.pine.common.property.animated.AnimationCurve;
import dev.prozilla.pine.common.property.animated.transitioned.TransitionedIntProperty;
import dev.prozilla.pine.common.util.ObjectUtils;
import dev.prozilla.pine.core.component.ui.Node;

import java.util.List;

public final class StyledIntProperty extends StyledProperty<Integer, IntProperty, AdaptiveIntProperty, TransitionedIntProperty> implements IntProperty {
	
	public StyledIntProperty(StyledPropertyKey<Integer> name, Node node, List<StyleRule<Integer>> styleRules, AdaptiveIntProperty defaultValue) {
		this(name, node, styleRules, defaultValue, null);
	}
	
	public StyledIntProperty(StyledPropertyKey<Integer> name, Node node, List<StyleRule<Integer>> styleRules, AdaptiveIntProperty defaultValue, List<StyleRule<AnimationCurve>> transitionRules) {
		super(name, node, styleRules, defaultValue, transitionRules);
	}
	
	@Override
	protected AdaptiveIntProperty createAdaptiveProperty(Integer value) {
		return AdaptiveIntProperty.adapt(ObjectUtils.unbox(value));
	}
	
	@Override
	protected AdaptiveIntProperty createAdaptiveProperty(Property<Integer> property) {
		if (property instanceof IntProperty intProperty) {
			return AdaptiveIntProperty.adapt(intProperty);
		} else {
			return AdaptiveIntProperty.adapt(IntProperty.fromProperty(property));
		}
	}
	
	@Override
	protected TransitionedIntProperty createTransitionedProperty(Integer initialValue, AnimationCurve curve) {
		return new TransitionedIntProperty(ObjectUtils.unbox(initialValue), curve);
	}
	
	@Override
	public int get() {
		return adaptiveProperty.get();
	}
	
}
