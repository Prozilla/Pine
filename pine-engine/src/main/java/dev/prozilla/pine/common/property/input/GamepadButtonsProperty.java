package dev.prozilla.pine.common.property.input;

import dev.prozilla.pine.common.property.mutable.SimpleMutableObjectProperty;
import dev.prozilla.pine.core.state.input.gamepad.GamepadButton;
import dev.prozilla.pine.core.state.input.gamepad.GamepadInput;

public class GamepadButtonsProperty extends SimpleMutableObjectProperty<GamepadButton[]> implements GamepadInputProperty {
	
	public GamepadButtonsProperty() {
		this((GamepadButton)null);
	}
	
	public GamepadButtonsProperty(GamepadButton... defaultButtons) {
		super(defaultButtons);
	}
	
	@Override
	public boolean isPressed(GamepadInput gamepad) {
		if (isNull()) {
			return false;
		}
		for (GamepadButton button : getValue()) {
			if (!gamepad.getButton(button)) {
				return false;
			}
		}
		return true;
	}
	
	@Override
	public boolean isDown(GamepadInput gamepad) {
		if (isNull()) {
			return false;
		}
		for (GamepadButton button : getValue()) {
			if (!gamepad.getButtonDown(button)) {
				return false;
			}
		}
		return true;
	}
	
	@Override
	public GamepadButtonsProperty adaptValue(GamepadButton[] value) {
		setValue(value);
		return this;
	}
	
}
