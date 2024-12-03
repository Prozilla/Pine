package dev.prozilla.pine.core.system;

import dev.prozilla.pine.core.component.ComponentCollector;

/**
 * System responsible for updating component data.
 */
public abstract class UpdateSystem extends SystemBase {
	
	public UpdateSystem(ComponentCollector collector) {
		super(collector);
	}
	
	public abstract void update(float deltaTime);
}
