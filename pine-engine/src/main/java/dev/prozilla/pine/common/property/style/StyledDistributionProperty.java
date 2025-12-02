package dev.prozilla.pine.common.property.style;

import dev.prozilla.pine.common.property.adaptive.AdaptiveObjectProperty;
import dev.prozilla.pine.common.property.animated.AnimationCurve;
import dev.prozilla.pine.common.property.animated.transitioned.TransitionedObjectProperty;
import dev.prozilla.pine.core.component.ui.LayoutNode;
import dev.prozilla.pine.core.component.ui.Node;

import java.util.List;

public final class StyledDistributionProperty extends StyledObjectProperty<LayoutNode.Distribution> {
	
	public StyledDistributionProperty(StyledPropertyKey<LayoutNode.Distribution> name, Node node, List<StyleRule<LayoutNode.Distribution>> styleRules, AdaptiveObjectProperty<LayoutNode.Distribution> defaultValue) {
		this(name, node, styleRules, defaultValue, null);
	}
	
	public StyledDistributionProperty(StyledPropertyKey<LayoutNode.Distribution> name, Node node, List<StyleRule<LayoutNode.Distribution>> styleRules, AdaptiveObjectProperty<LayoutNode.Distribution> defaultValue, List<StyleRule<AnimationCurve>> transitionRules) {
		super(name, node, styleRules, defaultValue, transitionRules);
	}
	
	@Override
	protected TransitionedObjectProperty<LayoutNode.Distribution> createTransitionedProperty(LayoutNode.Distribution initialValue, AnimationCurve curve) {
		return null;
	}
	
	@Override
	public boolean supportsTransitions() {
		return false;
	}
	
}
