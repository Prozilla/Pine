package dev.prozilla.pine.common.property.style;

import dev.prozilla.pine.common.property.adaptive.AdaptiveProperty;
import dev.prozilla.pine.common.property.animated.AnimationCurve;
import dev.prozilla.pine.core.component.canvas.RectTransform;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a collection of rules for a single style property of a canvas element.
 * @param <T> The type of the property
 */
public class Style<T> {
	
	private final List<StyleRule<T>> rules;
	private final List<StyleRule<AnimationCurve>> transitionRules;
	private AdaptiveProperty<T> defaultValue;
	
	public Style() {
		this.rules = new ArrayList<>();
		this.transitionRules = new ArrayList<>();
	}
	
	public List<StyleRule<T>> getRules() {
		return rules;
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
	
	public <P extends StyledProperty<T>> P toProperty(StyledPropertyName name, RectTransform context, StyledPropertyFactory<T, P> factory) {
		return factory.create(name, context, rules, defaultValue, transitionRules);
	}
	
	@FunctionalInterface
	public interface StyledPropertyFactory<T, P extends StyledProperty<T>> {
		
		P create(StyledPropertyName name, RectTransform context, List<StyleRule<T>> rules, AdaptiveProperty<T> defaultValue, List<StyleRule<AnimationCurve>> transitionRules);
		
	}
	
}
