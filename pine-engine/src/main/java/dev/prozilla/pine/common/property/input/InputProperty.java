package dev.prozilla.pine.common.property.input;

import dev.prozilla.pine.common.property.BooleanProperty;
import dev.prozilla.pine.core.state.input.Input;
import dev.prozilla.pine.core.state.input.Key;
import dev.prozilla.pine.core.state.input.MouseButton;
import dev.prozilla.pine.core.state.input.gamepad.GamepadAxis;
import dev.prozilla.pine.core.state.input.gamepad.GamepadButton;

public interface InputProperty {
	
	default BooleanProperty isPressedProperty(Input input) {
		return () -> isPressed(input);
	}
	
	boolean isPressed(Input input);
	
	default BooleanProperty isDownProperty(Input input) {
		return () -> isDown(input);
	}
	
	boolean isDown(Input input);
	
	default KeyboardKeyProperty adaptValue(Key value) {
		return new KeyboardKeyProperty(value);
	}
	
	default KeyboardKeysProperty adaptValue(Key[] value) {
		return new KeyboardKeysProperty(value);
	}
	
	default MouseButtonProperty adaptValue(MouseButton value) {
		return new MouseButtonProperty(value);
	}
	
	default MouseButtonsProperty adaptValue(MouseButton[] value) {
		return new MouseButtonsProperty(value);
	}
	
	default GamepadButtonProperty adaptValue(GamepadButton value) {
		return new GamepadButtonProperty(value);
	}
	
	default GamepadButtonsProperty adaptValue(GamepadButton[] value) {
		return new GamepadButtonsProperty(value);
	}
	
	default GamepadAxisProperty adaptValue(GamepadAxis value) {
		return new GamepadAxisProperty(value);
	}
	
	default GamepadAxesProperty adaptValue(GamepadAxis[] value) {
		return new GamepadAxesProperty(value);
	}
	
}
