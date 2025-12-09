package dev.prozilla.pine.common.property.input;

import dev.prozilla.pine.common.property.mutable.SimpleMutableObjectProperty;
import dev.prozilla.pine.common.util.StringUtils;
import dev.prozilla.pine.core.state.input.gamepad.GamepadButton;
import dev.prozilla.pine.core.state.input.gamepad.GamepadInput;

import java.util.Objects;

public class GamepadButtonProperty extends SimpleMutableObjectProperty<GamepadButton> implements GamepadInputProperty {
	
	public GamepadButtonProperty() {
		this(null);
	}
	
	public GamepadButtonProperty(GamepadButton defaultButton) {
		super(defaultButton);
	}
	
	@Override
	public boolean isPressed(GamepadInput gamepad) {
		return gamepad.getButton(getValue());
	}
	
	@Override
	public boolean isDown(GamepadInput gamepad) {
		return gamepad.getButtonDown(getValue());
	}
	
	@Override
	public GamepadButtonProperty toGamepadButtonProperty(GamepadButton value) {
		setValue(value);
		return this;
	}
	
	@Override
	public boolean equals(Object object) {
		return object == this || (object instanceof GamepadButtonProperty gamepadButtonProperty && equals(gamepadButtonProperty));
	}
	
	public boolean equals(GamepadButtonProperty gamepadButtonProperty) {
		return gamepadButtonProperty != null && Objects.equals(getValue(), gamepadButtonProperty.getValue());
	}
	
	@Override
	public String toString() {
		return StringUtils.toString(getValue());
	}
	
}
