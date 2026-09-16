package dev.prozilla.pine.core.entity.prefab.ui;

import dev.prozilla.pine.common.property.style.StyleSheet;
import dev.prozilla.pine.common.util.parser.SequentialParser;
import dev.prozilla.pine.core.component.ui.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HTMLParser extends SequentialParser<NodePrefab> {
	
	protected List<NodePrefab> children;
	
	protected String title;
	protected List<StyleSheet> styleSheets; // TODO: parse style sheets
	
	public static final String MISSING_CLOSING_TAG_ERROR = "Missing closing tag";
	
	@Override
	public boolean parse(String input) {
		startStep(input, null);
		reset();
		return parse();
	}
	
	private boolean parse() {
		skipWhitespace();
		if (endOfInput() || getChar() != '<') {
			return fail(UNEXPECTED_END_OF_INPUT_ERROR);
		}
		moveCursor();
		
		String tag = readWhile((character) -> !Character.isWhitespace(character) && character != '>' && character != '/')
            .toLowerCase();
		if (tag.isEmpty()) {
			return succeed(new NodePrefab());
		}
		skipWhitespace();
		
		if (endOfInput()) {
			return fail(String.format("%s of %s", MISSING_CLOSING_TAG_ERROR, tag));
		}
		
		Map<String, String> attributes = parseAttributes();
		
		if (endOfInput()) {
			return succeed(createPrefab(tag, attributes));
		}
		
		boolean selfClosing = !endOfInput() && getChar() == '/';
		if (selfClosing) {
			moveCursor();
		}
		if (endOfInput()) {
			return fail(UNEXPECTED_END_OF_INPUT_ERROR);
		} else if (getChar() != '>') {
			return fail(MISSING_CLOSING_TAG_ERROR);
		}
		moveCursor();
		if (selfClosing) {
			return succeed(createPrefab(tag, attributes));
		}
		
		// Parse children and text content
		StringBuilder textContent = new StringBuilder();
		while (!endOfInput()) {
			textContent.append(readWhile((character) -> character != '<'));
			moveCursor();
			
			if (endOfInput()) {
				break;
			}
			
			if (getChar() == '/') {
				skipUntilChar('>');
				moveCursor();
				break;
			}
			
			moveCursor(-1);
			if (!parseChild()) {
				return fail(getError());
			}
		}
		
		String text = normalizeText(textContent.toString());
		return succeed(createPrefab(tag, attributes, text));
	}
	
	private Map<String, String> parseAttributes() {
		Map<String, String> attributes = new HashMap<>();
		
		while (!endOfInput() && getChar() != '>' && getChar() != '/') {
			skipWhitespace();
			String key = readWhile((character) -> !Character.isWhitespace(character) && character != '=' && character != '>' && character != '/');
			String value = "";
			if (!endOfInput() && getChar() == '=') {
				moveCursor();
				value = readBetweenQuotes();
				moveCursor();
			}
			attributes.put(key, value);
		}
		
		return attributes;
	}
	
	private boolean parseChild() {
		// Save
		NodePrefab parent = intermediate;
		List<NodePrefab> children = this.children;
		
		// Recurse
		intermediate = null;
		this.children = null;
		
		boolean success = parse();
		if (success) {
			if (children == null) {
				children = new ArrayList<>();
			}
			children.add(getResult());
		}
		
		// Restore
		intermediate = parent;
		this.children = children;
		
		return success;
	}
	
	private String normalizeText(String rawText) {
		String collapsed = rawText.replaceAll("\\s+", " ").trim();
		return replaceCharacterReferences(collapsed);
	}
	
	private String replaceCharacterReferences(String text) {
		return text
			.replace("&lt;", "<")
			.replace("&gt;", ">")
			.replace("&quot;", "\"")
			.replace("&#39;", "'")
			.replace("&apos;", "'")
			.replace("&nbsp;", " ")
			.replace("&amp;", "&");
	}
	
	@Override
	protected void setInput(String input) {
		super.setInput(input.replaceAll("<!--.*-->", ""));
	}
	
	@Override
	protected boolean fail(String errorMessage) {
		reset();
		return super.fail(errorMessage);
	}
	
	protected void reset() {
		title = null;
		styleSheets = null;
		children = null;
	}
	
	private NodePrefab createPrefab(String tag, Map<String, String> attributes) {
		return createPrefab(tag, attributes, null);
	}
	
	private NodePrefab createPrefab(String tag, Map<String, String> attributes, String text) {
		NodePrefab prefab = switch (tag) {
			case Node.PARAGRAPH_TAG -> new TextPrefab(text);
			case Node.BUTTON_TAG -> new TextButtonPrefab(text);
			case Node.INPUT_TAG -> "range".equals(attributes.get(Node.TYPE_ATTRIBUTE)) ? new RangeInputPrefab() : new TextInputPrefab(text);
			case Node.DIV_TAG -> new LayoutPrefab();
			case Node.TITLE_TAG -> {
				if (title == null) {
					title = text;
				}
				yield new NodePrefab();
			}
			default -> new NodePrefab();
		};
		
		prefab.setAttributes(attributes);
		
		if (children != null) {
			prefab.addChildren(children);
		}
		
		return prefab;
	}
	
	public List<StyleSheet> getStyleSheets() {
		List<StyleSheet> styleSheets = this.styleSheets;
		this.styleSheets = null;
		return styleSheets;
	}
	
	public String getTitle() {
		String title = this.title;
		this.title = null;
		return title;
	}
	
}
