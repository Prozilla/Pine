package dev.prozilla.pine.core.system;

import dev.prozilla.pine.core.component.ComponentCollector;

/**
 * System for handling initialization.
 */
public abstract class InitSystem extends SystemBase {
	
	public InitSystem(ComponentCollector collector) {
		super(collector);
	}
	
	public abstract void init(long window);
}
