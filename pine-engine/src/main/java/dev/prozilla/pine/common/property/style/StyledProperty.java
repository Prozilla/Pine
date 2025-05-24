package dev.prozilla.pine.common.property.style;

import dev.prozilla.pine.common.Animatable;
import dev.prozilla.pine.common.Printable;
import dev.prozilla.pine.common.property.VariableProperty;
import dev.prozilla.pine.common.property.adaptive.AdaptivePropertyBase;
import dev.prozilla.pine.common.property.animated.AnimationCurve;
import dev.prozilla.pine.common.property.animated.transitioned.TransitionedProperty;
import dev.prozilla.pine.core.component.ui.Node;
import dev.prozilla.pine.core.component.ui.NodeEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * A property used to style nodes based on a set of rules.
 *
 * <p>If a transition rule applies to this property, it will start a transition whenever its value changes.</p>
 */
public abstract class StyledProperty<T> extends VariableProperty<T> implements Animatable, Printable {
	
	protected final StyledPropertyKey<T> name;
	protected final Node node;
	
	protected final List<StyleRule<T>> rules;
	private StyleRule<T> currentRule;
	private AdaptivePropertyBase<T> adaptiveProperty;
	private final AdaptivePropertyBase<T> fallbackProperty;
	
	protected final List<StyleRule<AnimationCurve>> transitionRules;
	private StyleRule<AnimationCurve> currentTransitionRule;
	private TransitionedProperty<T> transitionedProperty;
	
	/**
	 * Creates a styled property without any transitions.
	 * @param node The node this property belongs to
	 * @param rules The rules that determine the value of this property
	 * @param defaultValue The initial value of this property. Also used when no rule is applied.
	 */
	public StyledProperty(StyledPropertyKey<T> name, Node node, List<StyleRule<T>> rules, AdaptivePropertyBase<T> defaultValue) {
		this(name, node, rules, defaultValue, null);
	}
	
	/**
	 * Creates a styled property with transitions.
	 * @param node The node this property belongs to
	 * @param rules The rules that determine the value of this property
	 * @param defaultValue The initial value of this property. Also used when no rule is applied.
	 * @param transitionRules The rules that determine how the value of this property transitions when changed
	 */
	public StyledProperty(StyledPropertyKey<T> name, Node node, List<StyleRule<T>> rules, AdaptivePropertyBase<T> defaultValue, List<StyleRule<AnimationCurve>> transitionRules) {
		this.name = name;
		this.node = Objects.requireNonNull(node, "node must not be null");
		this.rules = Objects.requireNonNullElse(rules, new ArrayList<>());
		this.adaptiveProperty = Objects.requireNonNull(defaultValue, "defaultValue must not be null");
		this.transitionRules = Objects.requireNonNullElse(transitionRules, new ArrayList<>());
		fallbackProperty = this.adaptiveProperty;
		
		// Re-apply style when selector changes
		node.addListener(NodeEvent.SELECTOR_CHANGE, (changedNode) -> this.invalidate());
	}
	
	public void addRule(StyleRule<T> rule) {
		rules.add(rule);
		applyRules();
	}
	
	public void invalidate() {
		applyTransitionRules();
		applyRules();
	}
	
	protected void applyRules() {
		StyleRule<T> rule = getBestMatch(rules);
		if (Objects.equals(rule, currentRule)) {
			return;
		}
		currentRule = rule;
		
		T value = rule != null ? rule.value() : fallbackProperty.getValue();
		
		if (transitionedProperty != null) {
			transitionedProperty.transitionTo(value);
		} else {
			setAdaptiveProperty(createAdaptiveProperty(value));
		}
	}
	
	public void addTransitionRule(StyleRule<AnimationCurve> transitionRule) {
		transitionRules.add(transitionRule);
		applyTransitionRules();
	}
	
	protected void applyTransitionRules() {
		if (!supportsTransitions()) {
			return;
		}
		
		StyleRule<AnimationCurve> transitionRule = getBestMatch(transitionRules);
		
		if (Objects.equals(transitionRule, currentTransitionRule)) {
			return;
		}
		currentTransitionRule = transitionRule;
		
		if (transitionRule == null) {
			transitionedProperty = null;
			return;
		}
		
		if (transitionedProperty == null || !transitionedProperty.getCurve().equals(transitionRule.value())) {
			transitionedProperty = createTransitionedProperty(adaptiveProperty.getValue(), transitionRule.value());
			if (transitionedProperty != null) {
				setAdaptiveProperty(createAdaptiveProperty(transitionedProperty));
			}
		}
	}
	
	abstract protected AdaptivePropertyBase<T> createAdaptiveProperty(T value);
	
	abstract protected AdaptivePropertyBase<T> createAdaptiveProperty(VariableProperty<T> property);
	
	abstract protected TransitionedProperty<T> createTransitionedProperty(T initialValue, AnimationCurve curve);
	
	public boolean supportsTransitions() {
		return true;
	}
	
	protected <U> StyleRule<U> getBestMatch(List<StyleRule<U>> rules) {
		StyleRule<U> bestMatch = null;
		for (StyleRule<U> rule : rules) {
			if (rule.matches(node) && (bestMatch == null || rule.getSpecificity() > bestMatch.getSpecificity())) {
				bestMatch = rule;
			}
		}
		
		return bestMatch;
	}
	
	protected void setAdaptiveProperty(AdaptivePropertyBase<T> adaptiveProperty) {
		if (this.adaptiveProperty == adaptiveProperty) {
			return;
		}
		
		this.adaptiveProperty = adaptiveProperty;
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
			stringJoiner.add(rule.selector().toString());
			stringJoiner.add("{");
			stringJoiner.add(name + ":");
			stringJoiner.add(rule.value().toString() + ";");
			stringJoiner.add("}");
		}
		
		return stringJoiner.toString();
	}
	
}
