package dev.prozilla.pine.common.asset.view;

import dev.prozilla.pine.common.asset.pool.AssetPools;
import dev.prozilla.pine.common.asset.style.CSSParser;
import dev.prozilla.pine.common.asset.style.StyleSheet;
import dev.prozilla.pine.common.util.ArrayUtils;
import dev.prozilla.pine.common.util.StringUtils;
import dev.prozilla.pine.common.util.parser.SequentialParser;
import dev.prozilla.pine.core.component.ui.Node;
import dev.prozilla.pine.core.entity.prefab.ui.*;

import java.util.*;

public class HTMLParser extends SequentialParser<NodePrefab> {
	
	protected List<NodePrefab> children;
	protected boolean ignoreChild;
	
	protected String title;
	protected Set<StyleSheet> styleSheets;
	protected Controller controller;
	
	public static final String MISSING_CLOSING_TAG_ERROR = "Missing closing tag";
	
	// Tags
	public static final String INCLUDE_TAG = "include";
	public static final String[] METADATA_TAGS = new String[] {
		Node.HEAD_TAG,
		Node.STYLE_TAG,
		Node.TITLE_TAG,
		Node.LINK_TAG
	};
	
	private static final CSSParser cssParser = new CSSParser();
	
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
			return succeed(createEmptyPrefab());
		}
		skipWhitespace();
		
		if (endOfInput()) {
			return fail(String.format("%s of %s", MISSING_CLOSING_TAG_ERROR, tag));
		}
		
		ignoreChild = ArrayUtils.contains(METADATA_TAGS, tag);
		
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
		boolean ignoreChild = this.ignoreChild;
		
		// Recurse
		intermediate = null;
		this.children = null;
		this.ignoreChild = false;
		
		boolean success = parse();
		if (success) {
			NodePrefab result = getResult();
			if (!this.ignoreChild) {
				if (children == null) {
					children = new ArrayList<>();
				}
				children.add(result);
			}
		}
		
		// Restore
		intermediate = parent;
		this.children = children;
		this.ignoreChild = ignoreChild;
		
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
			case Node.PARAGRAPH_TAG,
			     Node.HEADING_1_TAG,
			     Node.HEADING_2_TAG,
			     Node.HEADING_3_TAG,
			     Node.HEADING_4_TAG,
			     Node.HEADING_5_TAG,
			     Node.HEADING_6_TAG -> createTextPrefab(tag, text);
			case Node.BUTTON_TAG -> new TextButtonPrefab(text);
			case Node.INPUT_TAG -> createInputPrefab(attributes);
			case Node.DIV_TAG,
			     Node.SPAN_TAG,
			     Node.HTML_TAG,
			     Node.HEAD_TAG,
			     Node.HEADER_TAG,
			     Node.BODY_TAG,
			     Node.FOOTER_TAG -> new LayoutPrefab(tag);
			case Node.TITLE_TAG -> createTitlePrefab(text);
			case Node.STYLE_TAG -> createStylePrefab(text);
			case Node.LINK_TAG -> createLinkPrefab(attributes);
			case INCLUDE_TAG -> include(attributes.get("src"));
			default -> createEmptyPrefab();
		};
		
		prefab.setAttributes(attributes);
		
		if (children != null) {
			prefab.addChildren(children);
		}
		
		return prefab;
	}
	
	private TextPrefab createTextPrefab(String tag, String text) {
		TextPrefab textPrefab = new TextPrefab(text);
		textPrefab.setHTMLTag(tag);
		return textPrefab;
	}
	
	private NodePrefab createInputPrefab(Map<String, String> attributes) {
		return "range".equals(attributes.get(Node.TYPE_ATTRIBUTE)) ? createRangeInputPrefab(attributes) : createTextInputPrefab(attributes);
	}
	
	private NodePrefab createRangeInputPrefab(Map<String, String> attributes) {
		RangeInputPrefab rangeInputPrefab = new RangeInputPrefab();
		String value = attributes.get(Node.VALUE_ATTRIBUTE);
		if (value != null) {
			try {
				rangeInputPrefab.setValue(Float.parseFloat(value));
			} catch (NumberFormatException ignored) {}
		}
		return rangeInputPrefab;
	}
	
	private NodePrefab createTextInputPrefab(Map<String, String> attributes) {
		TextInputPrefab textInputPrefab = new TextInputPrefab();
		textInputPrefab.setText(attributes.get(Node.VALUE_ATTRIBUTE));
		return textInputPrefab;
	}
	
	private NodePrefab createTitlePrefab(String text) {
		if (title == null) {
			title = text;
		}
		return createEmptyPrefab();
	}
	
	private NodePrefab createStylePrefab(String text) {
		if (!StringUtils.isBlank(text) && cssParser.parse(text)) {
			addStyleSheet(cssParser.getResult());
		}
		return createEmptyPrefab();
	}
	
	private NodePrefab createLinkPrefab(Map<String, String> attributes) {
		Set<String> rel = parseRelAttribute(attributes);
		if (rel.contains("stylesheet")) {
			String href = attributes.get(Node.HREF_ATTRIBUTE);
			if (href != null) {
				addStyleSheet(AssetPools.styleSheets.load(href));
			}
		}
		return createEmptyPrefab();
	}
	
	private NodePrefab include(String path) {
		if (path == null) {
			return createIgnoredPrefab();
		}
		
		// Save
		NodePrefab parent = intermediate;
		List<NodePrefab> children = this.children;
		boolean ignoreChild = this.ignoreChild;
		String title = this.title;
		Set<StyleSheet> styleSheets = this.styleSheets;
		Controller controller = this.controller;
		
		// Recurse
		intermediate = null;
		this.children = null;
		this.ignoreChild = false;
		this.title = null;
		this.styleSheets = null;
		this.controller = null;
		
		View view = AssetPools.views.load(path);
		
		if (view.controller != null) {
			controller = controller != null ? controller.then(view.controller) : view.controller;
		}
		
		// Restore
		this.intermediate = parent;
		this.children = children;
		this.ignoreChild = ignoreChild;
		this.title = title;
		this.styleSheets = styleSheets;
		this.controller = controller;
		
		return view.prefab;
	}
	
	private NodePrefab createIgnoredPrefab() {
		ignoreChild = true;
		return createEmptyPrefab();
	}
	
	private NodePrefab createEmptyPrefab() {
		return new NodePrefab();
	}
	
	private Set<String> parseRelAttribute(Map<String, String> attributes) {
		String relAttribute = attributes.get(Node.REL_ATTRIBUTE);
		if (relAttribute == null) {
			return Collections.emptySet();
		}
		String[] keywords = relAttribute.toLowerCase().split("\\s+");
		return new HashSet<>(Arrays.asList(keywords));
	}
	
	private void addStyleSheet(StyleSheet styleSheet) {
		if (styleSheets == null) {
			styleSheets = new HashSet<>();
		}
		styleSheets.add(styleSheet);
	}
	
	public Set<StyleSheet> getStyleSheets() {
		Set<StyleSheet> styleSheets = this.styleSheets;
		this.styleSheets = null;
		return styleSheets;
	}
	
	public String getTitle() {
		String title = this.title;
		this.title = null;
		return title;
	}
	
	public Controller getController() {
		Controller controller = this.controller;
		this.controller = null;
		return controller;
	}
	
}
