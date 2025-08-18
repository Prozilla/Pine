package dev.prozilla.pine.core.system.input;

import dev.prozilla.pine.common.util.checks.Checks;
import dev.prozilla.pine.core.component.Component;
import dev.prozilla.pine.core.entity.EntityChunk;
import dev.prozilla.pine.core.state.input.Input;
import dev.prozilla.pine.core.system.SystemBuilder;
import dev.prozilla.pine.core.system.update.UpdateSystem;

/**
 * Utility class for building input systems.
 * @see UpdateSystem
 */
public class InputSystemBuilder extends SystemBuilder<InputSystem, InputSystemBuilder> {
	
	private Callback callback;
	
	@SafeVarargs
	public InputSystemBuilder(Class<? extends Component>... componentTypes) {
		super(componentTypes);
	}
	
	@Override
	protected InputSystemBuilder self() {
		return this;
	}
	
	public InputSystemBuilder setCallback(Callback callback) {
		this.callback = callback;
		return this;
	}
	
	@Override
	public InputSystem build() {
		Checks.isNotNull(callback, "callback");
		
		InputSystem inputSystem = new InputSystem(componentTypes) {
			@Override
			protected void process(EntityChunk chunk, Input input, float deltaTime) {
				callback.run(chunk, input, deltaTime);
			}
		};
		
		return super.finishBuild(inputSystem);
	}
	
	@FunctionalInterface
	public interface Callback {
		
		/**
		 * Updates a single entity's data based on input, each frame.
		 * @param chunk Entity that matches this system's query
		 * @param deltaTime Delta time in seconds
		 */
		void run(EntityChunk chunk, Input input, float deltaTime);
		
	}
}
