package dev.prozilla.pine.common.property.input;

import dev.prozilla.pine.common.util.ArrayUtils;
import dev.prozilla.pine.common.util.checks.Checks;
import dev.prozilla.pine.core.state.input.Input;
import dev.prozilla.pine.core.state.input.Key;
import dev.prozilla.pine.core.state.input.MouseButton;
import dev.prozilla.pine.core.state.input.gamepad.GamepadAxis;
import dev.prozilla.pine.core.state.input.gamepad.GamepadButton;

import java.util.Arrays;

/**
 * Input bindings are connections between input keys (mouse buttons, keyboard keys, ...) and a corresponding action.
 */
public class InputBindings implements InputProperty {

	private final InputProperty[] inputProperties;
	
	public InputBindings(InputProperty... inputProperties) {
		this.inputProperties = Checks.isNotNull(inputProperties, "inputProperties");
	}
	
	@Override
	public boolean isPressed(Input input) {
		for (InputProperty inputProperty : inputProperties) {
			if (inputProperty.isPressed(input)) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean isDown(Input input) {
		for (InputProperty inputProperty : inputProperties) {
			if (inputProperty.isDown(input)) {
				return true;
			}
		}
		return false;
	}
	
	public InputProperty getBinding(int index) {
		return inputProperties[index];
	}
	
	public void setBinding(int index, Key value) {
		inputProperties[index] = inputProperties[index].toKeyboardKeyProperty(value);
	}
	
	public void setBinding(int index, Key... value) {
		inputProperties[index] = inputProperties[index].toKeyboardKeysProperty(value);
	}
	
	public void setBinding(int index, MouseButton value) {
		inputProperties[index] = inputProperties[index].toMouseButtonProperty(value);
	}
	
	public void setBinding(int index, MouseButton... value) {
		inputProperties[index] = inputProperties[index].toMouseButtonsProperty(value);
	}
	
	public void setBinding(int index, GamepadButton value) {
		inputProperties[index] = inputProperties[index].toGamepadButtonProperty(value);
	}
	
	public void setBinding(int index, GamepadButton... value) {
		inputProperties[index] = inputProperties[index].toGamepadButtonsProperty(value);
	}
	
	public void setBinding(int index, GamepadAxis value) {
		inputProperties[index] = inputProperties[index].toGamepadAxisProperty(value);
	}
	
	public void setBinding(int index, GamepadAxis... value) {
		inputProperties[index] = inputProperties[index].toGamepadAxesProperty(value);
	}
	
	public void unbind(int index) {
		inputProperties[index] = InputProperty.FALLBACK;
	}
	
	@Override
	public boolean equals(Object object) {
		return object == this || (object instanceof InputBindings inputBindings && equals(inputBindings));
	}
	
	public boolean equals(InputBindings inputBindings) {
		return inputBindings != null && Arrays.equals(inputProperties, inputBindings.inputProperties);
	}
	
	@Override
	public String toString() {
		return ArrayUtils.toString(inputProperties, INPUT_BINDING_SEPARATOR);
	}
	
}
