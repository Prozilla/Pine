package dev.prozilla.pine.common.property.style;

import dev.prozilla.pine.common.property.VariableProperty;
import dev.prozilla.pine.common.property.adaptive.AdaptiveObjectProperty;
import dev.prozilla.pine.common.property.adaptive.AdaptiveProperty;
import dev.prozilla.pine.common.property.animated.AnimationCurve;
import dev.prozilla.pine.common.property.animated.transitioned.TransitionedProperty;
import dev.prozilla.pine.core.component.ui.Node;

import java.util.List;

public final class StyledObjectProperty extends StyledProperty<Object> {
	
	public StyledObjectProperty(StyledPropertyKey<Object> name, Node node, List<StyleRule<Object>> styleRules, AdaptiveProperty<Object> defaultValue) {
		this(name, node, styleRules, defaultValue, null);
	}
	
	public StyledObjectProperty(StyledPropertyKey<Object> name, Node node, List<StyleRule<Object>> styleRules, AdaptiveProperty<Object> defaultValue, List<StyleRule<AnimationCurve>> transitionRules) {
		super(name, node, styleRules, defaultValue, transitionRules);
	}
	
	@Override
	protected AdaptiveProperty<Object> createAdaptiveProperty(Object value) {
		return AdaptiveObjectProperty.adapt(value);
	}
	
	@Override
	protected AdaptiveProperty<Object> createAdaptiveProperty(VariableProperty<Object> property) {
		return AdaptiveObjectProperty.adapt(property);
	}
	
	@Override
	protected TransitionedProperty<Object> createTransitionedProperty(Object initialValue, AnimationCurve curve) {
		return null;
	}
	
}
