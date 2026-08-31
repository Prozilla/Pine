package dev.prozilla.pine.common.property.style.selector;

import dev.prozilla.pine.common.util.ArrayUtils;
import dev.prozilla.pine.common.util.StringUtils;
import dev.prozilla.pine.core.component.ui.Node;
import org.jetbrains.annotations.NotNull;

/**
 * A selector that matches elements with a specific tag.
 */
public class AttributeSelector extends Selector {
	
	private final String name;
	private final Operator operator;
	private final String value;
	private final boolean caseSensitive;
	
	public AttributeSelector(String name) {
		this(name, null, null);
	}
	
	public AttributeSelector(String name, String value) {
		this(name, Operator.EQUALS, value);
	}
	
	public AttributeSelector(String name, Operator operator, String value) {
		this(name, operator, value, false);
	}
	
	public AttributeSelector(String name, Operator operator, String value, boolean caseSensitive) {
		this.name = StringUtils.toLowerCase(name);
		this.operator = operator;
		this.value = caseSensitive ? value : StringUtils.toLowerCase(value);
		this.caseSensitive = caseSensitive;
	}
	
	@Override
	public boolean matches(Node node) {
		if (operator == null) {
			return node.hasAttribute(name);
		}
		
		String value = node.getAttribute(name);
		return operator.compare(this.value, value, caseSensitive);
	}
	
	@Override
	public int getSpecificity() {
		return 10;
	}
	
	@Override
	public @NotNull String toString() {
		StringBuilder stringBuilder = new StringBuilder(name);
		if (operator != null) {
			stringBuilder.append(operator);
			if (value != null) {
				stringBuilder.append("\"");
				stringBuilder.append(value);
				stringBuilder.append("\"");
				if (caseSensitive) {
					stringBuilder.append(" s");
				}
			}
		}
		return stringBuilder.toString();
	}
	
	@Override
	public boolean equals(Selector other) {
		if (!(other instanceof AttributeSelector otherAttributeSelector)) {
			return false;
		}
		
		return value.equals(otherAttributeSelector.value);
	}
	
	public enum Operator {
		EQUALS("=") {
			@Override
			public boolean compare(String selectorValue, String nodeValue, boolean caseSensitive) {
				if (StringUtils.isEmpty(selectorValue)) {
					return StringUtils.isEmpty(nodeValue);
				}
				return caseSensitive ? selectorValue.equals(nodeValue) : selectorValue.equalsIgnoreCase(nodeValue);
			}
		},
		CONTAINS_WORD("~=") {
			@Override
			public boolean compare(String selectorValue, String nodeValue, boolean caseSensitive) {
				if (StringUtils.isEmpty(selectorValue)) {
					return true;
				} else if (StringUtils.isEmpty(nodeValue)) {
					return false;
				}
				
				return ArrayUtils.contains(nodeValue.split(" "), selectorValue);
			}
		},
		STARTS_WITH("^=") {
			@Override
			public boolean compare(String selectorValue, String nodeValue, boolean caseSensitive) {
				if (StringUtils.isEmpty(selectorValue)) {
					return true;
				} else if (StringUtils.isEmpty(nodeValue)) {
					return false;
				}
				
				if (!caseSensitive) {
					nodeValue = nodeValue.toLowerCase();
				}
				
				return nodeValue.startsWith(selectorValue);
			}
		},
		ENDS_WIDTH("$=") {
			@Override
			public boolean compare(String selectorValue, String nodeValue, boolean caseSensitive) {
				if (StringUtils.isEmpty(selectorValue)) {
					return true;
				} else if (StringUtils.isEmpty(nodeValue)) {
					return false;
				}
				
				if (!caseSensitive) {
					nodeValue = nodeValue.toLowerCase();
				}
				
				return nodeValue.endsWith(selectorValue);
			}
		},
		CONTAINS("*=") {
			@Override
			public boolean compare(String selectorValue, String nodeValue, boolean caseSensitive) {
				if (StringUtils.isEmpty(selectorValue)) {
					return true;
				} else if (StringUtils.isEmpty(nodeValue)) {
					return false;
				}
				
				if (!caseSensitive) {
					nodeValue = nodeValue.toLowerCase();
				}
				
				return nodeValue.contains(selectorValue);
			}
		};
		
		private final String string;
		
		Operator(String string) {
			this.string = string;
		}
		
		public abstract boolean compare(String selectorValue, String nodeValue, boolean caseSensitive);
		
		@Override
		public String toString() {
			return string;
		}
		
	}
	
}
