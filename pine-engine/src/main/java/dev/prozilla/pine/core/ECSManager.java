package dev.prozilla.pine.core;

import dev.prozilla.pine.common.Lifecycle;

/**
 * Base class for entity, component and system managers.
 */
public abstract class ECSManager implements Lifecycle {
	
	protected final World world;
	
	public ECSManager(World world) {
		this.world = world;
	}
}
