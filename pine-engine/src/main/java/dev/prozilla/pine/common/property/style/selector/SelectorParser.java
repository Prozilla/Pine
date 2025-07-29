package dev.prozilla.pine.common.property.style.selector;

import dev.prozilla.pine.common.util.SequentialParser;
import dev.prozilla.pine.common.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class SelectorParser extends SequentialParser<Selector> {
	
	@Override
	public boolean parse(String input) {
		setInput(input.trim());
		List<Selector> parts = new ArrayList<>();
		
		while (!endOfInput()) {
			char c = getChar();
			
			if (c == '*') {
				parts.add(Selector.UNIVERSAL);
				moveCursor();
			} else if (c == '.') {
				moveCursor();
				String className = readWhile(SelectorParser::isValidNameChar);
				if (className.isEmpty()) {
					return fail("Invalid class selector");
				}
				parts.add(new ClassSelector(className));
			} else if (c == '#') {
				moveCursor(); // skip '#'
				String id = readWhile(SelectorParser::isValidNameChar);
				if (id.isEmpty()) {
					return fail("Invalid id selector");
				}
				parts.add(new IdSelector(id));
			} else if (c == ':') {
				if (getInput().startsWith(":not(", getCursor())) {
					moveCursor(5);
					int end = StringUtils.findClosingParenthesis(input, getCursor() - 1);
					if (end == -1) {
						return fail("Unmatched :not()");
					}
					String inner = input.substring(getCursor(), end);
					if (parseRecursively(inner)) {
						parts.add(new NotSelector(getResult()));
						setCursor(end + 1);
					} else {
						fail(getError());
					}
				} else {
					moveCursor(); // skip ':'
					String modifier = readWhile(SelectorParser::isValidNameChar);
					if (modifier.isEmpty()) {
						return fail("Invalid modifier selector");
					}
					parts.add(new ModifierSelector(modifier));
				}
			} else if (isValidNameChar(c)) {
				// Type selector
				String typeName = readWhile(SelectorParser::isValidNameChar);
				parts.add(new TypeSelector(typeName));
			} else {
				// Skip unknown or invalid characters
				moveCursor();
			}
		}
		
		if (parts.isEmpty()) {
			return fail("Empty selector");
		} else if (parts.size() == 1) {
			return succeed(parts.getFirst());
		} else {
			return succeed(new SelectorCombo(parts.toArray(new Selector[0])));
		}
	}
	
	private static boolean isValidNameChar(char character) {
		return Character.isLetterOrDigit(character) || character == '-' || character == '_';
	}
	
}
