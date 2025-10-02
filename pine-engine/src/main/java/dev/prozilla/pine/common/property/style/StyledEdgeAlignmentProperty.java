package dev.prozilla.pine.common.property.style;

import dev.prozilla.pine.common.math.vector.EdgeAlignment;
import dev.prozilla.pine.common.property.Property;
import dev.prozilla.pine.common.property.adaptive.AdaptiveEdgeAlignmentProperty;
import dev.prozilla.pine.common.property.adaptive.AdaptiveProperty;
import dev.prozilla.pine.common.property.adaptive.AdaptivePropertyBase;
import dev.prozilla.pine.common.property.animated.AnimationCurve;
import dev.prozilla.pine.common.property.animated.transitioned.TransitionedProperty;
import dev.prozilla.pine.core.component.ui.Node;

import java.util.List;

public final class StyledEdgeAlignmentProperty extends StyledProperty<EdgeAlignment> {
	
	public StyledEdgeAlignmentProperty(StyledPropertyKey<EdgeAlignment> name, Node node, List<StyleRule<EdgeAlignment>> styleRules, AdaptivePropertyBase<EdgeAlignment> defaultValue) {
		this(name, node, styleRules, defaultValue, null);
	}
	
	public StyledEdgeAlignmentProperty(StyledPropertyKey<EdgeAlignment> name, Node node, List<StyleRule<EdgeAlignment>> styleRules, AdaptivePropertyBase<EdgeAlignment> defaultValue, List<StyleRule<AnimationCurve>> transitionRules) {
		super(name, node, styleRules, defaultValue, transitionRules);
	}
	
	@Override
	protected AdaptiveProperty<EdgeAlignment> createAdaptiveProperty(EdgeAlignment value) {
		return AdaptiveEdgeAlignmentProperty.adapt(value);
	}
	
	@Override
	protected AdaptiveProperty<EdgeAlignment> createAdaptiveProperty(Property<EdgeAlignment> property) {
		return AdaptiveEdgeAlignmentProperty.adapt(property);
	}
	
	@Override
	protected TransitionedProperty<EdgeAlignment> createTransitionedProperty(EdgeAlignment initialValue, AnimationCurve curve) {
		return null;
	}
	
	@Override
	public boolean supportsTransitions() {
		return false;
	}
	
}
