package dev.prozilla.pine.common.property.style;

import dev.prozilla.pine.common.math.dimension.DualDimension;
import dev.prozilla.pine.common.property.VariableProperty;
import dev.prozilla.pine.common.property.adaptive.AdaptiveDualDimensionProperty;
import dev.prozilla.pine.common.property.adaptive.AdaptiveProperty;
import dev.prozilla.pine.common.property.animated.AnimationCurve;
import dev.prozilla.pine.common.property.animated.transitioned.TransitionedDualDimensionProperty;
import dev.prozilla.pine.core.component.ui.Node;

import java.util.List;

public final class StyledDualDimensionProperty extends StyledProperty<DualDimension> {
	
	public StyledDualDimensionProperty(StyledPropertyKey<DualDimension> name, Node node, List<StyleRule<DualDimension>> styleRules, AdaptiveProperty<DualDimension> defaultValue) {
		this(name, node, styleRules, defaultValue, null);
	}
	
	public StyledDualDimensionProperty(StyledPropertyKey<DualDimension> name, Node node, List<StyleRule<DualDimension>> styleRules, AdaptiveProperty<DualDimension> defaultValue, List<StyleRule<AnimationCurve>> transitionRules) {
		super(name, node, styleRules, defaultValue, transitionRules);
	}
	
	@Override
	protected AdaptiveProperty<DualDimension> createAdaptiveProperty(DualDimension value) {
		return AdaptiveDualDimensionProperty.adapt(value);
	}
	
	@Override
	protected AdaptiveProperty<DualDimension> createAdaptiveProperty(VariableProperty<DualDimension> property) {
		return AdaptiveDualDimensionProperty.adapt(property);
	}
	
	@Override
	protected TransitionedDualDimensionProperty createTransitionedProperty(DualDimension initialValue, AnimationCurve curve) {
		return new TransitionedDualDimensionProperty(initialValue, curve);
	}
	
}
