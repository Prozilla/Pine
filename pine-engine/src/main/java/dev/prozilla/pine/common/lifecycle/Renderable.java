package dev.prozilla.pine.common.lifecycle;

import dev.prozilla.pine.core.rendering.Renderer;

public interface Renderable {
	
	/**
	 * Renders this object every frame.
	 */
	void render(Renderer renderer);
	
}
