package dev.prozilla.pine.common.property.input;

import dev.prozilla.pine.core.state.input.gamepad.GamepadAxis;
import dev.prozilla.pine.core.state.input.gamepad.GamepadInput;

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
	public GamepadAxesProperty adaptValue(GamepadAxis[] value) {
		setValue(value);
		return this;
	}
	
}
