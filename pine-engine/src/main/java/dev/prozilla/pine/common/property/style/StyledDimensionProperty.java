package dev.prozilla.pine.common.property.style;

import dev.prozilla.pine.common.math.dimension.DimensionBase;
import dev.prozilla.pine.common.property.adaptive.AdaptiveObjectProperty;
import dev.prozilla.pine.common.property.animated.AnimationCurve;
import dev.prozilla.pine.common.property.animated.transitioned.TransitionedDimensionProperty;
import dev.prozilla.pine.core.component.ui.Node;

import java.util.List;

public final class StyledDimensionProperty extends StyledObjectProperty<DimensionBase> {
	
	public StyledDimensionProperty(StyledPropertyKey<DimensionBase> name, Node node, List<StyleRule<DimensionBase>> styleRules, AdaptiveObjectProperty<DimensionBase> defaultValue) {
		this(name, node, styleRules, defaultValue, null);
	}
	
	public StyledDimensionProperty(StyledPropertyKey<DimensionBase> name, Node node, List<StyleRule<DimensionBase>> styleRules, AdaptiveObjectProperty<DimensionBase> defaultValue, List<StyleRule<AnimationCurve>> transitionRules) {
		super(name, node, styleRules, defaultValue, transitionRules);
	}
	
	@Override
	protected TransitionedDimensionProperty createTransitionedProperty(DimensionBase initialValue, AnimationCurve curve) {
		return new TransitionedDimensionProperty(initialValue, curve);
	}
	
}
