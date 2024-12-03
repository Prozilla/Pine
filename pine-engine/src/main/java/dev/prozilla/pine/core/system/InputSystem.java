package dev.prozilla.pine.core.system;

import dev.prozilla.pine.core.component.ComponentCollector;
import dev.prozilla.pine.core.state.input.Input;

/**
 * System for handling input for components.
 */
public abstract class InputSystem extends SystemBase {
	
	public InputSystem(ComponentCollector collector) {
		super(collector);
	}
	
	public abstract void input(float deltaTime);
	
	public Input getInput() {
		return world.application.getInput();
	}
}
