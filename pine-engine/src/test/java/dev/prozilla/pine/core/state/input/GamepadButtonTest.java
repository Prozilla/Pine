package dev.prozilla.pine.core.state.input;

import dev.prozilla.pine.core.state.input.gamepad.GamepadButton;
import dev.prozilla.pine.test.TestLoggingExtension;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(TestLoggingExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GamepadButtonTest {
	
	@Test
	void testIsValid() {
		for (GamepadButton gamepadButton : GamepadButton.values()) {
			assertTrue(GamepadButton.isValid(gamepadButton.getValue()));
		}
	}
	
}