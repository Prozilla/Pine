package dev.prozilla.pine.core.rendering.shape;

import dev.prozilla.pine.common.system.Color;
import dev.prozilla.pine.core.rendering.Renderer;

/**
 * An object that can be drawn on the screen with a given color and depth.
 */
@FunctionalInterface
public interface ColoredDrawable extends Drawable {
	
	/**
	 * Draws this object with the renderer's fallback color.
	 * @param renderer The renderer
	 * @param depth The depth of the entity
	 */
	@Override
	default void draw(Renderer renderer, float depth) {
		draw(renderer, renderer.getFallbackColor(), depth);
	}
	
	/**
	 * Draws this object with a given color.
	 * @param renderer The renderer
	 * @param color The color to draw with
	 * @param depth The depth of the entity
	 */
	void draw(Renderer renderer, Color color, float depth);
	
}
