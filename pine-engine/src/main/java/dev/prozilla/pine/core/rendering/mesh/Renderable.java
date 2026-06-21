package dev.prozilla.pine.core.rendering.mesh;

import dev.prozilla.pine.core.rendering.Renderer;

/**
 * An object that can be drawn on the screen.
 */
@FunctionalInterface
public interface Renderable {
	
	/**
	 * Renders this object.
	 * @param renderer The renderer
	 */
	void draw(Renderer renderer);
	
}
