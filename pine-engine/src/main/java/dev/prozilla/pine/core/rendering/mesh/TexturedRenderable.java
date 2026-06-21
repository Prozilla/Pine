package dev.prozilla.pine.core.rendering.mesh;

import dev.prozilla.pine.common.asset.image.TextureAsset;
import dev.prozilla.pine.common.system.Color;
import dev.prozilla.pine.core.rendering.Renderer;

/**
 * An object that can be drawn on the screen with a given texture, color and depth.
 */
@FunctionalInterface
public interface TexturedRenderable extends ColoredRenderable {
	
	@Override
	default void draw(Renderer renderer, Color color) {
		draw(renderer, null, color);
	}
	
	/**
	 * Draws this object with a given texture.
	 * @param renderer The renderer
	 * @param texture The texture to draw with
	 */
	default void draw(Renderer renderer, TextureAsset texture) {
		draw(renderer, texture, renderer.getFallbackColor());
	}
	
	/**
	 * Draws this object with a given texture and color.
	 * @param renderer The renderer
	 * @param texture The texture to draw with
	 * @param color The color to draw with
	 */
	void draw(Renderer renderer, TextureAsset texture, Color color);
	
}
