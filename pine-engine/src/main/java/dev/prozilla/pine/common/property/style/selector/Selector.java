package dev.prozilla.pine.common.property.style.selector;

import dev.prozilla.pine.common.Printable;
import dev.prozilla.pine.common.util.StringUtils;
import dev.prozilla.pine.core.component.ui.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * A selector for nodes based on <a href="https://developer.mozilla.org/en-US/docs/Web/CSS/CSS_selectors">CSS selectors</a>.
 */
public abstract class Selector implements Printable {
	
	/**
	 * Checks whether this selector matches a given node.
	 * @param node The node
	 * @return True if this selector matches the node.
	 */
	public abstract boolean matches(Node node);
	
	/**
	 * Returns an integer representing the specificity of this selector.
	 * @return The specificity of this selector
	 */
	public abstract int getSpecificity();
	
	@Override
	public boolean equals(Object other) {
		return this == other || (other instanceof Selector otherSelector && equals(otherSelector));
	}
	
	public abstract boolean equals(Selector other);
	
	/**
	 * Matches all elements.
	 */
	public static final Selector UNIVERSAL = new Selector() {
		@Override
		public boolean matches(Node node) {
			return true;
		}
		
		@Override
		public int getSpecificity() {
			return 0;
		}
		
		@Override
		public String toString() {
			return "*";
		}
		
		@Override
		public boolean equals(Selector other) {
			return this == other;
		}
	};
	
	public static Selector parse(String input) {
		input = input.trim();
		List<Selector> parts = new ArrayList<>();
		
		int i = 0;
		while (i < input.length()) {
			char c = input.charAt(i);
			
			if (c == '*') {
				parts.add(UNIVERSAL);
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
					parts.add(new NotSelector(parse(inner)));
					i = end + 1;
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
			return parts.getFirst();
		} else {
			return new SelectorCombo(parts.toArray(new Selector[0]));
		}
	}
	
	private static boolean isValidNameChar(char c) {
		return Character.isLetterOrDigit(c) || c == '-' || c == '_';
	}
	
}
