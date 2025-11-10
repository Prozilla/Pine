package dev.prozilla.pine.common.property.style.selector;

import dev.prozilla.pine.common.util.StringUtils;
import dev.prozilla.pine.common.util.parser.SequentialParser;

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
			} else if (c == '>') {
				moveCursor();
				Selector parentSelector = createSelector(parts);
				parts.clear();
				if (parseRecursively(getRemainingInput())) {
					Selector childSelector = getResult();
					parts.add(new ChildSelector(parentSelector, childSelector));
					moveCursorToEnd();
				} else {
					return fail(getError());
				}
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
						return fail(getError());
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
				// Skip meaningless characters
				moveCursor();
			}
		}
		
		Selector result = createSelector(parts);
		if (result == null) {
			return fail(UNEXPECTED_END_OF_INPUT_ERROR);
		} else {
			return succeed(result);
		}
	}
	
	private static Selector createSelector(List<Selector> parts) {
		if (parts.isEmpty()) {
			return null;
		} else if (parts.size() == 1) {
			return parts.getFirst();
		} else {
			return new SelectorCombo(parts.toArray(new Selector[0]));
		}
	}
	
	private static boolean isValidNameChar(char character) {
		return Character.isLetterOrDigit(character) || character == '-' || character == '_';
	}
	
}
