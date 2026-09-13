package dev.prozilla.pine.common.property.style;

import dev.prozilla.pine.common.property.adaptive.AdaptiveObjectProperty;
import dev.prozilla.pine.common.property.animated.AnimationCurve;
import dev.prozilla.pine.common.property.animated.transitioned.TransitionedObjectProperty;
import dev.prozilla.pine.core.component.ui.Node;
import dev.prozilla.pine.core.state.input.CursorType;

import java.util.List;

public final class StyledCursorProperty extends StyledObjectProperty<CursorType> {
	
	public StyledCursorProperty(StyledPropertyKey<CursorType> name, Node node, List<StyleRule<CursorType>> styleRules, AdaptiveObjectProperty<CursorType> defaultValue) {
		this(name, node, styleRules, defaultValue, null);
	}
	
	public StyledCursorProperty(StyledPropertyKey<CursorType> name, Node node, List<StyleRule<CursorType>> styleRules, AdaptiveObjectProperty<CursorType> defaultValue, List<StyleRule<AnimationCurve>> transitionRules) {
		super(name, node, styleRules, defaultValue, transitionRules);
	}
	
	@Override
	protected TransitionedObjectProperty<CursorType> createTransitionedProperty(CursorType initialValue, AnimationCurve curve) {
		return null;
	}
	
	@Override
	public boolean supportsTransitions() {
		return false;
	}
	
}
