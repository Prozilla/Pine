package dev.prozilla.pine.common.property.style.selector;

import dev.prozilla.pine.core.component.canvas.RectTransform;

/**
 * A selector that matches elements with a specific modifier.
 */
public class ModifierSelector extends Selector {

	private final String modifier;
	
	public ModifierSelector(String modifier) {
		this.modifier = modifier;
	}
	
	@Override
	public boolean matches(RectTransform context) {
		return context.modifiers.contains(modifier);
	}
	
	@Override
	public int getSpecificity() {
		return 10;
	}
	
	@Override
	public String toString() {
		return ":" + modifier;
	}
	
	public final static ModifierSelector HOVER = new ModifierSelector("hover");
	
}
