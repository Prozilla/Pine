package dev.prozilla.pine.common.asset.style;

import dev.prozilla.pine.common.asset.style.selector.Selector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class StyledPropertyShorthand {
	
	private final String name;
	private final StyledPropertyKey<?>[] longhands;
	
	private static final Map<String, StyledPropertyShorthand> REGISTRY = new HashMap<>();
	
	public static final StyledPropertyShorthand BORDER = new StyledPropertyShorthand("border", StyledPropertyKey.BORDER_STYLE, StyledPropertyKey.BORDER_WIDTH, StyledPropertyKey.BORDER_COLOR);
	public static final StyledPropertyShorthand OUTLINE = new StyledPropertyShorthand("outline", StyledPropertyKey.OUTLINE_STYLE, StyledPropertyKey.OUTLINE_WIDTH, StyledPropertyKey.OUTLINE_COLOR);
	public static final StyledPropertyShorthand FONT = new StyledPropertyShorthand("font", StyledPropertyKey.FONT_SIZE, StyledPropertyKey.FONT_FAMILY);
	
	private StyledPropertyShorthand(String name, StyledPropertyKey<?>... longhands) {
		this.name = name;
		this.longhands = longhands;
		REGISTRY.put(name, this);
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	public StyledPropertyKey<?>[] getLonghands() {
		return longhands;
	}
	
	/**
	 * Expands this shorthand into longhand rules.
	 */
	public boolean apply(StyleSheet sheet, Selector selector, String input) {
		List<String> tokens = tokenize(input);
		if (tokens.isEmpty() || tokens.size() > longhands.length) {
			return false;
		}
		
		Assignment<?>[] assignments = new Assignment<?>[longhands.length];
		
		for (String token : tokens) {
			boolean matched = false;
			
			for (int i = 0; i < longhands.length && !matched; i++) {
				if (assignments[i] == null) {
					Assignment<?> assignment = Assignment.parse(longhands[i], token);
					if (assignment != null) {
						assignments[i] = assignment;
						matched = true;
					}
				}
			}
			
			if (!matched) {
				return false;
			}
		}
		
		for (Assignment<?> assignment : assignments) {
			if (assignment != null) {
				assignment.apply(sheet, selector);
			}
		}
		
		return true;
	}
	
	private static List<String> tokenize(String input) {
		List<String> tokens = new ArrayList<>(4);
		int depth = 0;
		int start = -1;
		
		for (int i = 0; i < input.length(); i++) {
			char character = input.charAt(i);
			if (character == '(') {
				depth++;
			} else if (character == ')') {
				depth--;
			}
			
			if (depth == 0 && Character.isWhitespace(character)) {
				if (start >= 0) {
					tokens.add(input.substring(start, i));
					start = -1;
				}
			} else if (start < 0) {
				start = i;
			}
		}
		
		if (start >= 0) {
			tokens.add(input.substring(start));
		}
		
		return tokens;
	}
	
	public static StyledPropertyShorthand parse(String name) {
		return REGISTRY.get(name.toLowerCase());
	}
	
	private record Assignment<T>(StyledPropertyKey<T> key, T value) {
		
		static <T> Assignment<T> parse(StyledPropertyKey<T> key, String token) {
			try {
				T value = key.parseValue(token);
				return value != null ? new Assignment<>(key, value) : null;
			} catch (Exception e) {
				return null;
			}
		}
		
		void apply(StyleSheet sheet, Selector selector) {
			sheet.addRule(selector, key, value);
		}
		
	}
	
}