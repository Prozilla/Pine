package dev.prozilla.pine.common.property.style.selector;

import dev.prozilla.pine.core.component.canvas.RectTransform;

/**
 * A selector that matches elements with a specific name.
 */
public class TypeSelector extends Selector {

	private final String type;
	
	public TypeSelector(String type) {
		this.type = type.toLowerCase();
	}
	
	@Override
	public boolean matches(RectTransform context) {
		return context.getEntity().getName().toLowerCase().equals(type);
	}
	
	@Override
	public int getSpecificity() {
		return 1;
	}
	
	@Override
	public String toString() {
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
