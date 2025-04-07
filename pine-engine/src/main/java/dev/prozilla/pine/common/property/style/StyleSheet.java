package dev.prozilla.pine.common.property.style;

import dev.prozilla.pine.common.Printable;
import dev.prozilla.pine.common.math.dimension.DualDimension;
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
		return createProperty(StyledPropertyName.COLOR, context, (Style.StyledPropertyFactory<Color, StyledColorProperty>)StyledColorProperty::new);
	}
	
	public StyledColorProperty createBackgroundColorProperty(RectTransform context) {
		return createProperty(StyledPropertyName.BACKGROUND_COLOR, context, (Style.StyledPropertyFactory<Color, StyledColorProperty>)StyledColorProperty::new);
	}
	
	public StyledDualDimensionProperty createSizeProperty(RectTransform context) {
		return createProperty(StyledPropertyName.SIZE, context, (Style.StyledPropertyFactory<DualDimension, StyledDualDimensionProperty>)StyledDualDimensionProperty::new);
	}
	
	protected  <T, P extends StyledProperty<T>> P createProperty(StyledPropertyName name, RectTransform context, Style.StyledPropertyFactory<T, P> factory) {
		Style<T> style = getStyle(name, false);
		return style != null ? style.toProperty(name, context, factory) : null;
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
		StringJoiner stringJoiner = new StringJoiner(" ");
		
		for (Map.Entry<StyledPropertyName, Style<?>> styleEntry : styles.entrySet()) {
			for (StyleRule<?> rule : styleEntry.getValue().getRules()) {
				stringJoiner.add(rule.selector().toString());
				stringJoiner.add("{");
				stringJoiner.add(styleEntry.getKey().toString() + ":");
				stringJoiner.add(rule.value().toString() + ";");
				stringJoiner.add("}");
			}
		}
		
		return stringJoiner.toString();
	}
}
