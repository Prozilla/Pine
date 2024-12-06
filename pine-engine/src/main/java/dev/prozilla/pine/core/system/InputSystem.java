package dev.prozilla.pine.core.system;

import dev.prozilla.pine.core.component.Component;
import dev.prozilla.pine.core.entity.EntityQuery;
import dev.prozilla.pine.core.state.input.Input;

/**
 * System for handling input for entities.
 */
public abstract class InputSystem extends SystemBase {
	
	@SafeVarargs
	public InputSystem(Class<? extends Component>... componentTypes) {
		super(componentTypes);
	}
	
	/**
	 * Handles input for each entity, each frame.
	 */
	public abstract void input(float deltaTime);
	
	public Input getInput() {
		return application.getInput();
	}
}
