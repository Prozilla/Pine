package dev.prozilla.pine.common.property.style.selector;

import dev.prozilla.pine.core.component.ui.Node;
import org.jetbrains.annotations.NotNull;

/**
 * A selector that matches elements with a specific tag.
 */
public class IdSelector extends Selector {
	
	private final String id;
	
	public IdSelector(String id) {
		this.id = id;
	}
	
	@Override
	public boolean matches(Node node) {
		return node.getEntity().hasTag(id);
	}
	
	@Override
	public int getSpecificity() {
		return 100;
	}
	
	@Override
	public @NotNull String toString() {
		return "#" + id;
	}
	
	@Override
	public boolean equals(Selector other) {
		if (!(other instanceof IdSelector otherIdSelector)) {
			return false;
		}
		
		return id.equals(otherIdSelector.id);
	}
	
}
