package dev.prozilla.pine.common.property.style;

import dev.prozilla.pine.common.math.vector.Anchor;
import dev.prozilla.pine.common.property.adaptive.AdaptiveObjectProperty;
import dev.prozilla.pine.common.property.animated.AnimationCurve;
import dev.prozilla.pine.common.property.animated.transitioned.TransitionedObjectProperty;
import dev.prozilla.pine.core.component.ui.Node;

import java.util.List;

public final class StyledGridAlignmentProperty extends StyledObjectProperty<Anchor> {
	
	public StyledGridAlignmentProperty(StyledPropertyKey<Anchor> name, Node node, List<StyleRule<Anchor>> styleRules, AdaptiveObjectProperty<Anchor> defaultValue) {
		this(name, node, styleRules, defaultValue, null);
	}
	
	public StyledGridAlignmentProperty(StyledPropertyKey<Anchor> name, Node node, List<StyleRule<Anchor>> styleRules, AdaptiveObjectProperty<Anchor> defaultValue, List<StyleRule<AnimationCurve>> transitionRules) {
		super(name, node, styleRules, defaultValue, transitionRules);
	}
	
	@Override
	protected TransitionedObjectProperty<Anchor> createTransitionedProperty(Anchor initialValue, AnimationCurve curve) {
		return null;
	}
	
	@Override
	public boolean supportsTransitions() {
		return false;
	}
	
}
