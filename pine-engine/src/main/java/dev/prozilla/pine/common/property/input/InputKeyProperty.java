package dev.prozilla.pine.common.property.input;

import dev.prozilla.pine.core.state.input.Input;
import dev.prozilla.pine.core.state.input.Key;
import dev.prozilla.pine.core.state.input.MouseButton;
import dev.prozilla.pine.core.state.input.gamepad.GamepadAxis;
import dev.prozilla.pine.core.state.input.gamepad.GamepadButton;

public interface InputKeyProperty extends InputProperty {
	
	InputKeyProperty FALLBACK = new InputKeyProperty() {
		@Override
		public boolean isPressed(Input input) {
			return false;
		}
		
		@Override
		public boolean isDown(Input input) {
			return false;
		}
		
		@Override
		public String toString() {
			return "null";
		}
	};
	
	String INPUT_ELEMENT_SEPARATOR = "+";
	String INPUT_BINDING_SEPARATOR = " | ";
	
	/**
	 * Converts this input property to a keyboard key property.
	 * @param value The keyboard key
	 * @return A keyboard key property.
	 */
	default KeyboardKeyProperty toKeyboardKeyProperty(Key value) {
		return new KeyboardKeyProperty(value);
	}
	
	/**
	 * Converts this input property to a keyboard keys property.
	 * @param value The keyboard keys
	 * @return A keyboard keys property.
	 */
	default KeyboardKeysProperty toKeyboardKeysProperty(Key... value) {
		return new KeyboardKeysProperty(value);
	}
	
	/**
	 * Converts this input property to a mouse button property.
	 * @param value The mouse button
	 * @return A mouse button property.
	 */
	default MouseButtonProperty toMouseButtonProperty(MouseButton value) {
		return new MouseButtonProperty(value);
	}
	
	/**
	 * Converts this input property to a mouse buttons property.
	 * @param value The mouse buttons
	 * @return A mouse buttons property.
	 */
	default MouseButtonsProperty toMouseButtonsProperty(MouseButton... value) {
		return new MouseButtonsProperty(value);
	}
	
	/**
	 * Converts this input property to a gamepad button property.
	 * @param value The gamepad button
	 * @return A gamepad button property.
	 */
	default GamepadButtonProperty toGamepadButtonProperty(GamepadButton value) {
		return new GamepadButtonProperty(value);
	}
	
	/**
	 * Converts this input property to a gamepad buttons property.
	 * @param value The gamepad buttons
	 * @return A gamepad buttons property.
	 */
	default GamepadButtonsProperty toGamepadButtonsProperty(GamepadButton... value) {
		return new GamepadButtonsProperty(value);
	}
	
	/**
	 * Converts this input property to a gamepad axis property.
	 * @param value The gamepad axis
	 * @return A gamepad axis property.
	 */
	default GamepadAxisProperty toGamepadAxisProperty(GamepadAxis value) {
		return new GamepadAxisProperty(value);
	}
	
	/**
	 * Converts this input property to a gamepad axes property.
	 * @param value The gamepad axes
	 * @return A gamepad axes property.
	 */
	default GamepadAxesProperty toGamepadAxesProperty(GamepadAxis... value) {
		return new GamepadAxesProperty(value);
	}
	
}
