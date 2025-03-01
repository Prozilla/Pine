package dev.prozilla.pine.core.system.render;

import dev.prozilla.pine.core.component.Component;
import dev.prozilla.pine.core.entity.EntityChunk;
import dev.prozilla.pine.core.rendering.Renderer;

/**
 * System for rendering entities to the screen.
 */
public abstract class RenderSystem extends RenderSystemBase {
	
	@SafeVarargs
	public RenderSystem(Class<? extends Component>... componentTypes) {
		super(componentTypes);
	}
	
	@Override
	public final void render(Renderer renderer) {
		forEach(chunk -> {
			process(chunk, renderer);
		});
	}
	
	/**
	 * Renders a single entity, each frame.
	 * @param chunk Entity that matches this system's query
	 */
	protected abstract void process(EntityChunk chunk, Renderer renderer);
}
