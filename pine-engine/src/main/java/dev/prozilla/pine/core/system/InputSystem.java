package dev.prozilla.pine.core.system;

import dev.prozilla.pine.core.component.Component;

/**
 * System for handling input for components.
 */
public abstract class InputSystem<T extends Component> extends SystemBase<T> {
	
	public InputSystem(Class<T> componentClass) {
		super(componentClass);
	}
	
	public abstract void input(float deltaTime);
}
