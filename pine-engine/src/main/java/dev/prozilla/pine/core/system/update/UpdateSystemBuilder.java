package dev.prozilla.pine.core.system.update;

import dev.prozilla.pine.core.component.Component;
import dev.prozilla.pine.core.entity.EntityChunk;
import dev.prozilla.pine.core.system.SystemBuilder;

import java.util.Objects;

/**
 * Utility class for building update systems.
 * @see UpdateSystem
 */
public class UpdateSystemBuilder extends SystemBuilder<UpdateSystem, UpdateSystemBuilder> {
	
	private Callback callback;
	
	@SafeVarargs
	public UpdateSystemBuilder(Class<? extends Component>... componentTypes) {
		super(componentTypes);
	}
	
	@Override
	protected UpdateSystemBuilder self() {
		return this;
	}
	
	public UpdateSystemBuilder setCallback(Callback callback) {
		this.callback = callback;
		return this;
	}
	
	@Override
	public UpdateSystem build() {
		Objects.requireNonNull(callback, "Callback must not be null.");
		
		UpdateSystem updateSystem = new UpdateSystem(componentTypes) {
			@Override
			protected void process(EntityChunk chunk, float deltaTime) {
				callback.run(chunk, deltaTime);
			}
		};
		
		return super.finishBuild(updateSystem);
	}
	
	@FunctionalInterface
	public interface Callback {
		
		/**
		 * Updates a single entity's data, each frame.
		 * @param chunk Entity that matches this system's query
		 * @param deltaTime Delta time in seconds
		 */
		void run(EntityChunk chunk, float deltaTime);
		
	}
}
