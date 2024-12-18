package dev.prozilla.pine.core.system.input;

import dev.prozilla.pine.core.component.Component;
import dev.prozilla.pine.core.entity.EntityChunk;
import dev.prozilla.pine.core.state.input.Input;

/**
 * System for updating entity data based on input.
 */
public abstract class InputSystem extends InputSystemBase {
	
	@SafeVarargs
	public InputSystem(Class<? extends Component>... componentTypes) {
		super(componentTypes);
	}
	
	@Override
	public final void input(float deltaTime) {
		Input input = getInput();
		
		forEach(chunk -> {
			process(chunk, input, deltaTime);
		});
	}
	
	/**
	 * Updates a single entity's data based on input, each frame.
	 * @param chunk Entity that matches this system's query
	 * @param deltaTime Delta time in seconds
	 */
	protected abstract void process(EntityChunk chunk, Input input, float deltaTime);
}
