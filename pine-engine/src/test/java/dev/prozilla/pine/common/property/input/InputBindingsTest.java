package dev.prozilla.pine.common.property.input;

import dev.prozilla.pine.core.state.input.MouseButton;
import dev.prozilla.pine.core.state.input.gamepad.GamepadButton;
import dev.prozilla.pine.test.TestLoggingExtension;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith({TestLoggingExtension.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class InputBindingsTest {
	
	@Test
	void testBindingMutations() {
		InputBindings inputBindings = new InputBindings(new MouseButtonProperty(MouseButton.LEFT), new GamepadButtonProperty(GamepadButton.A));
		
		inputBindings.setBinding(0, MouseButton.RIGHT);
		assertEquals(new MouseButtonProperty(MouseButton.RIGHT), inputBindings.getBinding(0));
		assertEquals(new GamepadButtonProperty(GamepadButton.A), inputBindings.getBinding(1));
		
		inputBindings.setBinding(1, GamepadButton.B);
		assertEquals(new MouseButtonProperty(MouseButton.RIGHT), inputBindings.getBinding(0));
		assertEquals(new GamepadButtonProperty(GamepadButton.B), inputBindings.getBinding(1));
	}
	
}
