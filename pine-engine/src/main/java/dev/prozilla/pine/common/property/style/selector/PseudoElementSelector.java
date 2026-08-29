package dev.prozilla.pine.common.property.style.selector;

import dev.prozilla.pine.core.component.ui.Node;
import dev.prozilla.pine.core.component.ui.TextInputNode;
import org.jetbrains.annotations.NotNull;

/**
 * A selector that matches pseudo-elements.
 */
public class PseudoElementSelector extends Selector {

	private final String name;
	
	public static final PseudoElementSelector PLACEHOLDER = new PseudoElementSelector(TextInputNode.PLACEHOLDER_ELEMENT);
	
	public PseudoElementSelector(String name) {
		this.name = name;
	}
	
	@Override
	public boolean matches(Node node) {
		return node.pseudoName != null && node.pseudoName.equals(name);
	}
	
	@Override
	public int getSpecificity() {
		return 10;
	}
	
	@Override
	public @NotNull String toString() {
		return "::" + name;
	}
	
	@Override
	public boolean equals(Selector other) {
		if (!(other instanceof PseudoElementSelector otherPseudoElementSelector)) {
			return false;
		}
		
		return name.equals(otherPseudoElementSelector.name);
	}
	
}
