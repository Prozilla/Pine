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
	
	public InputBinding(Key defaultKey) {
		this(new KeyboardKeyProperty(defaultKey));
	}
	
	public InputBinding(Key... defaultKeys) {
		this(new KeyboardKeysProperty(defaultKeys));
	}
	
	public InputBinding(MouseButton defaultButton) {
		this(new MouseButtonProperty(defaultButton));
	}
	
	public InputBinding(MouseButton... defaultButtons) {
		this(new MouseButtonsProperty(defaultButtons));
	}
	
	public InputBinding(GamepadButton defaultButton) {
		this(new GamepadButtonProperty(defaultButton));
	}
	
	public InputBinding(GamepadButton... defaultButtons) {
		this(new GamepadButtonsProperty(defaultButtons));
	}
	
	public InputBinding(GamepadAxis defaultAxis) {
		this(new GamepadAxisProperty(defaultAxis));
	}
	
	public InputBinding(GamepadAxis... defaultAxes) {
		this(new GamepadAxesProperty(defaultAxes));
	}
	
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
	
	public InputProperty getBinding() {
		return inputProperty;
	}
	
	public void setBinding(Key value) {
		inputProperty = inputProperty.toKeyboardKeyProperty(value);
	}
	
	public void setBinding(Key... value) {
		inputProperty = inputProperty.toKeyboardKeysProperty(value);
	}
	
	public void setBinding(MouseButton value) {
		inputProperty = inputProperty.toMouseButtonProperty(value);
	}
	
	public void setBinding(MouseButton... value) {
		inputProperty = inputProperty.toMouseButtonsProperty(value);
	}
	
	public void setBinding(GamepadButton value) {
		inputProperty = inputProperty.toGamepadButtonProperty(value);
	}
	
	public void setBinding(GamepadButton... value) {
		inputProperty = inputProperty.toGamepadButtonsProperty(value);
	}
	
	public void setBinding(GamepadAxis value) {
		inputProperty = inputProperty.toGamepadAxisProperty(value);
	}
	
	public void setBinding(GamepadAxis... value) {
		inputProperty = inputProperty.toGamepadAxesProperty(value);
	}
	
	public void unbind() {
		inputProperty = InputProperty.FALLBACK;
	}
	
	@Override
	public boolean equals(Object object) {
		return object == this || (object instanceof InputBinding inputBinding && equals(inputBinding));
	}
	
	public boolean equals(InputBinding inputBinding) {
		return inputBinding != null && inputProperty.equals(inputBinding.inputProperty);
	}
	
	@Override
	public String toString() {
		return inputProperty.toString();
	}
	
}
