package dev.prozilla.pine.common.property.style;

import dev.prozilla.pine.common.math.vector.GridAlignment;
import dev.prozilla.pine.common.property.VariableProperty;
import dev.prozilla.pine.common.property.adaptive.AdaptiveGridAlignmentProperty;
import dev.prozilla.pine.common.property.adaptive.AdaptiveProperty;
import dev.prozilla.pine.common.property.adaptive.AdaptivePropertyBase;
import dev.prozilla.pine.common.property.animated.AnimationCurve;
import dev.prozilla.pine.common.property.animated.transitioned.TransitionedProperty;
import dev.prozilla.pine.core.component.ui.Node;

import java.util.List;

public final class StyledGridAlignmentProperty extends StyledProperty<GridAlignment> {
	
	public StyledGridAlignmentProperty(StyledPropertyKey<GridAlignment> name, Node node, List<StyleRule<GridAlignment>> styleRules, AdaptivePropertyBase<GridAlignment> defaultValue) {
		this(name, node, styleRules, defaultValue, null);
	}
	
	public StyledGridAlignmentProperty(StyledPropertyKey<GridAlignment> name, Node node, List<StyleRule<GridAlignment>> styleRules, AdaptivePropertyBase<GridAlignment> defaultValue, List<StyleRule<AnimationCurve>> transitionRules) {
		super(name, node, styleRules, defaultValue, transitionRules);
	}
	
	@Override
	protected AdaptiveProperty<GridAlignment> createAdaptiveProperty(GridAlignment value) {
		return AdaptiveGridAlignmentProperty.adapt(value);
	}
	
	@Override
	protected AdaptiveProperty<GridAlignment> createAdaptiveProperty(VariableProperty<GridAlignment> property) {
		return AdaptiveGridAlignmentProperty.adapt(property);
	}
	
	@Override
	protected TransitionedProperty<GridAlignment> createTransitionedProperty(GridAlignment initialValue, AnimationCurve curve) {
		return null;
	}
	
	@Override
	public boolean supportsTransitions() {
		return false;
	}
	
}
