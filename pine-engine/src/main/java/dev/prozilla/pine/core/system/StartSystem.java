package dev.prozilla.pine.core.system;

import dev.prozilla.pine.core.component.ComponentCollector;

/**
 * System for handling start.
 */
public abstract class StartSystem extends SystemBase {
	
	public StartSystem(ComponentCollector collector) {
		super(collector, true);
	}
	
	public abstract void start();
}
