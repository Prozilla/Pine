package dev.prozilla.pine.core.rendering.shape;

import dev.prozilla.pine.core.rendering.Renderer;

/**
 * An object that can be drawn on the screen.
 */
@FunctionalInterface
public interface Drawable {
	
	/**
	 * Draws this object.
	 * @param renderer The renderer
	 */
	void draw(Renderer renderer);
	
}
