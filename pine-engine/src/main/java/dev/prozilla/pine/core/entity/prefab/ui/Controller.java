package dev.prozilla.pine.core.entity.prefab.ui;

import dev.prozilla.pine.core.component.ui.Node;

public interface Controller {
	
	/**
	 * Initializes an instance of a view.
	 * @param view The view instance to initialize
	 */
	default void initialize(Node view) {
	
	}
	
}
