package dev.prozilla.pine.common.property.input;

import dev.prozilla.pine.common.property.mutable.SimpleMutableObjectProperty;
import dev.prozilla.pine.core.state.input.gamepad.GamepadButton;
import dev.prozilla.pine.core.state.input.gamepad.GamepadInput;

public class GamepadButtonProperty extends SimpleMutableObjectProperty<GamepadButton> implements GamepadInputProperty {
	
	public GamepadButtonProperty() {
		this(null);
	}
	
	public GamepadButtonProperty(GamepadButton defaultButton) {
		super(defaultButton);
	}
	
	@Override
	public boolean isPressed(GamepadInput gamepad) {
		if (isNull()) {
			return false;
		}
		return gamepad.getButton(getValue());
	}
	
	@Override
	public boolean isDown(GamepadInput gamepad) {
		if (isNull()) {
			return false;
		}
		return gamepad.getButtonDown(getValue());
	}
	
	@Override
	public GamepadButtonProperty adaptValue(GamepadButton value) {
		setValue(value);
		return this;
	}
	
}
