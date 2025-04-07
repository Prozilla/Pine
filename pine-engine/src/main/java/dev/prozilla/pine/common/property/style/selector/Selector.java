package dev.prozilla.pine.common.property.style.selector;

import dev.prozilla.pine.common.Printable;
import dev.prozilla.pine.core.component.canvas.RectTransform;

/**
 * A selector for canvas elements based on <a href="https://developer.mozilla.org/en-US/docs/Web/CSS/CSS_selectors">CSS selectors</a>
 */
public abstract class Selector implements Printable {
	
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
	
	public abstract boolean matches(RectTransform context);
	
	public abstract int getSpecificity();
	
}
