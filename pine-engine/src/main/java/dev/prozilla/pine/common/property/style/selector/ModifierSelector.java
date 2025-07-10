package dev.prozilla.pine.common.property.style.selector;

import dev.prozilla.pine.core.component.ui.Node;

/**
 * A selector that matches elements with a specific modifier.
 */
public class ModifierSelector extends Selector {

	private final String modifier;
	
	public ModifierSelector(String modifier) {
		this.modifier = modifier;
	}
	
	@Override
	public boolean matches(Node node) {
		return node.modifiers.contains(modifier);
	}
	
	@Override
	public int getSpecificity() {
		return 10;
	}
	
	@Override
	public String toString() {
		return ":" + modifier;
	}
	
	public final static ModifierSelector HOVER = new ModifierSelector(Node.HOVER_MODIFIER);
	public final static ModifierSelector FOCUS = new ModifierSelector(Node.FOCUS_MODIFIER);
	
	@Override
	public boolean equals(Selector other) {
		if (!(other instanceof ModifierSelector otherModifierSelector)) {
			return false;
		}
		
		return modifier.equals(otherModifierSelector.modifier);
	}
	
}
