package dev.prozilla.pine.common.property.style;

import dev.prozilla.pine.common.property.adaptive.AdaptiveObjectProperty;
import dev.prozilla.pine.common.property.animated.AnimationCurve;
import dev.prozilla.pine.common.property.animated.transitioned.TransitionedObjectProperty;
import dev.prozilla.pine.core.component.ui.Node;
import dev.prozilla.pine.core.component.ui.style.BorderStyle;

import java.util.List;

public final class StyledBorderStyleProperty extends StyledObjectProperty<BorderStyle> {
	
	public StyledBorderStyleProperty(StyledPropertyKey<BorderStyle> name, Node node, List<StyleRule<BorderStyle>> styleRules, AdaptiveObjectProperty<BorderStyle> defaultValue) {
		this(name, node, styleRules, defaultValue, null);
	}
	
	public StyledBorderStyleProperty(StyledPropertyKey<BorderStyle> name, Node node, List<StyleRule<BorderStyle>> styleRules, AdaptiveObjectProperty<BorderStyle> defaultValue, List<StyleRule<AnimationCurve>> transitionRules) {
		super(name, node, styleRules, defaultValue, transitionRules);
	}
	
	@Override
	protected TransitionedObjectProperty<BorderStyle> createTransitionedProperty(BorderStyle initialValue, AnimationCurve curve) {
		return null;
	}
	
	@Override
	public boolean supportsTransitions() {
		return false;
	}
	
}
