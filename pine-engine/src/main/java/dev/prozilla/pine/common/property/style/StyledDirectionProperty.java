package dev.prozilla.pine.common.property.style;

import dev.prozilla.pine.common.math.vector.Direction;
import dev.prozilla.pine.common.property.VariableProperty;
import dev.prozilla.pine.common.property.adaptive.AdaptiveDirectionProperty;
import dev.prozilla.pine.common.property.adaptive.AdaptiveProperty;
import dev.prozilla.pine.common.property.adaptive.AdaptivePropertyBase;
import dev.prozilla.pine.common.property.animated.AnimationCurve;
import dev.prozilla.pine.common.property.animated.transitioned.TransitionedProperty;
import dev.prozilla.pine.core.component.ui.Node;

import java.util.List;

public final class StyledDirectionProperty extends StyledProperty<Direction> {
	
	public StyledDirectionProperty(StyledPropertyKey<Direction> name, Node node, List<StyleRule<Direction>> styleRules, AdaptivePropertyBase<Direction> defaultValue) {
		this(name, node, styleRules, defaultValue, null);
	}
	
	public StyledDirectionProperty(StyledPropertyKey<Direction> name, Node node, List<StyleRule<Direction>> styleRules, AdaptivePropertyBase<Direction> defaultValue, List<StyleRule<AnimationCurve>> transitionRules) {
		super(name, node, styleRules, defaultValue, transitionRules);
	}
	
	@Override
	protected AdaptiveProperty<Direction> createAdaptiveProperty(Direction value) {
		return AdaptiveDirectionProperty.adapt(value);
	}
	
	@Override
	protected AdaptiveProperty<Direction> createAdaptiveProperty(VariableProperty<Direction> property) {
		return AdaptiveDirectionProperty.adapt(property);
	}
	
	@Override
	protected TransitionedProperty<Direction> createTransitionedProperty(Direction initialValue, AnimationCurve curve) {
		return null;
	}
	
	@Override
	public boolean supportsTransitions() {
		return false;
	}
	
}
