package dev.prozilla.pine.common.property.input;

import dev.prozilla.pine.common.property.BooleanProperty;
import dev.prozilla.pine.core.state.input.Input;
import dev.prozilla.pine.core.state.input.Key;
import dev.prozilla.pine.core.state.input.MouseButton;
import dev.prozilla.pine.core.state.input.gamepad.GamepadAxis;
import dev.prozilla.pine.core.state.input.gamepad.GamepadButton;

public interface InputProperty {
	
	InputProperty FALLBACK = new InputProperty() {
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
	
	default BooleanProperty isPressedProperty(Input input) {
		return () -> isPressed(input);
	}
	
	boolean isPressed(Input input);
	
	default BooleanProperty isDownProperty(Input input) {
		return () -> isDown(input);
	}
	
	boolean isDown(Input input);
	
	default KeyboardKeyProperty toKeyboardKeyProperty(Key value) {
		return new KeyboardKeyProperty(value);
	}
	
	default KeyboardKeysProperty toKeyboardKeysProperty(Key[] value) {
		return new KeyboardKeysProperty(value);
	}
	
	default MouseButtonProperty toMouseButtonProperty(MouseButton value) {
		return new MouseButtonProperty(value);
	}
	
	default MouseButtonsProperty toMouseButtonsProperty(MouseButton[] value) {
		return new MouseButtonsProperty(value);
	}
	
	default GamepadButtonProperty toGamepadButtonProperty(GamepadButton value) {
		return new GamepadButtonProperty(value);
	}
	
	default GamepadButtonsProperty toGamepadButtonsProperty(GamepadButton[] value) {
		return new GamepadButtonsProperty(value);
	}
	
	default GamepadAxisProperty toGamepadAxisProperty(GamepadAxis value) {
		return new GamepadAxisProperty(value);
	}
	
	default GamepadAxesProperty toGamepadAxesProperty(GamepadAxis[] value) {
		return new GamepadAxesProperty(value);
	}
	
}
