package dev.prozilla.pine.common.property.input;

import dev.prozilla.pine.common.util.ArrayUtils;
import dev.prozilla.pine.core.state.input.gamepad.GamepadAxis;
import dev.prozilla.pine.core.state.input.gamepad.GamepadInput;

import java.util.Arrays;

public class GamepadAxesProperty extends AnalogInputProperty<GamepadAxis[]> implements GamepadInputProperty {
	
	public GamepadAxesProperty() {
		this((GamepadAxis)null);
	}
	
	public GamepadAxesProperty(GamepadAxis... defaultAxes) {
		this(GamepadAxisProperty.DEFAULT_THRESHOLD, defaultAxes);
	}
	
	public GamepadAxesProperty(float threshold, GamepadAxis... defaultAxes) {
		super(defaultAxes, threshold);
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
		for (GamepadAxis axis : getValue()) {
			if (gamepad.getAxis(axis) <= getThreshold()) {
				return false;
			}
		}
		return true;
	}
	
	@Override
	public GamepadAxesProperty toGamepadAxesProperty(GamepadAxis[] value) {
		setValue(value);
		return this;
	}
	
	@Override
	public boolean equals(Object object) {
		return object == this || (object instanceof GamepadAxesProperty gamepadAxesProperty && equals(gamepadAxesProperty));
	}
	
	public boolean equals(GamepadAxesProperty gamepadAxesProperty) {
		return gamepadAxesProperty != null && Arrays.equals(getValue(), gamepadAxesProperty.getValue());
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
