package dev.prozilla.pine.common.property.style;

import dev.prozilla.pine.common.property.Property;
import dev.prozilla.pine.common.property.adaptive.AdaptiveObjectProperty;
import dev.prozilla.pine.common.property.animated.AnimationCurve;
import dev.prozilla.pine.common.property.animated.transitioned.TransitionedObjectProperty;
import dev.prozilla.pine.core.component.ui.Node;

import java.util.List;

public abstract class StyledObjectProperty<T> extends StyledProperty<T, Property<T>, AdaptiveObjectProperty<T>, TransitionedObjectProperty<T>> {
	
	public StyledObjectProperty(StyledPropertyKey<T> name, Node node, List<StyleRule<T>> styleRules, AdaptiveObjectProperty<T> defaultValue) {
		this(name, node, styleRules, defaultValue, null);
	}
	
	public StyledObjectProperty(StyledPropertyKey<T> name, Node node, List<StyleRule<T>> styleRules, AdaptiveObjectProperty<T> defaultValue, List<StyleRule<AnimationCurve>> transitionRules) {
		super(name, node, styleRules, defaultValue, transitionRules);
	}
	
	@Override
	protected AdaptiveObjectProperty<T> createAdaptiveProperty(T value) {
		return AdaptiveObjectProperty.adapt(value);
	}
	
	@Override
	protected AdaptiveObjectProperty<T> createAdaptiveProperty(Property<T> property) {
		return AdaptiveObjectProperty.adapt(property);
	}
	
}
