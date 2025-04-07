package dev.prozilla.pine.common.property.style;

import dev.prozilla.pine.common.property.adaptive.AdaptiveProperty;
import dev.prozilla.pine.common.property.animated.AnimationCurve;
import dev.prozilla.pine.common.property.style.selector.Selector;
import dev.prozilla.pine.common.system.resource.Color;
import dev.prozilla.pine.core.component.canvas.RectTransform;

import java.util.HashMap;
import java.util.Map;

public class StyleSheet {
	
	private final Map<StyledPropertyName, Style<?>> styles;
	
	public StyleSheet() {
		this.styles = new HashMap<>();
	}
	
	public <T> void addRule(Selector selector, StyledPropertyName propertyName, T value) {
		addRule(propertyName, new StyleRule<>(selector, value));
	}
	
	@SuppressWarnings("unchecked")
	protected <T> void addRule(StyledPropertyName propertyName, StyleRule<T> rule) {
		Style<T> style = (Style<T>)getStyle(propertyName);
		style.addRule(rule);
	}
	
	public void addTransition(Selector selector, StyledPropertyName propertyName, AnimationCurve value) {
		addTransition(propertyName, new StyleRule<>(selector, value));
	}
	
	protected void addTransition(StyledPropertyName propertyName, StyleRule<AnimationCurve> transitionRule) {
		Style<?> style = getStyle(propertyName);
		style.addTransitionRule(transitionRule);
	}
	
	@SuppressWarnings("unchecked")
	public <T> void setDefaultValue(StyledPropertyName propertyName, AdaptiveProperty<T> defaultValue) {
		Style<T> style = (Style<T>)getStyle(propertyName);
		style.setDefaultValue(defaultValue);
	}
	
	@SuppressWarnings("unchecked")
	public StyledColorProperty createColorProperty(RectTransform context) {
		Style<Color> style = (Style<Color>)getStyle(StyledPropertyName.COLOR);
		
		if (style == null) {
			return null;
		}
		
		return new StyledColorProperty(StyledPropertyName.COLOR, context, style.getRules(), style.getDefaultValue(), style.getTransitionRules());
	}
	
	protected Style<?> getStyle(StyledPropertyName propertyName) {
		Style<?> style = styles.get(propertyName);
		
		if (style == null) {
			style = new Style<>();
			styles.put(propertyName, style);
		}
		
		return style;
	}
	
}
