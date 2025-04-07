package dev.prozilla.pine.common.property.style;

import dev.prozilla.pine.common.Printable;
import dev.prozilla.pine.common.property.style.selector.Selector;
import dev.prozilla.pine.core.component.canvas.RectTransform;

public class StyleRule<T> implements Printable {
	
	private final Selector selector;
	private final T value;
	
	public StyleRule(Selector selector, T value) {
		this.selector = selector;
		this.value = value;
	}
	
	public boolean matches(RectTransform context) {
		return selector.matches(context);
	}
	
	public int getSpecificity() {
		return selector.getSpecificity();
	}
	
	public Selector getSelector() {
		return selector;
	}
	
	public T getValue() {
		return value;
	}
	
	@Override
	public String toString() {
		return String.format("%s { %s }", selector, value);
	}
}
