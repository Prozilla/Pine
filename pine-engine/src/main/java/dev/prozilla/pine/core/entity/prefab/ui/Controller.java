package dev.prozilla.pine.core.entity.prefab.ui;

import dev.prozilla.pine.common.util.collection.Pipable;
import dev.prozilla.pine.core.component.ui.Node;

public interface Controller extends Pipable<Controller, ControllerPipeline> {
	
	/**
	 * Loads an instance of a view.
	 * @param view The view instance to load
	 */
	default void load(Node view) {
	
	}
	
	/**
	 * Unloads an instance of a view.
	 * @param view The view instance to unload
	 */
	default void unload(Node view) {
		
	}
	
	@Override
	default ControllerPipeline then(Controller controller) {
		return new ControllerPipeline(this, controller);
	}
	
}
