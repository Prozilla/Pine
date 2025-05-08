package dev.prozilla.pine.common.property.style;

import dev.prozilla.pine.common.property.animated.AnimationCurve;
import dev.prozilla.pine.common.property.style.selector.Selector;
import dev.prozilla.pine.common.util.Parser;

public class CSSParser extends Parser<StyleSheet> {
	
	@Override
	public boolean parse(String input) {
		// Remove comments
		input = input.replaceAll("/\\*.*?\\*/", "");
		
		StyleSheet styleSheet = new StyleSheet();
		int i = 0;
		int len = input.length();
		
		while (i < len) {
			i = skipWhitespace(input, i);
			if (i >= len) {
				break;
			}
			
			// Read selector (continue until "{")
			int start = i;
			while (i < len && input.charAt(i) != '{') {
				i++;
			}
			if (i >= len) {
				break;
			}
			
			Selector selector = Selector.parse(input.substring(start, i).trim());
			i++;
			
			// Read properties (continue until "}")
			while (i < len && input.charAt(i) != '}') {
				i = skipWhitespace(input, i);
				if (i >= len || input.charAt(i) == '}') {
					break;
				}
				
				// Read property name (continue until ":")
				start = i;
				while (i < len && input.charAt(i) != ':') {
					i++;
				}
				if (i >= len || input.charAt(i) == '}') {
					break;
				}
				
				String propertyName = input.substring(start, i).trim();
				StyledPropertyKey<?> propertyKey = StyledPropertyKey.parse(propertyName);
				i++;
				
				// Read property value (continue until ";" or "}")
				start = i;
				while (i < len && input.charAt(i) != ';' && input.charAt(i) != '}') {
					i++;
				}
				String value = input.substring(start, i).trim();
				
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
				if (i < len && input.charAt(i) == ';') {
					i++;
				}
			}
			
			// Skip "}"
			if (i < len && input.charAt(i) == '}') {
				i++;
			}
		}
		
		return succeed(styleSheet);
	}
	
	private static int skipWhitespace(String input, int i) {
		int len = input.length();
		while (i < len && Character.isWhitespace(input.charAt(i))) {
			i++;
		}
		return i;
	}
	
}
