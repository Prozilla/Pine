package dev.prozilla.pine.common.property.input;

import dev.prozilla.pine.common.util.StringUtils;
import dev.prozilla.pine.core.state.input.gamepad.GamepadAxis;
import dev.prozilla.pine.core.state.input.gamepad.GamepadInput;

import java.util.Objects;

public class GamepadAxisProperty extends AnalogInputProperty<GamepadAxis> implements GamepadInputProperty {
	
	public static final float DEFAULT_THRESHOLD = 0.5f;
	
	public GamepadAxisProperty() {
		this(null);
	}
	
	public GamepadAxisProperty(GamepadAxis defaultAxis) {
		this(defaultAxis, DEFAULT_THRESHOLD);
	}
	
	public GamepadAxisProperty(GamepadAxis defaultAxis, float threshold) {
		super(defaultAxis, threshold);
	}
	
	@Override
	public boolean isDown(GamepadInput gamepad) {
		return isPressed(gamepad);
	}
	
	@Override
	public boolean isPressed(GamepadInput gamepad) {
		if (isNull()) {
			return false;
		}
		return gamepad.getAxis(getValue()) > getThreshold();
	}
	
	@Override
	public GamepadAxisProperty toGamepadAxisProperty(GamepadAxis value) {
		setValue(value);
		return this;
	}
	
	@Override
	public boolean equals(Object object) {
		return object == this || (object instanceof GamepadAxisProperty gamepadAxisProperty && equals(gamepadAxisProperty));
	}
	
	public boolean equals(GamepadAxisProperty gamepadAxisProperty) {
		return gamepadAxisProperty != null && Objects.equals(getValue(), gamepadAxisProperty.getValue());
	}
	
	@Override
	public String toString() {
		return StringUtils.toString(getValue());
	}
	
}
