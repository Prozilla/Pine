package dev.prozilla.pine.core.rendering.mesh;

import dev.prozilla.pine.core.rendering.Renderer;
import dev.prozilla.pine.core.rendering.material.Material;

@FunctionalInterface
public interface MaterialRenderable extends TexturedRenderable {
	
	/**
	 * Draws this object with a given material.
	 * @param renderer The renderer
	 * @param material The material to draw with
	 */
	default void draw(Renderer renderer, Material<?> material) {
		if (material == null) {
			renderer.resetMaterial();
			draw(renderer);
		} else {
			renderer.setMaterial(material);
			if (material.color != null && material.getTexture() != null) {
				draw(renderer, material.getTexture(), material.color);
			} else if (material.color == null) {
				draw(renderer, material.getTexture());
			} else {
				draw(renderer, material.color);
			}
		}
	}
	
}
