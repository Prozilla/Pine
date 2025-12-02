package dev.prozilla.pine.common.property.style;

import dev.prozilla.pine.common.math.vector.Direction;
import dev.prozilla.pine.common.property.adaptive.AdaptiveObjectProperty;
import dev.prozilla.pine.common.property.animated.AnimationCurve;
import dev.prozilla.pine.common.property.animated.transitioned.TransitionedObjectProperty;
import dev.prozilla.pine.core.component.ui.Node;

import java.util.List;

public final class StyledDirectionProperty extends StyledObjectProperty<Direction> {
	
	public StyledDirectionProperty(StyledPropertyKey<Direction> name, Node node, List<StyleRule<Direction>> styleRules, AdaptiveObjectProperty<Direction> defaultValue) {
		this(name, node, styleRules, defaultValue, null);
	}
	
	public StyledDirectionProperty(StyledPropertyKey<Direction> name, Node node, List<StyleRule<Direction>> styleRules, AdaptiveObjectProperty<Direction> defaultValue, List<StyleRule<AnimationCurve>> transitionRules) {
		super(name, node, styleRules, defaultValue, transitionRules);
	}
	
	@Override
	protected TransitionedObjectProperty<Direction> createTransitionedProperty(Direction initialValue, AnimationCurve curve) {
		return null;
	}
	
	@Override
	public boolean supportsTransitions() {
		return false;
	}
	
}
