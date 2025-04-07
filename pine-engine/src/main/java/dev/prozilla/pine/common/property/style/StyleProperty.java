package dev.prozilla.pine.common.property.style;

import dev.prozilla.pine.common.Animatable;
import dev.prozilla.pine.common.Printable;
import dev.prozilla.pine.common.property.VariableProperty;
import dev.prozilla.pine.common.property.adaptive.AdaptiveProperty;
import dev.prozilla.pine.common.property.animated.AnimationCurve;
import dev.prozilla.pine.common.property.animated.transitioned.TransitionedProperty;
import dev.prozilla.pine.core.component.canvas.RectEvent;
import dev.prozilla.pine.core.component.canvas.RectTransform;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

public abstract class StyleProperty<T> extends VariableProperty<T> implements Animatable, Printable {
	
	protected final RectTransform context;
	
	protected final List<StyleRule<T>> rules;
	private AdaptiveProperty<T> adaptiveProperty;
	private final AdaptiveProperty<T> fallbackProperty;
	
	protected final List<StyleRule<AnimationCurve>> transitionRules;
	private TransitionedProperty<T> transitionedProperty;
	
	private final List<StylePropertyEventListener<T>> listeners;
	
	public StyleProperty(RectTransform context, List<StyleRule<T>> rules, AdaptiveProperty<T> defaultValue) {
		this.context = Objects.requireNonNull(context, "context must not be null");
		this.rules = Objects.requireNonNullElse(rules, new ArrayList<>());
		this.adaptiveProperty = Objects.requireNonNull(defaultValue, "defaultValue must not be null");
		fallbackProperty = this.adaptiveProperty;
		
		transitionRules = new ArrayList<>();
		listeners = new ArrayList<>();
		
		// Re-apply style when selector changes
		context.addListener(RectEvent.SELECTOR_CHANGE, () -> {
			applyTransitionRules();
			applyRules();
		});
	}
	
	public void addRule(StyleRule<T> rule) {
		rules.add(rule);
		applyRules();
	}
	
	protected void applyRules() {
		StyleRule<T> rule = getBestMatch(rules);
		T value = rule != null ? rule.getValue() : fallbackProperty.getValue();
		
		if (transitionedProperty != null) {
			transitionedProperty.transitionTo(value);
		} else {
			setAdaptiveProperty(new AdaptiveProperty<>(value));
		}
	}
	
	public void addTransitionRule(StyleRule<AnimationCurve> transitionRule) {
		transitionRules.add(transitionRule);
		applyTransitionRules();
	}
	
	protected void applyTransitionRules() {
		StyleRule<AnimationCurve> transitionRule = getBestMatch(transitionRules);
		
		if (transitionRule == null) {
			transitionedProperty = null;
			return;
		}
		
		if (transitionedProperty == null || transitionedProperty.getCurve().equals(transitionRule.getValue())) {
			transitionedProperty = createTransitionedProperty(adaptiveProperty.getValue(), transitionRule.getValue());
			setAdaptiveProperty(new AdaptiveProperty<>(transitionedProperty));
		}
	}
	
	abstract protected TransitionedProperty<T> createTransitionedProperty(T initialValue, AnimationCurve curve);
	
	protected <U> StyleRule<U> getBestMatch(List<StyleRule<U>> rules) {
		StyleRule<U> bestMatch = null;
		for (StyleRule<U> rule : rules) {
			if (rule.matches(context) && (bestMatch == null || rule.getSpecificity() > bestMatch.getSpecificity())) {
				bestMatch = rule;
			}
		}
		
		return bestMatch;
	}
	
	protected void setAdaptiveProperty(AdaptiveProperty<T> adaptiveProperty) {
		if (this.adaptiveProperty == adaptiveProperty) {
			return;
		}
		
		fireChangeEvent(this.adaptiveProperty, adaptiveProperty);
		this.adaptiveProperty = adaptiveProperty;
	}
	
	protected void fireChangeEvent(AdaptiveProperty<T> oldProperty, AdaptiveProperty<T> newProperty) {
		for (StylePropertyEventListener<T> listener : listeners) {
			listener.onChange(oldProperty, newProperty);
		}
	}
	
	public void addListener(StylePropertyEventListener<T> listener) {
		listeners.add(listener);
	}
	
	public void removeListener(StylePropertyEventListener<T> listener) {
		listeners.remove(listener);
	}
	
	@Override
	public void restartAnimation() {
		adaptiveProperty.restartAnimation();
	}
	
	@Override
	public void updateAnimation(float deltaTime) {
		adaptiveProperty.updateAnimation(deltaTime);
	}
	
	@Override
	public T getValue() {
		return adaptiveProperty.getValue();
	}
	
	@Override
	public String toString() {
		StringJoiner stringJoiner = new StringJoiner(" ");
		
		for (StyleRule<T> rule : rules) {
			stringJoiner.add(rule.getSelector().toString());
			stringJoiner.add("{");
			stringJoiner.add(rule.getValue().toString());
			stringJoiner.add("}");
		}
		
		return stringJoiner.toString();
	}
}
