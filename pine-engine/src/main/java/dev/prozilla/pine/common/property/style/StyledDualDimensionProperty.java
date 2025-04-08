package dev.prozilla.pine.common.property.style;

import dev.prozilla.pine.common.math.dimension.DualDimension;
import dev.prozilla.pine.common.property.VariableProperty;
import dev.prozilla.pine.common.property.adaptive.AdaptiveDualDimensionProperty;
import dev.prozilla.pine.common.property.adaptive.AdaptiveProperty;
import dev.prozilla.pine.common.property.animated.AnimationCurve;
import dev.prozilla.pine.common.property.animated.transitioned.TransitionedDualDimensionProperty;
import dev.prozilla.pine.core.component.canvas.RectTransform;

import java.util.List;

public final class StyledDualDimensionProperty extends StyledProperty<DualDimension> {
	
	public StyledDualDimensionProperty(StyledPropertyKey name, RectTransform context, List<StyleRule<DualDimension>> styleRules, AdaptiveProperty<DualDimension> defaultValue) {
		this(name, context, styleRules, defaultValue, null);
	}
	
	public StyledDualDimensionProperty(StyledPropertyKey name, RectTransform context, List<StyleRule<DualDimension>> styleRules, AdaptiveProperty<DualDimension> defaultValue, List<StyleRule<AnimationCurve>> transitionRules) {
		super(name, context, styleRules, defaultValue, transitionRules);
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
