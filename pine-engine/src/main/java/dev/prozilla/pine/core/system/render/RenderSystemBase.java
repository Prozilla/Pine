package dev.prozilla.pine.core.system.render;

import dev.prozilla.pine.core.component.Component;
import dev.prozilla.pine.core.rendering.Renderer;
import dev.prozilla.pine.core.system.SystemBase;

/**
 * Base class for systems responsible for rendering entities to the screen.
 */
public abstract class RenderSystemBase extends SystemBase {
	
	@SafeVarargs
	public RenderSystemBase(Class<? extends Component>... componentTypes) {
		super(componentTypes);
	}
	
	/**
	 * Renders every entity to the screen, each frame.
	 */
	public abstract void render(Renderer renderer);
}
