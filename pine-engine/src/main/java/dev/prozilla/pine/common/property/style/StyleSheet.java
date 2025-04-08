package dev.prozilla.pine.common.property.style;

import dev.prozilla.pine.common.Printable;
import dev.prozilla.pine.common.math.dimension.DualDimension;
import dev.prozilla.pine.common.property.adaptive.AdaptiveColorProperty;
import dev.prozilla.pine.common.property.adaptive.AdaptiveDualDimensionProperty;
import dev.prozilla.pine.common.property.adaptive.AdaptiveProperty;
import dev.prozilla.pine.common.property.animated.AnimationCurve;
import dev.prozilla.pine.common.property.style.selector.Selector;
import dev.prozilla.pine.common.system.resource.Color;
import dev.prozilla.pine.core.component.canvas.RectTransform;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * Manages style rules for different properties of canvas elements.
 */
public class StyleSheet implements Printable {
	
	private final Map<StyledPropertyName, Style<?>> styles;
	
	public StyleSheet() {
		this.styles = new HashMap<>();
	}
	
	public <T> void addRule(Selector selector, StyledPropertyName propertyName, T value) {
		addRule(propertyName, new StyleRule<>(selector, value));
	}
	
	protected <T> void addRule(StyledPropertyName propertyName, StyleRule<T> rule) {
		Style<T> style = getStyle(propertyName, true);
		style.addRule(rule);
	}
	
	public void addTransition(Selector selector, StyledPropertyName propertyName, AnimationCurve value) {
		addTransition(propertyName, new StyleRule<>(selector, value));
	}
	
	protected void addTransition(StyledPropertyName propertyName, StyleRule<AnimationCurve> transitionRule) {
		Style<?> style = getStyle(propertyName, true);
		style.addTransitionRule(transitionRule);
	}
	
	public <T> void setDefaultValue(StyledPropertyName propertyName, AdaptiveProperty<T> defaultValue) {
		Objects.requireNonNull(defaultValue, "defaultValue must not be null");
		
		Style<T> style = getStyle(propertyName, true);
		style.setDefaultValue(defaultValue);
	}
	
	public StyledColorProperty createColorProperty(RectTransform context) {
		return createStyledColorProperty(StyledPropertyName.COLOR, context, Color.white());
	}
	
	public StyledColorProperty createBackgroundColorProperty(RectTransform context) {
		return createStyledColorProperty(StyledPropertyName.BACKGROUND_COLOR, context, Color.black());
	}
	
	public StyledDualDimensionProperty createSizeProperty(RectTransform context) {
		return createStyledDualDimensionProperty(StyledPropertyName.SIZE, context, new DualDimension());
	}
	
	public StyledDualDimensionProperty createPaddingProperty(RectTransform context) {
		return createStyledDualDimensionProperty(StyledPropertyName.PADDING, context, new DualDimension());
	}
	
	public StyledDualDimensionProperty createPositionProperty(RectTransform context) {
		return createStyledDualDimensionProperty(StyledPropertyName.POSITION, context, new DualDimension());
	}
	
	protected StyledColorProperty createStyledColorProperty(StyledPropertyName name, RectTransform context, Color fallbackValue) {
		return createStyledProperty(name, context, new AdaptiveColorProperty(fallbackValue),  (Style.StyledPropertyFactory<Color, StyledColorProperty>)StyledColorProperty::new);
	}
	
	protected StyledDualDimensionProperty createStyledDualDimensionProperty(StyledPropertyName name, RectTransform context, DualDimension fallbackValue) {
		return createStyledProperty(name, context, new AdaptiveDualDimensionProperty(fallbackValue),  (Style.StyledPropertyFactory<DualDimension, StyledDualDimensionProperty>)StyledDualDimensionProperty::new);
	}
	
	protected  <T, P extends StyledProperty<T>> P createStyledProperty(StyledPropertyName name, RectTransform context, AdaptiveProperty<T> fallbackValue, Style.StyledPropertyFactory<T, P> factory) {
		Style<T> style = getStyle(name, false);
		return style != null ? style.toProperty(name, context, fallbackValue, factory) : null;
	}
	
	@SuppressWarnings("unchecked")
	protected <T> Style<T> getStyle(StyledPropertyName propertyName, boolean createIfMissing) {
		return (Style<T>)getGenericStyle(propertyName, createIfMissing);
	}
	
	protected Style<?> getGenericStyle(StyledPropertyName propertyName, boolean createIfMissing) {
		Style<?> style = styles.get(propertyName);
		
		if (style == null && createIfMissing) {
			style = new Style<>();
			styles.put(propertyName, style);
		}
		
		return style;
	}
	
	@Override
	public String toString() {
		Map<String, StringJoiner> selectorToProperties = new HashMap<>();
		
		for (Map.Entry<StyledPropertyName, Style<?>> styleEntry : styles.entrySet()) {
			StyledPropertyName propertyName = styleEntry.getKey();
			Style<?> style = styleEntry.getValue();
			
			for (StyleRule<?> rule : style.getRules()) {
				String selector = rule.selector().toString();
				String property = String.format("%s: %s;", propertyName.toString(), rule.value().toString());
				
				selectorToProperties
					.computeIfAbsent(selector, k -> new StringJoiner(" "))
					.add(property);
			}
			
			for (StyleRule<AnimationCurve> transitionRule : style.getTransitionRules()) {
				String selector = transitionRule.selector().toString();
				String property = String.format("transition: %s %s;", propertyName.toString(), transitionRule.value().toString());
				
				selectorToProperties
					.computeIfAbsent(selector, k -> new StringJoiner(" "))
					.add(property);
			}
		}
		
		StringJoiner result = new StringJoiner(" ");
		for (Map.Entry<String, StringJoiner> entry : selectorToProperties.entrySet()) {
			result.add(entry.getKey());
			result.add("{");
			result.add(entry.getValue().toString());
			result.add("}");
		}
		
		return result.toString();
	}
	
}
