package dev.prozilla.pine.common.property.style.selector;

import dev.prozilla.pine.core.component.ui.Node;
import org.jetbrains.annotations.NotNull;

/**
 * A selector that negates another selector.
 */
public class NotSelector extends Selector {
	
	private final Selector selector;
	
	public NotSelector(Selector selector) {
		this.selector = selector;
	}
	
	@Override
	public boolean matches(Node node) {
		return !selector.matches(node);
	}
	
	@Override
	public int getSpecificity() {
		return selector.getSpecificity();
	}
	
	@Override
	public @NotNull String toString() {
		return String.format(":not(%s)", selector);
	}
	
	@Override
	public boolean equals(Selector other) {
		if (!(other instanceof NotSelector otherNotSelector)) {
			return false;
		}
		
		return selector.equals(otherNotSelector.selector);
	}
}
