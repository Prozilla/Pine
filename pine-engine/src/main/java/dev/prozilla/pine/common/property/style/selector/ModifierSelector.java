package dev.prozilla.pine.common.property.style.selector;

import dev.prozilla.pine.core.component.canvas.RectTransform;

public class ModifierSelector extends Selector {

	private final String modifier;
	
	public final static ModifierSelector HOVER = new ModifierSelector("hover");
	
	public ModifierSelector(String modifier) {
		this.modifier = modifier;
	}
	
	@Override
	public boolean matches(RectTransform context) {
		return context.modifiers.contains(modifier);
	}
	
	@Override
	public int getSpecificity() {
		return 0;
	}
	
	@Override
	public String toString() {
		return ":" + modifier;
	}
	
}
