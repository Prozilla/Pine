package dev.prozilla.pine.common.property.style;

import dev.prozilla.pine.common.math.vector.Alignment;
import dev.prozilla.pine.common.property.adaptive.AdaptiveObjectProperty;
import dev.prozilla.pine.common.property.animated.AnimationCurve;
import dev.prozilla.pine.common.property.animated.transitioned.TransitionedObjectProperty;
import dev.prozilla.pine.core.component.ui.Node;

import java.util.List;

public final class StyledEdgeAlignmentProperty extends StyledObjectProperty<Alignment> {
	
	public StyledEdgeAlignmentProperty(StyledPropertyKey<Alignment> name, Node node, List<StyleRule<Alignment>> styleRules, AdaptiveObjectProperty<Alignment> defaultValue) {
		this(name, node, styleRules, defaultValue, null);
	}
	
	public StyledEdgeAlignmentProperty(StyledPropertyKey<Alignment> name, Node node, List<StyleRule<Alignment>> styleRules, AdaptiveObjectProperty<Alignment> defaultValue, List<StyleRule<AnimationCurve>> transitionRules) {
		super(name, node, styleRules, defaultValue, transitionRules);
	}
	
	@Override
	protected TransitionedObjectProperty<Alignment> createTransitionedProperty(Alignment initialValue, AnimationCurve curve) {
		return null;
	}
	
	@Override
	public boolean supportsTransitions() {
		return false;
	}
	
}
