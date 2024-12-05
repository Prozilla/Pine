package dev.prozilla.pine.core.system;

import dev.prozilla.pine.core.entity.EntityQuery;

/**
 * System responsible for updating entity data.
 */
public abstract class UpdateSystem extends SystemBase {
	
	public UpdateSystem(EntityQuery query) {
		super(query);
	}
	
	/**
	 * Updates every entity, each frame.
	 */
	public abstract void update(float deltaTime);
}
