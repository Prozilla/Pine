package dev.prozilla.pine.core.system;

import dev.prozilla.pine.core.component.Component;
import dev.prozilla.pine.core.rendering.Renderer;

/**
 * System responsible for rendering components to the screen.
 */
public abstract class RenderSystem<T extends Component> extends SystemBase<T> {

	public RenderSystem(Class<T> componentClass) {
		super(componentClass);
	}
	
	/** Renders every component to the screen, each frame. */
	public abstract void render(Renderer renderer);
}
