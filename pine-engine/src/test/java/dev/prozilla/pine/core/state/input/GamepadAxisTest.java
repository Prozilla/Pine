package dev.prozilla.pine.core.state.input;

import dev.prozilla.pine.core.state.input.gamepad.GamepadAxis;
import dev.prozilla.pine.test.TestLoggingExtension;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(TestLoggingExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GamepadAxisTest {
	
	@Test
	void testIsValid() {
		for (GamepadAxis gamepadAxis : GamepadAxis.values()) {
			assertTrue(GamepadAxis.isValid(gamepadAxis.getValue()));
		}
	}
	
}