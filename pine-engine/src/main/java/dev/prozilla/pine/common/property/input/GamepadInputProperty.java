package dev.prozilla.pine.common.property.input;

import dev.prozilla.pine.core.state.input.Input;
import dev.prozilla.pine.core.state.input.gamepad.GamepadInput;

public interface GamepadInputProperty extends InputProperty {
	
	@Override
	default boolean isPressed(Input input) {
		return isPressed(input.getGamepad());
	}
	
	boolean isPressed(GamepadInput gamepad);
	
	@Override
	default boolean isDown(Input input) {
		return isDown(input.getGamepad());
	}
	
	boolean isDown(GamepadInput gamepad);
	
}
