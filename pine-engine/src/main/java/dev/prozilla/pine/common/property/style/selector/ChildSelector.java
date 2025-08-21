package dev.prozilla.pine.common.property.style.selector;

import dev.prozilla.pine.core.component.ui.Node;
import org.jetbrains.annotations.NotNull;

/**
 * A selector that matches elements with a specific tag.
 */
public class ChildSelector extends Selector {
	
	private final Selector parentSelector;
	private final Selector childSelector;
	
	public ChildSelector(Selector parentSelector, Selector childSelector) {
		this.parentSelector = parentSelector;
		this.childSelector = childSelector;
	}
	
	@Override
	public boolean matches(Node node) {
		return node.parent != null && parentSelector.matches(node.parent) && childSelector.matches(node);
	}
	
	@Override
	public int getSpecificity() {
		return childSelector.getSpecificity();
	}
	
	@Override
	public @NotNull String toString() {
		return String.format("%s > %s", parentSelector, childSelector);
	}
	
	@Override
	public boolean equals(Selector other) {
		if (!(other instanceof ChildSelector otherChildSelector)) {
			return false;
		}
		
		return childSelector.equals(otherChildSelector.childSelector) && parentSelector.equals(otherChildSelector.parentSelector);
	}
	
}
