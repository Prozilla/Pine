package dev.prozilla.pine.core.system;

import dev.prozilla.pine.core.component.Component;
import dev.prozilla.pine.core.entity.EntityQuery;

/**
 * System responsible for updating entity data.
 */
public abstract class UpdateSystem extends SystemBase {
	
	@SafeVarargs
	public UpdateSystem(Class<? extends Component>... componentTypes) {
		super(componentTypes);
	}
	
	/**
	 * Updates every entity, each frame.
	 */
	public abstract void update(float deltaTime);
}
