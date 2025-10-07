package dev.prozilla.pine.common.property.input;

import dev.prozilla.pine.common.util.checks.Checks;
import dev.prozilla.pine.core.state.input.Input;
import dev.prozilla.pine.core.state.input.Key;
import dev.prozilla.pine.core.state.input.MouseButton;
import dev.prozilla.pine.core.state.input.gamepad.GamepadAxis;
import dev.prozilla.pine.core.state.input.gamepad.GamepadButton;

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
	
	public void setBinding(int index, Key... value) {
		inputProperties[index] = inputProperties[index].adaptValue(value);
	}
	
	public void setBinding(int index, Key value) {
		inputProperties[index] = inputProperties[index].adaptValue(value);
	}
	
	public void setBinding(int index, MouseButton... value) {
		inputProperties[index] = inputProperties[index].adaptValue(value);
	}
	
	public void setBinding(int index, MouseButton value) {
		inputProperties[index] = inputProperties[index].adaptValue(value);
	}
	
	public void setBinding(int index, GamepadButton... value) {
		inputProperties[index] = inputProperties[index].adaptValue(value);
	}
	
	public void setBinding(int index, GamepadButton value) {
		inputProperties[index] = inputProperties[index].adaptValue(value);
	}
	
	public void setBinding(int index, GamepadAxis... value) {
		inputProperties[index] = inputProperties[index].adaptValue(value);
	}
	
	public void setBinding(int index, GamepadAxis value) {
		inputProperties[index] = inputProperties[index].adaptValue(value);
	}
	
}
