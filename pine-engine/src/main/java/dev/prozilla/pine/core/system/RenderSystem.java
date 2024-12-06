package dev.prozilla.pine.core.system;

import dev.prozilla.pine.core.component.Component;
import dev.prozilla.pine.core.rendering.Renderer;

/**
 * System responsible for rendering entities to the screen.
 */
public abstract class RenderSystem extends SystemBase {
	
	@SafeVarargs
	public RenderSystem(Class<? extends Component>... componentTypes) {
		super(componentTypes);
	}
	
	/**
	 * Renders every entity to the screen, each frame.
	 */
	public abstract void render(Renderer renderer);
}
