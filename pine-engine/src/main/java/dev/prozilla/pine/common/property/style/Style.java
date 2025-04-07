package dev.prozilla.pine.common.property.style;

import dev.prozilla.pine.common.property.adaptive.AdaptiveProperty;
import dev.prozilla.pine.common.property.animated.AnimationCurve;

import java.util.ArrayList;
import java.util.List;

public class Style<T> {
	
	private final List<StyleRule<T>> rules;
	private final List<StyleRule<AnimationCurve>> transitionRules;
	private AdaptiveProperty<T> defaultValue;
	
	public Style() {
		this.rules = new ArrayList<>();
		this.transitionRules = new ArrayList<>();
	}
	
	public void addRule(StyleRule<T> rule) {
		rules.add(rule);
	}
	
	public void addTransitionRule(StyleRule<AnimationCurve> transitionRule) {
		transitionRules.add(transitionRule);
	}
	
	public void setDefaultValue(AdaptiveProperty<T> defaultValue) {
		this.defaultValue = defaultValue;
	}
	
	public List<StyleRule<T>> getRules() {
		return rules;
	}
	
	public List<StyleRule<AnimationCurve>> getTransitionRules() {
		return transitionRules;
	}
	
	public AdaptiveProperty<T> getDefaultValue() {
		return defaultValue;
	}
	
}
