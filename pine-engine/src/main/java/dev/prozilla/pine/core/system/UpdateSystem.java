package dev.prozilla.pine.core.system;

import dev.prozilla.pine.core.component.Component;

/**
 * System responsible for updating component data.
 */
public abstract class UpdateSystem<T extends Component> extends SystemBase<T> {
	
	public UpdateSystem(Class<T> componentClass) {
		super(componentClass);
	}
	
	public abstract void update(float deltaTime);
}
