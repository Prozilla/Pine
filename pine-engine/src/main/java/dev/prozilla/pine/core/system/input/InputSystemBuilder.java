package dev.prozilla.pine.core.system.input;

import dev.prozilla.pine.core.component.Component;
import dev.prozilla.pine.core.entity.EntityMatch;
import dev.prozilla.pine.core.state.input.Input;
import dev.prozilla.pine.core.system.SystemBuilder;
import dev.prozilla.pine.core.system.update.UpdateSystem;

import java.util.Objects;

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
		Objects.requireNonNull(callback, "Callback must not be null.");
		
		InputSystem inputSystem = new InputSystem(componentTypes) {
			@Override
			protected void process(EntityMatch match, Input input, float deltaTime) {
				callback.run(match, input, deltaTime);
			}
		};
		
		return super.finishBuild(inputSystem);
	}
	
	@FunctionalInterface
	public interface Callback {
		
		/**
		 * Updates a single entity's data based on input, each frame.
		 * @param match Entity that matches this system's query
		 * @param deltaTime Delta time in seconds
		 */
		void run(EntityMatch match, Input input, float deltaTime);
		
	}
}
