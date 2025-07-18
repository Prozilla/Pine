package dev.prozilla.pine.core;

import dev.prozilla.pine.common.lifecycle.Destructible;
import dev.prozilla.pine.core.scene.World;
import dev.prozilla.pine.core.state.Tracker;

/**
 * Base class for entity, component and system managers.
 */
public abstract class ECSManager implements Destructible {
	
	protected final World world;
	
	public ECSManager(World world) {
		this.world = world;
	}
	
	/**
	 * Returns the application's tracker.
	 */
	protected Tracker getTracker() {
		return world.application.getTracker();
	}
	
}
