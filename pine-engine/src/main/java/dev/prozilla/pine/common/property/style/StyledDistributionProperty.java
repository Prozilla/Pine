package dev.prozilla.pine.common.property.style;

import dev.prozilla.pine.common.property.Property;
import dev.prozilla.pine.common.property.adaptive.AdaptiveDistributionProperty;
import dev.prozilla.pine.common.property.adaptive.AdaptiveProperty;
import dev.prozilla.pine.common.property.adaptive.AdaptivePropertyBase;
import dev.prozilla.pine.common.property.animated.AnimationCurve;
import dev.prozilla.pine.common.property.animated.transitioned.TransitionedProperty;
import dev.prozilla.pine.core.component.ui.LayoutNode;
import dev.prozilla.pine.core.component.ui.Node;

import java.util.List;

public final class StyledDistributionProperty extends StyledProperty<LayoutNode.Distribution> {
	
	public StyledDistributionProperty(StyledPropertyKey<LayoutNode.Distribution> name, Node node, List<StyleRule<LayoutNode.Distribution>> styleRules, AdaptivePropertyBase<LayoutNode.Distribution> defaultValue) {
		this(name, node, styleRules, defaultValue, null);
	}
	
	public StyledDistributionProperty(StyledPropertyKey<LayoutNode.Distribution> name, Node node, List<StyleRule<LayoutNode.Distribution>> styleRules, AdaptivePropertyBase<LayoutNode.Distribution> defaultValue, List<StyleRule<AnimationCurve>> transitionRules) {
		super(name, node, styleRules, defaultValue, transitionRules);
	}
	
	@Override
	protected AdaptiveProperty<LayoutNode.Distribution> createAdaptiveProperty(LayoutNode.Distribution value) {
		return AdaptiveDistributionProperty.adapt(value);
	}
	
	@Override
	protected AdaptiveProperty<LayoutNode.Distribution> createAdaptiveProperty(Property<LayoutNode.Distribution> property) {
		return AdaptiveDistributionProperty.adapt(property);
	}
	
	@Override
	protected TransitionedProperty<LayoutNode.Distribution> createTransitionedProperty(LayoutNode.Distribution initialValue, AnimationCurve curve) {
		return null;
	}
	
	@Override
	public boolean supportsTransitions() {
		return false;
	}
	
}
