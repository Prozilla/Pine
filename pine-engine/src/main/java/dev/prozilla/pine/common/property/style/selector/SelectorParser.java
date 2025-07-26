package dev.prozilla.pine.common.property.style.selector;

import dev.prozilla.pine.common.util.Parser;
import dev.prozilla.pine.common.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class SelectorParser extends Parser<Selector> {
	
	@Override
	public boolean parse(String input) {
		input = input.trim();
		List<Selector> parts = new ArrayList<>();
		
		int i = 0;
		while (i < input.length()) {
			char c = input.charAt(i);
			
			if (c == '*') {
				parts.add(Selector.UNIVERSAL);
				i++;
			} else if (c == '.') {
				int start = i + 1;
				while (++i < input.length() && isValidNameChar(input.charAt(i))) {}
				parts.add(new ClassSelector(input.substring(start, i)));
			} else if (c == '#') {
				int start = i + 1;
				while (++i < input.length() && isValidNameChar(input.charAt(i))) {}
				parts.add(new IdSelector(input.substring(start, i)));
			} else if (c == ':') {
				if (input.startsWith(":not(", i)) {
					int start = i + 5;
					int end = StringUtils.findClosingParenthesis(input, start - 1);
					if (end == -1) throw new IllegalArgumentException("Unmatched :not()");
					String inner = input.substring(start, end);
					if (parse(inner)) {
						parts.add(new NotSelector(getResult()));
						i = end + 1;
					} else {
						return false;
					}
				} else {
					int start = i + 1;
					while (++i < input.length() && isValidNameChar(input.charAt(i))) {}
					parts.add(new ModifierSelector(input.substring(start, i)));
				}
			} else if (isValidNameChar(c)) {
				// Type selector
				int start = i;
				while (i < input.length() && isValidNameChar(input.charAt(i))) i++;
				parts.add(new TypeSelector(input.substring(start, i)));
			} else {
				// Skip unknown or invalid characters
				i++;
			}
		}
		
		if (parts.size() == 1) {
			return succeed(parts.getFirst());
		} else {
			return succeed(new SelectorCombo(parts.toArray(new Selector[0])));
		}
	}
	
	private static boolean isValidNameChar(char c) {
		return Character.isLetterOrDigit(c) || c == '-' || c == '_';
	}
	
}
