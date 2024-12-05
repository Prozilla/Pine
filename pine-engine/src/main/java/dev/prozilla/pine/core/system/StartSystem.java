package dev.prozilla.pine.core.system;

import dev.prozilla.pine.core.entity.EntityQuery;

/**
 * System for handling start.
 * @deprecated Start systems are unused and planned to be deleted.
 */
@Deprecated
public abstract class StartSystem extends SystemBase {
	
	@Deprecated
	public StartSystem(EntityQuery collector) {
		super(collector, true);
	}
	
	public abstract void start();
}
