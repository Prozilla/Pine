package dev.prozilla.pine.common.property.input;

import dev.prozilla.pine.common.property.mutable.SimpleMutableObjectProperty;
import dev.prozilla.pine.common.util.ArrayUtils;
import dev.prozilla.pine.core.state.input.gamepad.GamepadButton;
import dev.prozilla.pine.core.state.input.gamepad.GamepadInput;

import java.util.Arrays;

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
	public GamepadButtonsProperty toGamepadButtonsProperty(GamepadButton[] value) {
		setValue(value);
		return this;
	}
	
	@Override
	public boolean equals(Object object) {
		return object == this || (object instanceof GamepadButtonsProperty gamepadButtonsProperty && equals(gamepadButtonsProperty));
	}
	
	public boolean equals(GamepadButtonsProperty gamepadButtonsProperty) {
		return gamepadButtonsProperty != null && Arrays.equals(getValue(), gamepadButtonsProperty.getValue());
	}
	
	@Override
	public String toString() {
		if (isNull()) {
			return "null";
		} else {
			return ArrayUtils.toString(getValue(), INPUT_ELEMENT_SEPARATOR);
		}
	}
	
}
