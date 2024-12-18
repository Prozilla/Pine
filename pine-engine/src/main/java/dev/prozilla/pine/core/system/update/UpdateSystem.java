package dev.prozilla.pine.core.system.update;

import dev.prozilla.pine.core.component.Component;
import dev.prozilla.pine.core.entity.EntityChunk;

/**
 * System for updating entity data.
 */
public abstract class UpdateSystem extends UpdateSystemBase {
	
	@SafeVarargs
	public UpdateSystem(Class<? extends Component>... componentTypes) {
		super(componentTypes);
	}
	
	@Override
	public final void update(float deltaTime) {
		forEach(chunk -> {
			process(chunk, deltaTime);
		});
	}
	
	/**
	 * Updates a single entity's data, each frame.
	 * @param chunk Entity that matches this system's query
	 * @param deltaTime Delta time in seconds
	 */
	protected abstract void process(EntityChunk chunk, float deltaTime);
}
