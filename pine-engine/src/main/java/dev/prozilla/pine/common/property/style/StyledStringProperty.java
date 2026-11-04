package dev.prozilla.pine.common.property.style;

import dev.prozilla.pine.common.asset.style.StyleRule;
import dev.prozilla.pine.common.asset.style.StyledPropertyKey;
import dev.prozilla.pine.common.property.adaptive.AdaptiveObjectProperty;
import dev.prozilla.pine.common.property.animated.AnimationCurve;
import dev.prozilla.pine.common.property.animated.transitioned.TransitionedObjectProperty;
import dev.prozilla.pine.core.component.ui.Node;

import java.util.List;

public final class StyledStringProperty extends StyledObjectProperty<String> {
	
	public StyledStringProperty(StyledPropertyKey<String> name, Node node, List<StyleRule<String>> styleRules, AdaptiveObjectProperty<String> defaultValue) {
		this(name, node, styleRules, defaultValue, null);
	}
	
	public StyledStringProperty(StyledPropertyKey<String> name, Node node, List<StyleRule<String>> styleRules, AdaptiveObjectProperty<String> defaultValue, List<StyleRule<AnimationCurve>> transitionRules) {
		super(name, node, styleRules, defaultValue, transitionRules);
	}
	
	@Override
	protected TransitionedObjectProperty<String> createTransitionedProperty(String initialValue, AnimationCurve curve) {
		return null;
	}
	
	@Override
	public boolean supportsTransitions() {
		return false;
	}
	
}
