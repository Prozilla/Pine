package dev.prozilla.pine.core.system;

import dev.prozilla.pine.core.component.ComponentCollector;
import dev.prozilla.pine.core.rendering.Renderer;

/**
 * System responsible for rendering components to the screen.
 */
public abstract class RenderSystem extends SystemBase {
	
	public RenderSystem(ComponentCollector collector) {
		super(collector);
	}
	
	/** Renders every component to the screen, each frame. */
	public abstract void render(Renderer renderer);
}
