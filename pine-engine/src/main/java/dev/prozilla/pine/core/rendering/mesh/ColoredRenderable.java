package dev.prozilla.pine.core.rendering.mesh;

import dev.prozilla.pine.common.system.Color;
import dev.prozilla.pine.core.rendering.Renderer;

/**
 * An object that can be drawn on the screen with a given color and depth.
 */
@FunctionalInterface
public interface ColoredRenderable extends Renderable {
	
	/**
	 * Draws this object with the renderer's fallback color.
	 * @param renderer The renderer
	 */
	@Override
	default void draw(Renderer renderer) {
		draw(renderer, renderer.getFallbackColor());
	}
	
	/**
	 * Draws this object with a given color.
	 * @param renderer The renderer
	 * @param color The color to draw with
	 */
	void draw(Renderer renderer, Color color);
	
}
