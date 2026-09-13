package dev.prozilla.pine.core;

import dev.prozilla.pine.common.lifecycle.Destructible;
import dev.prozilla.pine.core.scene.Scene;
import dev.prozilla.pine.core.state.Tracker;

/**
 * Base class for entity, component and system managers.
 */
public abstract class ECSManager implements Destructible {
	
	protected final Scene scene;
	
	public ECSManager(Scene scene) {
		this.scene = scene;
	}
	
	/**
	 * Returns the application's tracker.
	 */
	protected Tracker getTracker() {
		return scene.getApplication().getTracker();
	}
	
}
