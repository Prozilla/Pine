package dev.prozilla.pine.common.property.style;

import dev.prozilla.pine.common.math.vector.GridAlignment;
import dev.prozilla.pine.common.property.adaptive.AdaptiveObjectProperty;
import dev.prozilla.pine.common.property.animated.AnimationCurve;
import dev.prozilla.pine.common.property.animated.transitioned.TransitionedObjectProperty;
import dev.prozilla.pine.core.component.ui.Node;

import java.util.List;

public final class StyledGridAlignmentProperty extends StyledObjectProperty<GridAlignment> {
	
	public StyledGridAlignmentProperty(StyledPropertyKey<GridAlignment> name, Node node, List<StyleRule<GridAlignment>> styleRules, AdaptiveObjectProperty<GridAlignment> defaultValue) {
		this(name, node, styleRules, defaultValue, null);
	}
	
	public StyledGridAlignmentProperty(StyledPropertyKey<GridAlignment> name, Node node, List<StyleRule<GridAlignment>> styleRules, AdaptiveObjectProperty<GridAlignment> defaultValue, List<StyleRule<AnimationCurve>> transitionRules) {
		super(name, node, styleRules, defaultValue, transitionRules);
	}
	
	@Override
	protected TransitionedObjectProperty<GridAlignment> createTransitionedProperty(GridAlignment initialValue, AnimationCurve curve) {
		return null;
	}
	
	@Override
	public boolean supportsTransitions() {
		return false;
	}
	
}
