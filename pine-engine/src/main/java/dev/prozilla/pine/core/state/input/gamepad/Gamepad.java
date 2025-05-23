package dev.prozilla.pine.core.state.input.gamepad;

import dev.prozilla.pine.common.Lifecycle;
import org.lwjgl.glfw.GLFWGamepadState;
import org.lwjgl.system.MemoryUtil;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.glfwGetGamepadState;

/**
 * Handles input for gamepad devices by reading from {@link GLFWGamepadState}.
 */
public class Gamepad implements GamepadInput, Lifecycle {
	
	private final GLFWGamepadState state;
	private final int id;
	
	private final List<Integer> buttonsPressed;
	private final List<Integer> previousButtonsPressed;
	
	public Gamepad(int id) {
		this.id = id;
		
		state = new GLFWGamepadState(MemoryUtil.memAlloc(40));
		
		buttonsPressed = new ArrayList<>();
		previousButtonsPressed = new ArrayList<>();
	}
	
	@Override
	public void input() {
		previousButtonsPressed.clear();
		previousButtonsPressed.addAll(buttonsPressed);
		buttonsPressed.clear();
		
		// Poll state
		glfwGetGamepadState(id, state);
		
		for (GamepadButton button : GamepadButton.values()) {
			if (state.buttons(button.getValue()) == GLFW_PRESS) {
				buttonsPressed.add(button.getValue());
			}
		}
	}
	
	@Override
	public void destroy() {
		state.close();
	}
	
	@Override
	public float getAxis(int axis) {
		return state.axes(axis);
	}
	
	@Override
	public boolean getButton(int button) {
		return state.buttons(button) == GLFW_PRESS;
	}
	
	@Override
	public boolean getButtonDown(int button) {
		return buttonsPressed.contains(button) && !previousButtonsPressed.contains(button);
	}
	
}
