package dev.prozilla.pine.common.property.style;

import dev.prozilla.pine.common.math.dimension.DualDimension;
import dev.prozilla.pine.common.property.adaptive.AdaptiveProperty;
import dev.prozilla.pine.common.property.animated.AnimationCurve;
import dev.prozilla.pine.common.property.style.selector.Selector;
import dev.prozilla.pine.common.system.resource.Color;
import dev.prozilla.pine.core.component.canvas.RectTransform;

import java.util.HashMap;
import java.util.Map;

/**
 * Manages style rules for different properties of canvas elements.
 */
public class StyleSheet {
	
	private final Map<StyledPropertyName, Style<?>> styles;
	
	public StyleSheet() {
		this.styles = new HashMap<>();
	}
	
	public <T> void addRule(Selector selector, StyledPropertyName propertyName, T value) {
		addRule(propertyName, new StyleRule<>(selector, value));
	}
	
	protected <T> void addRule(StyledPropertyName propertyName, StyleRule<T> rule) {
		Style<T> style = getStyle(propertyName);
		style.addRule(rule);
	}
	
	public void addTransition(Selector selector, StyledPropertyName propertyName, AnimationCurve value) {
		addTransition(propertyName, new StyleRule<>(selector, value));
	}
	
	protected void addTransition(StyledPropertyName propertyName, StyleRule<AnimationCurve> transitionRule) {
		Style<?> style = getStyle(propertyName);
		style.addTransitionRule(transitionRule);
	}
	
	public <T> void setDefaultValue(StyledPropertyName propertyName, AdaptiveProperty<T> defaultValue) {
		Style<T> style = getStyle(propertyName);
		style.setDefaultValue(defaultValue);
	}
	
	public StyledColorProperty createColorProperty(RectTransform context) {
		return createProperty(StyledPropertyName.COLOR, context, (Style.StyledPropertyFactory<Color, StyledColorProperty>)StyledColorProperty::new);
	}
	
	public StyledDualDimensionProperty createSizeProperty(RectTransform context) {
		return createProperty(StyledPropertyName.SIZE, context, (Style.StyledPropertyFactory<DualDimension, StyledDualDimensionProperty>)StyledDualDimensionProperty::new);
	}
	
	public <T, P extends StyledProperty<T>> P createProperty(StyledPropertyName name, RectTransform context, Style.StyledPropertyFactory<T, P> factory) {
		Style<T> style = getStyle(name);
		return style != null ? style.toProperty(name, context, factory) : null;
	}
	
	@SuppressWarnings("unchecked")
	protected <T> Style<T> getStyle(StyledPropertyName propertyName) {
		return (Style<T>)getGenericStyle(propertyName);
	}
	
	protected Style<?> getGenericStyle(StyledPropertyName propertyName) {
		Style<?> style = styles.get(propertyName);
		
		if (style == null) {
			style = new Style<>();
			styles.put(propertyName, style);
		}
		
		return style;
	}
	
}
