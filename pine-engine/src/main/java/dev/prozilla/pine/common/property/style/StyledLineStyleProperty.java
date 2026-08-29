package dev.prozilla.pine.common.property.style;

import dev.prozilla.pine.common.property.adaptive.AdaptiveObjectProperty;
import dev.prozilla.pine.common.property.animated.AnimationCurve;
import dev.prozilla.pine.common.property.animated.transitioned.TransitionedObjectProperty;
import dev.prozilla.pine.core.component.ui.Node;
import dev.prozilla.pine.core.component.ui.style.LineStyle;

import java.util.List;

public final class StyledLineStyleProperty extends StyledObjectProperty<LineStyle> {
	
	public StyledLineStyleProperty(StyledPropertyKey<LineStyle> name, Node node, List<StyleRule<LineStyle>> styleRules, AdaptiveObjectProperty<LineStyle> defaultValue) {
		this(name, node, styleRules, defaultValue, null);
	}
	
	public StyledLineStyleProperty(StyledPropertyKey<LineStyle> name, Node node, List<StyleRule<LineStyle>> styleRules, AdaptiveObjectProperty<LineStyle> defaultValue, List<StyleRule<AnimationCurve>> transitionRules) {
		super(name, node, styleRules, defaultValue, transitionRules);
	}
	
	@Override
	protected TransitionedObjectProperty<LineStyle> createTransitionedProperty(LineStyle initialValue, AnimationCurve curve) {
		return null;
	}
	
	@Override
	public boolean supportsTransitions() {
		return false;
	}
	
}
