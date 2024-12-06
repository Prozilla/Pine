package dev.prozilla.pine.core.system.render;

import dev.prozilla.pine.core.component.Component;
import dev.prozilla.pine.core.entity.EntityMatch;
import dev.prozilla.pine.core.rendering.Renderer;
import dev.prozilla.pine.core.system.SystemBuilder;

import java.util.Objects;

/**
 * Utility class for building render systems.
 * @see RenderSystem
 */
public class RenderSystemBuilder extends SystemBuilder<RenderSystem, RenderSystemBuilder> {
	
	private Callback callback;
	
	@SafeVarargs
	public RenderSystemBuilder(Class<? extends Component>... componentTypes) {
		super(componentTypes);
	}
	
	@Override
	protected RenderSystemBuilder self() {
		return this;
	}
	
	public RenderSystemBuilder setCallback(Callback callback) {
		this.callback = callback;
		return this;
	}
	
	@Override
	public RenderSystem build() {
		Objects.requireNonNull(callback, "Callback must not be null.");
		
		RenderSystem renderSystem = new RenderSystem(componentTypes) {
			@Override
			protected void process(EntityMatch match, Renderer renderer) {
				callback.run(match, renderer);
			}
		};
		
		return super.finishBuild(renderSystem);
	}
	
	@FunctionalInterface
	public interface Callback {
		
		/**
		 * Renders a single entity, each frame.
		 * @param match Entity that matches this system's query
		 */
		void run(EntityMatch match, Renderer renderer);
		
	}
}
