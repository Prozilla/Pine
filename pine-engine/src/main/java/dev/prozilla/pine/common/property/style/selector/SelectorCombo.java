package dev.prozilla.pine.common.property.style.selector;

import dev.prozilla.pine.core.component.ui.Node;
import org.jetbrains.annotations.NotNull;

/**
 * A selector that combines multiple other selectors.
 * 
 * <p>This selector matches a node if all selectors match the node.</p>
 */
public class SelectorCombo extends Selector {
	
	private final Selector[] selectors;
	
	public SelectorCombo(Selector... selectors) {
		this.selectors = selectors;
	}
	
	@Override
	public boolean matches(Node node) {
		for (Selector selector : selectors) {
			if (!selector.matches(node)) {
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
	public @NotNull String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		for (Selector selector : selectors) {
			stringBuilder.append(selector.toString());
		}
		return stringBuilder.toString();
	}
	
	@Override
	public boolean equals(Selector other) {
		if (!(other instanceof SelectorCombo otherSelectorCombo)) {
			return false;
		}
		
		Selector[] otherSelectors = otherSelectorCombo.selectors;
		
		if (selectors.length != otherSelectors.length) {
			return false;
		}
		
		for (int i = 0; i < selectors.length; i++) {
			if (!selectors[i].equals(otherSelectors[i])) {
				return false;
			}
		}
		
		return true;
	}
	
}
