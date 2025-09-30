package dev.prozilla.pine.common.property.style.selector;

import dev.prozilla.pine.common.Printable;
import dev.prozilla.pine.core.component.ui.Node;
import org.jetbrains.annotations.NotNull;

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
		public int getSpecificity() {
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
	
	/**
	 * @deprecated Replaced by {@link SelectorParser} as of 2.0.2
	 */
	@Deprecated
	public static Selector parse(String input) {
		return new SelectorParser().read(input);
	}
	
}
