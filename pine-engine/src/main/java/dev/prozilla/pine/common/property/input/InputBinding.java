package dev.prozilla.pine.common.property.input;

import dev.prozilla.pine.common.util.checks.Checks;
import dev.prozilla.pine.core.state.input.Input;
import dev.prozilla.pine.core.state.input.Key;
import dev.prozilla.pine.core.state.input.MouseButton;
import dev.prozilla.pine.core.state.input.gamepad.GamepadAxis;
import dev.prozilla.pine.core.state.input.gamepad.GamepadButton;

/**
 * An input binding is the connection between an input key (mouse buttons, keyboard keys, ...) and a corresponding action.
 */
public class InputBinding implements InputProperty {

	private InputProperty inputProperty;
	
	public InputBinding(InputProperty inputProperty) {
		this.inputProperty = Checks.isNotNull(inputProperty, "inputProperty");
	}
	
	@Override
	public boolean isPressed(Input input) {
		return inputProperty.isPressed(input);
	}
	
	@Override
	public boolean isDown(Input input) {
		return inputProperty.isDown(input);
	}
	
	public void setBinding(Key... value) {
		inputProperty = inputProperty.adaptValue(value);
	}
	
	public void setBinding(Key value) {
		inputProperty = inputProperty.adaptValue(value);
	}
	
	public void setBinding(MouseButton... value) {
		inputProperty = inputProperty.adaptValue(value);
	}
	
	public void setBinding(MouseButton value) {
		inputProperty = inputProperty.adaptValue(value);
	}
	
	public void setBinding(GamepadButton... value) {
		inputProperty = inputProperty.adaptValue(value);
	}
	
	public void setBinding(GamepadButton value) {
		inputProperty = inputProperty.adaptValue(value);
	}
	
	public void setBinding(GamepadAxis... value) {
		inputProperty = inputProperty.adaptValue(value);
	}
	
	public void setBinding(GamepadAxis value) {
		inputProperty = inputProperty.adaptValue(value);
	}
	
}
