package dev.prozilla.pine.core.system.update;

import dev.prozilla.pine.core.component.Component;
import dev.prozilla.pine.core.system.SystemBase;

/**
 * Base class for systems responsible for updating entity data.
 */
public abstract class UpdateSystemBase extends SystemBase {
	
	@SafeVarargs
	public UpdateSystemBase(Class<? extends Component>... componentTypes) {
		super(componentTypes);
	}
	
	/**
	 * Updates each entity's data, each frame.
	 */
	public abstract void update(float deltaTime);
}
