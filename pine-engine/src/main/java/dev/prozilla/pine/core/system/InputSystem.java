package dev.prozilla.pine.core.system;

import dev.prozilla.pine.core.component.ComponentCollector;

/**
 * System for handling input for components.
 */
public abstract class InputSystem extends SystemBase {
	
	public InputSystem(ComponentCollector collector) {
		super(collector);
	}
	
	public abstract void input(float deltaTime);
}
