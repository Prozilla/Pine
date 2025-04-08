package dev.prozilla.pine.common.property.style.selector;

import dev.prozilla.pine.core.component.canvas.RectTransform;

/**
 * A selector that matches elements with a specific class.
 */
public class ClassSelector extends Selector {

	private final String className;
	
	public ClassSelector(String className) {
		this.className = className;
	}
	
	@Override
	public boolean matches(RectTransform context) {
		return context.classes.contains(className);
	}
	
	@Override
	public int getSpecificity() {
		return 10;
	}
	
	@Override
	public String toString() {
		return "." + className;
	}
	
	@Override
	public boolean equals(Selector other) {
		if (!(other instanceof ClassSelector otherClassSelector)) {
			return false;
		}
		
		return className.equals(otherClassSelector.className);
	}
	
}
