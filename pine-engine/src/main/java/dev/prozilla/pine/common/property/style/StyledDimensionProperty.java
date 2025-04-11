package dev.prozilla.pine.common.property.style;

import dev.prozilla.pine.common.math.dimension.DimensionBase;
import dev.prozilla.pine.common.property.VariableProperty;
import dev.prozilla.pine.common.property.adaptive.AdaptiveDimensionProperty;
import dev.prozilla.pine.common.property.adaptive.AdaptiveProperty;
import dev.prozilla.pine.common.property.adaptive.AdaptivePropertyBase;
import dev.prozilla.pine.common.property.animated.AnimationCurve;
import dev.prozilla.pine.common.property.animated.transitioned.TransitionedDimensionProperty;
import dev.prozilla.pine.core.component.ui.Node;

import java.util.List;

public final class StyledDimensionProperty extends StyledProperty<DimensionBase> {
	
	public StyledDimensionProperty(StyledPropertyKey<DimensionBase> name, Node node, List<StyleRule<DimensionBase>> styleRules, AdaptivePropertyBase<DimensionBase> defaultValue) {
		this(name, node, styleRules, defaultValue, null);
	}
	
	public StyledDimensionProperty(StyledPropertyKey<DimensionBase> name, Node node, List<StyleRule<DimensionBase>> styleRules, AdaptivePropertyBase<DimensionBase> defaultValue, List<StyleRule<AnimationCurve>> transitionRules) {
		super(name, node, styleRules, defaultValue, transitionRules);
	}
	
	@Override
	protected AdaptiveProperty<DimensionBase> createAdaptiveProperty(DimensionBase value) {
		return AdaptiveDimensionProperty.adapt(value);
	}
	
	@Override
	protected AdaptiveProperty<DimensionBase> createAdaptiveProperty(VariableProperty<DimensionBase> property) {
		return AdaptiveDimensionProperty.adapt(property);
	}
	
	@Override
	protected TransitionedDimensionProperty createTransitionedProperty(DimensionBase initialValue, AnimationCurve curve) {
		return new TransitionedDimensionProperty(initialValue, curve);
	}
	
}
