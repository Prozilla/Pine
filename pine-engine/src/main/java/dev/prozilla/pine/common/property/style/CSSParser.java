package dev.prozilla.pine.common.property.style;

import dev.prozilla.pine.common.property.animated.AnimationCurve;
import dev.prozilla.pine.common.property.animated.AnimationCurveParser;
import dev.prozilla.pine.common.property.style.selector.Selector;
import dev.prozilla.pine.common.property.style.selector.SelectorParser;
import dev.prozilla.pine.common.util.parser.SequentialParser;

public class CSSParser extends SequentialParser<StyleSheet> {
	
	private static final SelectorParser selectorParser = new SelectorParser();
	private static final AnimationCurveParser animationCurveParser = new AnimationCurveParser();
	
	@Override
	public boolean parse(String input) {
		startStep(input, new StyleSheet());
		
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
				// Parse normal property
				intermediate.parseRule(selector, propertyKey, value);
			}
		}
	}
	
	private void parseTransitionProperty(String value, Selector selector) {
		StyledPropertyKey<?> propertyKey;
		for (String transitionValue : value.split(",")) {
			String[] parts = transitionValue.trim().split(" ", 2);
			
			if (parts.length == 2) {
				propertyKey = StyledPropertyKey.parse(parts[0]);
				
				if (animationCurveParser.parse(parts[1])) {
				    AnimationCurve animationCurve = animationCurveParser.getResult();
					
					if (propertyKey != null && animationCurve != null) {
						intermediate.addTransition(selector, propertyKey, animationCurve);
					}
				}
			}
		}
	}
	
	@Override
	protected void setInput(String input) {
		super.setInput(input.replaceAll("/\\*.*?\\*/", ""));
	}
	
}
