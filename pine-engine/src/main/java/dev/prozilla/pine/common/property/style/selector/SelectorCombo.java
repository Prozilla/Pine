package dev.prozilla.pine.common.property.style.selector;

import dev.prozilla.pine.core.component.canvas.RectTransform;

public class SelectorCombo extends Selector {
	
	private final Selector[] selectors;
	
	public SelectorCombo(Selector... selectors) {
		this.selectors = selectors;
	}
	
	@Override
	public boolean matches(RectTransform context) {
		for (Selector selector : selectors) {
			if (!selector.matches(context)) {
				return false;
			}
		}
		
		return true;
	}
	
	@Override
	public int getSpecificity() {
		int specificity = 0;
		for (Selector selector : selectors) {
			specificity += selector.getSpecificity();
		}
		return specificity;
	}
	
	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		for (Selector selector : selectors) {
			stringBuilder.append(selector.toString());
		}
		return stringBuilder.toString();
	}
	
}
