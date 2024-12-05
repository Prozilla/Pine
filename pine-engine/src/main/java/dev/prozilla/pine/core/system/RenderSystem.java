package dev.prozilla.pine.core.system;

import dev.prozilla.pine.core.entity.EntityQuery;
import dev.prozilla.pine.core.rendering.Renderer;

/**
 * System responsible for rendering entities to the screen.
 */
public abstract class RenderSystem extends SystemBase {
	
	public RenderSystem(EntityQuery collector) {
		super(collector);
	}
	
	/**
	 * Renders every entity to the screen, each frame.
	 */
	public abstract void render(Renderer renderer);
}
