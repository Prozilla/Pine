package dev.prozilla.pine.common.property.style.selector;

import dev.prozilla.pine.common.Printable;
import dev.prozilla.pine.core.component.canvas.RectTransform;

/**
 * A selector for canvas elements based on <a href="https://developer.mozilla.org/en-US/docs/Web/CSS/CSS_selectors">CSS selectors</a>.
 */
public abstract class Selector implements Printable {
	
	/**
	 * Checks whether this selector matches a given canvas element.
	 * @param context The canvas element
	 * @return True if this selector matches the canvas element.
	 */
	public abstract boolean matches(RectTransform context);
	
	/**
	 * Returns an integer representing the specificity of this selector.
	 * @return The specificity of this selector
	 */
	public abstract int getSpecificity();
	
	/**
	 * Matches all elements.
	 */
	public static final Selector UNIVERSAL = new Selector() {
		@Override
		public boolean matches(RectTransform context) {
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
	};
	
}
