package dev.prozilla.pine.core.system;

import dev.prozilla.pine.core.component.Component;

/**
 * System for handling entity initialization.
 */
public abstract class InitSystem extends SystemBase {
	
	@SafeVarargs
	public InitSystem(Class<? extends Component>... componentTypes) {
		super(componentTypes, true);
	}
	
	/**
	 * Initializes each entity before it enters the world.
	 * @param window Window ID
	 */
	public abstract void init(long window);
}
