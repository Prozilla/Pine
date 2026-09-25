package dev.prozilla.pine.common.property.style.selector;

import dev.prozilla.pine.common.Printable;
import dev.prozilla.pine.core.component.ui.Node;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * A selector for nodes based on <a href="https://developer.mozilla.org/en-US/docs/Web/CSS/CSS_selectors">CSS selectors</a>.
 */
public abstract class Selector implements Printable {
	
	public Node query(Node node) {
		if (matches(node)) {
			return node;
		}
		
		for (Node child : node.children) {
			Node match = query(child);
			if (match != null) {
				return match;
			}
		}
		
		return null;
	}
	
	public void queryAll(Node node, List<Node> out) {
		if (matches(node)) {
			out.add(node);
		}
		
		for (Node child : node.children) {
			queryAll(child, out);
		}
	}
	
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
	public abstract int getSpecificity(Node node);
	
	/**
	 * Creates a selector list containing this selector and the given selector.
	 * @return The new selector list.
	 */
	public SelectorList or(Selector selector) {
		return new SelectorList(this, selector);
	}
	
	@Override
	public boolean equals(Object other) {
		return this == other || (other instanceof Selector otherSelector && equals(otherSelector));
	}
	
	public abstract boolean equals(Selector other);
	
	public abstract @NotNull String toString();
	
	/**
	 * Matches all elements.
	 */
	public static final Selector UNIVERSAL = new Selector() {
		@Override
		public boolean matches(Node node) {
			return true;
		}
		
		@Override
		public int getSpecificity(Node node) {
			return 0;
		}
		
		@Override
		public @NotNull String toString() {
			return "*";
		}
		
		@Override
		public boolean equals(Selector other) {
			return this == other;
		}
	};
	
}
