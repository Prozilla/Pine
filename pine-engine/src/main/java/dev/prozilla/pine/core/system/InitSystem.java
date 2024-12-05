package dev.prozilla.pine.core.system;

import dev.prozilla.pine.core.entity.EntityQuery;

/**
 * System for handling entity initialization.
 */
public abstract class InitSystem extends SystemBase {
	
	public InitSystem(EntityQuery collector) {
		super(collector, true);
	}
	
	/**
	 * Initializes each entity before it enters the world.
	 * @param window Window ID
	 */
	public abstract void init(long window);
}
