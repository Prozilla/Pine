package dev.prozilla.pine.common.property.style;

import dev.prozilla.pine.common.Printable;
import dev.prozilla.pine.common.math.dimension.DualDimension;
import dev.prozilla.pine.common.property.adaptive.AdaptiveColorProperty;
import dev.prozilla.pine.common.property.adaptive.AdaptiveDualDimensionProperty;
import dev.prozilla.pine.common.property.adaptive.AdaptiveProperty;
import dev.prozilla.pine.common.property.animated.AnimationCurve;
import dev.prozilla.pine.common.property.style.selector.Selector;
import dev.prozilla.pine.common.system.resource.Color;
import dev.prozilla.pine.common.system.resource.Resource;
import dev.prozilla.pine.common.system.resource.ResourcePool;
import dev.prozilla.pine.core.component.canvas.RectTransform;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * Manages style rules for different properties of canvas elements.
 */
public class StyleSheet implements Printable, Resource {
	
	private final Map<StyledPropertyKey<?>, Style<?>> styles;
	
	public String path;
	
	public StyleSheet() {
		this.styles = new HashMap<>();
	}
	
	public <T> void parseRule(Selector selector, StyledPropertyKey<T> propertyName, String input) {
		T value = propertyName.parseValue(input);
		if (value != null) {
			addRule(selector, propertyName, value);
		}
	}
	
	public <T> void addRule(Selector selector, StyledPropertyKey<T> propertyName, T value) {
		addRule(propertyName, new StyleRule<>(selector, value));
	}
	
	protected <T> void addRule(StyledPropertyKey<T> propertyName, StyleRule<T> rule) {
		Style<T> style = getStyle(propertyName, true);
		style.addRule(rule);
	}
	
	public void addTransition(Selector selector, StyledPropertyKey<?> propertyName, AnimationCurve value) {
		addTransition(propertyName, new StyleRule<>(selector, value));
	}
	
	protected void addTransition(StyledPropertyKey<?> propertyName, StyleRule<AnimationCurve> transitionRule) {
		Style<?> style = getStyle(propertyName, true);
		style.addTransitionRule(transitionRule);
	}
	
	public <T> void setDefaultValue(StyledPropertyKey<T> propertyName, AdaptiveProperty<T> defaultValue) {
		Objects.requireNonNull(defaultValue, "defaultValue must not be null");
		
		Style<T> style = getStyle(propertyName, true);
		style.setDefaultValue(defaultValue);
	}
	
	public StyledColorProperty createColorProperty(RectTransform context) {
		return createStyledColorProperty(StyledPropertyKey.COLOR, context, Color.white());
	}
	
	public StyledColorProperty createBackgroundColorProperty(RectTransform context) {
		return createStyledColorProperty(StyledPropertyKey.BACKGROUND_COLOR, context, Color.black());
	}
	
	public StyledDualDimensionProperty createSizeProperty(RectTransform context) {
		return createStyledDualDimensionProperty(StyledPropertyKey.SIZE, context, new DualDimension());
	}
	
	public StyledDualDimensionProperty createPaddingProperty(RectTransform context) {
		return createStyledDualDimensionProperty(StyledPropertyKey.PADDING, context, new DualDimension());
	}
	
	public StyledDualDimensionProperty createPositionProperty(RectTransform context) {
		return createStyledDualDimensionProperty(StyledPropertyKey.POSITION, context, new DualDimension());
	}
	
	protected StyledColorProperty createStyledColorProperty(StyledPropertyKey<Color> name, RectTransform context, Color fallbackValue) {
		return createStyledProperty(name, context, new AdaptiveColorProperty(fallbackValue),  (Style.StyledPropertyFactory<Color, StyledColorProperty>)StyledColorProperty::new);
	}
	
	protected StyledDualDimensionProperty createStyledDualDimensionProperty(StyledPropertyKey<DualDimension> name, RectTransform context, DualDimension fallbackValue) {
		return createStyledProperty(name, context, new AdaptiveDualDimensionProperty(fallbackValue),  (Style.StyledPropertyFactory<DualDimension, StyledDualDimensionProperty>)StyledDualDimensionProperty::new);
	}
	
	protected  <T, P extends StyledProperty<T>> P createStyledProperty(StyledPropertyKey<T> name, RectTransform context, AdaptiveProperty<T> fallbackValue, Style.StyledPropertyFactory<T, P> factory) {
		Style<T> style = getStyle(name, false);
		return style != null ? style.toProperty(name, context, fallbackValue, factory) : null;
	}
	
	@SuppressWarnings("unchecked")
	protected <T> Style<T> getStyle(StyledPropertyKey<T> propertyName, boolean createIfMissing) {
		return (Style<T>)getGenericStyle(propertyName, createIfMissing);
	}
	
	protected Style<?> getGenericStyle(StyledPropertyKey<?> propertyName, boolean createIfMissing) {
		Style<?> style = styles.get(propertyName);
		
		if (style == null && createIfMissing) {
			style = new Style<>();
			styles.put(propertyName, style);
		}
		
		return style;
	}
	
	@Override
	public String getPath() {
		return path;
	}
	
	@Override
	public void destroy() {
		if (path != null) {
			ResourcePool.removeStyleSheet(path);
		}
	}
	
	@Override
	public String toString() {
		Map<String, StringJoiner> selectorToProperties = new HashMap<>();
		
		for (Map.Entry<StyledPropertyKey<?>, Style<?>> styleEntry : styles.entrySet()) {
			StyledPropertyKey<?> propertyName = styleEntry.getKey();
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
	
	public static StyleSheet parse(String css) {
		// Remove comments
		css = css.replaceAll("/\\*.*?\\*/", "");
		
		StyleSheet styleSheet = new StyleSheet();
		int i = 0;
		int len = css.length();
		
		while (i < len) {
			i = skipWhitespace(css, i);
			if (i >= len) {
				break;
			}
			
			// Read selector (continue until "{")
			int start = i;
			while (i < len && css.charAt(i) != '{') {
				i++;
			}
			if (i >= len) {
				break;
			}
			
			Selector selector = Selector.parse(css.substring(start, i).trim());
			i++;
			
			// Read properties (continue until "}")
			while (i < len && css.charAt(i) != '}') {
				i = skipWhitespace(css, i);
				if (i >= len || css.charAt(i) == '}') {
					break;
				}
				
				// Read property name (continue until ":")
				start = i;
				while (i < len && css.charAt(i) != ':') {
					i++;
				}
				if (i >= len || css.charAt(i) == '}') {
					break;
				}
				
				String propertyName = css.substring(start, i).trim();
				StyledPropertyKey<?> propertyKey = StyledPropertyKey.parse(propertyName);
				i++;
				
				// Read property value (continue until ";" or "}")
				start = i;
				while (i < len && css.charAt(i) != ';' && css.charAt(i) != '}') {
					i++;
				}
				String value = css.substring(start, i).trim();
				
				if (selector != null) {
					if (propertyName.equals("transition")) {
						// Parse transition property
						for (String transitionValue : value.split(",")) {
							String[] parts = transitionValue.trim().split(" ", 2);
							
							if (parts.length == 2) {
								propertyKey = StyledPropertyKey.parse(parts[0]);
								AnimationCurve animationCurve = AnimationCurve.parse(parts[1]);
								
								if (propertyKey != null && animationCurve != null) {
									styleSheet.addTransition(selector, propertyKey, animationCurve);
								}
							}
						}
					} else if (propertyKey != null) {
						// Parse normal property
						styleSheet.parseRule(selector, propertyKey, value);
					}
				}
				
				// Skip "}"
				if (i < len && css.charAt(i) == ';') {
					i++;
				}
			}
			
			// Skip "}"
			if (i < len && css.charAt(i) == '}') {
				i++;
			}
		}
		
		return styleSheet;
	}
	
	private static int skipWhitespace(String css, int i) {
		int len = css.length();
		while (i < len && Character.isWhitespace(css.charAt(i))) {
			i++;
		}
		return i;
	}
	
}
