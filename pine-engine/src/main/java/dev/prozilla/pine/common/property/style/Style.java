package dev.prozilla.pine.common.property.style;

import dev.prozilla.pine.common.property.adaptive.AdaptiveProperty;
import dev.prozilla.pine.common.property.adaptive.AdaptivePropertyBase;
import dev.prozilla.pine.common.property.animated.AnimationCurve;
import dev.prozilla.pine.core.component.ui.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a collection of rules for a single style property of a node.
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
	
	public List<StyleRule<AnimationCurve>> getTransitionRules() {
		return transitionRules;
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
	
	public <P extends StyledProperty<T>> P toProperty(StyledPropertyKey<T> name, Node node, AdaptivePropertyBase<T> fallbackValue, StyledPropertyFactory<T, P> factory) {
		return factory.create(name, node, rules, Objects.requireNonNullElse(defaultValue,  fallbackValue), transitionRules);
	}
	
	@FunctionalInterface
	public interface StyledPropertyFactory<T, P extends StyledProperty<T>> {
		
		P create(StyledPropertyKey<T> name, Node node, List<StyleRule<T>> rules, AdaptivePropertyBase<T> defaultValue, List<StyleRule<AnimationCurve>> transitionRules);
		
	}
	
}
