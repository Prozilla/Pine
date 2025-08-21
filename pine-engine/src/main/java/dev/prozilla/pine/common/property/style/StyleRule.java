package dev.prozilla.pine.common.property.style;

import dev.prozilla.pine.common.Printable;
import dev.prozilla.pine.common.property.style.selector.Selector;
import dev.prozilla.pine.core.component.ui.Node;
import org.jetbrains.annotations.NotNull;

/**
 * A rule that defines the value of a styled property of any node that matches its selector.
 * @param value The value this rule assigns to styled properties of elements that match its selector
 * @param <T> The type of the property
 */
public record StyleRule<T>(Selector selector, T value) implements Printable {
	
	/**
	 * Checks whether this rule applies to a given node.
	 * @param node The node
	 * @return True if the selectors of this rule matches the node.
	 */
	public boolean matches(Node node) {
		return selector.matches(node);
	}
	
	/**
	 * Returns the specificity of the selector of this rule.
	 * @return The specificity of the selector.
	 */
	public int getSpecificity() {
		return selector.getSpecificity();
	}
	
	@Override
	public @NotNull String toString() {
		return String.format("%s { %s }", selector, value);
	}
	
}
