package dev.prozilla.pine.common.property.style;

import dev.prozilla.pine.common.Printable;
import dev.prozilla.pine.common.property.style.selector.Selector;
import dev.prozilla.pine.core.component.canvas.RectTransform;

/**
 * A rule that defines the value of a styled property of any canvas element that matches its selector.
 * @param value The value this rule assigns to styled properties of elements that match its selector
 * @param <T> The type of the property
 */
public record StyleRule<T>(Selector selector, T value) implements Printable {
	
	/**
	 * Checks whether this rule applies to a given canvas element.
	 * @param context The canvas element
	 * @return True if the selectors of this rule matches the canvas element.
	 */
	public boolean matches(RectTransform context) {
		return selector.matches(context);
	}
	
	/**
	 * Returns the specificity of the selector of this rule.
	 * @return The specificity of the selector.
	 */
	public int getSpecificity() {
		return selector.getSpecificity();
	}
	
	@Override
	public String toString() {
		return String.format("%s { %s }", selector, value);
	}
	
}
