package dev.prozilla.pine.common.property.style.selector;

import dev.prozilla.pine.common.util.ArrayUtils;
import dev.prozilla.pine.core.component.ui.Node;
import org.jetbrains.annotations.NotNull;

import java.util.StringJoiner;

/**
 * A list of selectors.
 * 
 * <p>This selector matches a node if any selector in the list matches the node.</p>
 */
public class SelectorList extends Selector {
	
	private final Selector[] selectors;
	
	public SelectorList(Selector... selectors) {
		this.selectors = selectors;
	}
	
	@Override
	public boolean matches(Node node) {
		for (Selector selector : selectors) {
			if (selector.matches(node)) {
				return true;
			}
		}
		
		return false;
	}
	
	@Override
	public int getSpecificity(Node node) {
		for (Selector selector : selectors) {
			if (selector.matches(node)) {
				return selector.getSpecificity(node);
			}
		}
		
		return 0;
	}
	
	@Override
	public SelectorList or(Selector selector) {
		return new SelectorList(ArrayUtils.add(selectors, selector));
	}
	
	@Override
	public @NotNull String toString() {
		StringJoiner stringJoiner = new StringJoiner(", ");
		for (Selector selector : selectors) {
			stringJoiner.add(selector.toString());
		}
		return stringJoiner.toString();
	}
	
	@Override
	public boolean equals(Selector other) {
		if (!(other instanceof SelectorList otherSelectorList)) {
			return false;
		}
		
		Selector[] otherSelectors = otherSelectorList.selectors;
		
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
