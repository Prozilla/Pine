package dev.prozilla.pine.common.asset.style;

import dev.prozilla.pine.common.asset.style.selector.Selector;
import dev.prozilla.pine.common.asset.style.selector.SelectorParser;
import dev.prozilla.pine.common.property.animated.AnimationCurve;
import dev.prozilla.pine.common.property.animated.AnimationCurveParser;
import dev.prozilla.pine.common.util.parser.SuppliedSequentialParser;

import java.util.ArrayList;
import java.util.List;

public class CSSParser extends SuppliedSequentialParser<StyleSheet> {
	
	private static final SelectorParser selectorParser = new SelectorParser();
	private static final AnimationCurveParser animationCurveParser = new AnimationCurveParser();
	
	public CSSParser() {
		super(StyleSheet::new);
	}
	
	@Override
	protected boolean parse() {
		while (!endOfInput()) {
			skipWhitespace();
			if (endOfInput())
				break;
			
			// Read selector
			Selector selector = parseSelector();
			if (selector == null)
				break;
			
			// Read properties
			while (!endOfInput() && getChar() != '}') {
				skipWhitespace();
				if (endOfInput() || getChar() == '}') {
					break;
				}
				
				// Read property name
				String propertyName = parsePropertyName();
				if (propertyName == null)
					break;
				StyledPropertyKey<?> propertyKey = StyledPropertyKey.parse(propertyName);
				moveCursor();
				
				// Read property value
				parsePropertyValue(selector, propertyName, propertyKey);
				
				skipIfChar(';');
			}
			
			// Skip "}"
			skipIfChar('}');
		}
		
		return succeed();
	}
	
	private Selector parseSelector() {
		int start = getCursor();
		skipUntilChar('{');
		if (endOfInput()) {
			return null;
		}
		
		if (selectorParser.parse(getInput().substring(start, getCursor()).trim())) {
			moveCursor();
			return selectorParser.getResult();
		} else {
			return null;
		}
	}
	
	private String parsePropertyName() {
		int start = getCursor();
		skipUntilChar(':');
		if (endOfInput() || getChar() == '}') {
			return null;
		}
		return getInput().substring(start, getCursor()).trim();
	}
	
	private void parsePropertyValue(Selector selector, String propertyName, StyledPropertyKey<?> propertyKey) {
		int start = getCursor();
		skipUntilAnyChar(';', '}');
		String value = getInput().substring(start, getCursor()).trim();
		
		if (selector != null) {
			if (propertyName.equalsIgnoreCase("transition")) {
				parseTransitionProperty(value, selector);
			} else if (propertyKey != null) {
				intermediate.parseRule(selector, propertyKey, value);
			} else {
				StyledPropertyShorthand shorthand = StyledPropertyShorthand.parse(propertyName);
				if (shorthand != null) {
					shorthand.apply(intermediate, selector, value);
				}
			}
		}
	}
	
	private void parseTransitionProperty(String value, Selector selector) {
		for (String transitionValue : splitTopLevel(value, ',')) {
			String[] parts = transitionValue.trim().split("\\s+", 2);
			
			if (parts.length == 2 && animationCurveParser.parse(parts[1])) {
				AnimationCurve curve = animationCurveParser.getResult();
				
				if (curve != null) {
					StyledPropertyKey<?> key = StyledPropertyKey.parse(parts[0]);
					if (key != null) {
						intermediate.addTransition(selector, key, curve);
					} else {
						StyledPropertyShorthand shorthand = StyledPropertyShorthand.parse(parts[0]);
						if (shorthand != null) {
							for (StyledPropertyKey<?> longhand : shorthand.getLonghands()) {
								intermediate.addTransition(selector, longhand, curve);
							}
						}
					}
				}
			}
		}
	}
	
	private static List<String> splitTopLevel(String input, char delimiter) {
		List<String> parts = new ArrayList<>(2);
		int depth = 0, start = 0;
		
		for (int i = 0; i < input.length(); i++) {
			char character = input.charAt(i);
			if (character == '(') {
				depth++;
			} else if (character == ')') {
				depth--;
			} else if (character == delimiter && depth == 0) {
				parts.add(input.substring(start, i));
				start = i + 1;
			}
		}
		
		parts.add(input.substring(start));
		
		return parts;
	}
	
	@Override
	protected String preprocessInput(String input) {
		return super.preprocessInput(input.replaceAll("(?s)/\\*.*?\\*/", ""));
	}
	
}
