package dev.prozilla.pine.common.property.input;

import dev.prozilla.pine.core.state.input.gamepad.GamepadAxis;
import dev.prozilla.pine.core.state.input.gamepad.GamepadInput;

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
	public GamepadAxisProperty adaptValue(GamepadAxis value) {
		setValue(value);
		return this;
	}
	
}
