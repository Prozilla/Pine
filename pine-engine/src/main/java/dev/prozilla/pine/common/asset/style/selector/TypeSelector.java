package dev.prozilla.pine.common.asset.style.selector;

import dev.prozilla.pine.core.component.ui.Node;
import org.jetbrains.annotations.NotNull;

/**
 * A selector that matches elements with a specific name.
 */
public class TypeSelector extends Selector {

	private final String type;
	
	public static final TypeSelector P = new TypeSelector(Node.PARAGRAPH_TAG);
	public static final TypeSelector H1 = new TypeSelector(Node.HEADING_1_TAG);
	public static final TypeSelector H2 = new TypeSelector(Node.HEADING_2_TAG);
	public static final TypeSelector H3 = new TypeSelector(Node.HEADING_3_TAG);
	public static final TypeSelector H4 = new TypeSelector(Node.HEADING_4_TAG);
	public static final TypeSelector H5 = new TypeSelector(Node.HEADING_5_TAG);
	public static final TypeSelector H6 = new TypeSelector(Node.HEADING_6_TAG);
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
