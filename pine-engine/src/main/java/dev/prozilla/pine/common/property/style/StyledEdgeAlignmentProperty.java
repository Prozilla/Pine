package dev.prozilla.pine.common.property.style;

import dev.prozilla.pine.common.math.vector.EdgeAlignment;
import dev.prozilla.pine.common.property.adaptive.AdaptiveObjectProperty;
import dev.prozilla.pine.common.property.animated.AnimationCurve;
import dev.prozilla.pine.common.property.animated.transitioned.TransitionedObjectProperty;
import dev.prozilla.pine.core.component.ui.Node;

import java.util.List;

public final class StyledEdgeAlignmentProperty extends StyledObjectProperty<EdgeAlignment> {
	
	public StyledEdgeAlignmentProperty(StyledPropertyKey<EdgeAlignment> name, Node node, List<StyleRule<EdgeAlignment>> styleRules, AdaptiveObjectProperty<EdgeAlignment> defaultValue) {
		this(name, node, styleRules, defaultValue, null);
	}
	
	public StyledEdgeAlignmentProperty(StyledPropertyKey<EdgeAlignment> name, Node node, List<StyleRule<EdgeAlignment>> styleRules, AdaptiveObjectProperty<EdgeAlignment> defaultValue, List<StyleRule<AnimationCurve>> transitionRules) {
		super(name, node, styleRules, defaultValue, transitionRules);
	}
	
	@Override
	protected TransitionedObjectProperty<EdgeAlignment> createTransitionedProperty(EdgeAlignment initialValue, AnimationCurve curve) {
		return null;
	}
	
	@Override
	public boolean supportsTransitions() {
		return false;
	}
	
}
