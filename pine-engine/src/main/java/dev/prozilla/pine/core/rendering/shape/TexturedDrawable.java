package dev.prozilla.pine.core.rendering.shape;

import dev.prozilla.pine.common.asset.image.TextureAsset;
import dev.prozilla.pine.common.system.Color;
import dev.prozilla.pine.core.rendering.Renderer;

/**
 * An object that can be drawn on the screen with a given texture, color and depth.
 */
@FunctionalInterface
public interface TexturedDrawable extends ColoredDrawable {
	
	@Override
	default void draw(Renderer renderer, Color color, float depth) {
		draw(renderer, null, color, depth);
	}
	
	/**
	 * Draws this object with a given texture.
	 * @param renderer The renderer
	 * @param texture The texture to draw with
	 * @param depth The depth of the entity
	 */
	default void draw(Renderer renderer, TextureAsset texture, float depth) {
		draw(renderer, texture, renderer.getFallbackColor(), depth);
	}
	
	/**
	 * Draws this object with a given texture and color.
	 * @param renderer The renderer
	 * @param texture The texture to draw with
	 * @param color The color to draw with
	 * @param depth The depth of the entity
	 */
	void draw(Renderer renderer, TextureAsset texture, Color color, float depth);
	
}
