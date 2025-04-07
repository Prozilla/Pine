package dev.prozilla.pine.common.property.style.selector;

import dev.prozilla.pine.core.component.canvas.RectTransform;

/**
 * A selector that negates another selector.
 */
public class NotSelector extends Selector {
	
	private final Selector selector;
	
	public NotSelector(Selector selector) {
		this.selector = selector;
	}
	
	@Override
	public boolean matches(RectTransform context) {
		return !selector.matches(context);
	}
	
	@Override
	public int getSpecificity() {
		return selector.getSpecificity();
	}
	
	@Override
	public String toString() {
		return String.format(":not(%s)", selector);
	}
	
}
