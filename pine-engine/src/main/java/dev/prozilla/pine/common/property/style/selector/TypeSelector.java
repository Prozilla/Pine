package dev.prozilla.pine.common.property.style.selector;

import dev.prozilla.pine.core.component.ui.Node;
import org.jetbrains.annotations.NotNull;

/**
 * A selector that matches elements with a specific name.
 */
public class TypeSelector extends Selector {

	private final String type;
	
	public static final TypeSelector P = new TypeSelector(Node.PARAGRAPH_TAG);
	public static final TypeSelector BUTTON = new TypeSelector(Node.BUTTON_TAG);
	public static final TypeSelector INPUT = new TypeSelector(Node.INPUT_TAG);
	
	public TypeSelector(String type) {
		this.type = type.toLowerCase();
	}
	
	@Override
	public boolean matches(Node node) {
		return type.equals(node.htmlTag);
	}
	
	@Override
	public int getSpecificity(Node node) {
		return 1;
	}
	
	@Override
	public @NotNull String toString() {
		return type;
	}
	
	@Override
	public boolean equals(Selector other) {
		if (!(other instanceof TypeSelector otherTypeSelector)) {
			return false;
		}
		
		return type.equals(otherTypeSelector.type);
	}
	
}
