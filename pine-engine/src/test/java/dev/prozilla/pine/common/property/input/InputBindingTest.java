package dev.prozilla.pine.common.property.input;

import dev.prozilla.pine.core.state.input.Key;
import dev.prozilla.pine.core.state.input.gamepad.GamepadButton;
import dev.prozilla.pine.test.TestLoggingExtension;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith({TestLoggingExtension.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class InputBindingTest {
	
	@Test
	void testBindingMutations() {
		InputBinding inputBinding = new InputBinding(new KeyboardKeyProperty(Key.NUM_1));
		
		inputBinding.setBinding(Key.NUM_2);
		assertEquals(new KeyboardKeyProperty(Key.NUM_2), inputBinding.getBinding());
		
		inputBinding.setBinding(Key.NUM_3, Key.NUM_4);
		assertEquals(new KeyboardKeysProperty(Key.NUM_3, Key.NUM_4), inputBinding.getBinding());
		
		inputBinding.setBinding(GamepadButton.A);
		assertEquals(new GamepadButtonProperty(GamepadButton.A), inputBinding.getBinding());
	}
	
}
