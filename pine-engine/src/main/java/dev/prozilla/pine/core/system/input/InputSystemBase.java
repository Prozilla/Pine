package dev.prozilla.pine.core.system.input;

import dev.prozilla.pine.core.component.Component;
import dev.prozilla.pine.core.state.input.Input;
import dev.prozilla.pine.core.system.SystemBase;

import java.util.Comparator;

/**
 * Base class for systems responsible for updating entity data based on input.
 */
public abstract class InputSystemBase extends SystemBase {
	
	@SafeVarargs
	public InputSystemBase(Class<? extends Component>... componentTypes) {
		super(componentTypes);
	}
	
	/**
	 * Updates each entity's data based on input, each frame.
	 */
	public abstract void input(float deltaTime);
	
	public Input getInput() {
		return application.getInput();
	}
	
	/**
	 * Sorts the entity chunks in this input system based on their depth index.
	 */
	public void sort() {
		sort(Comparator.comparingInt(a -> -a.getTransform().getDepthIndex()));
	}
}
