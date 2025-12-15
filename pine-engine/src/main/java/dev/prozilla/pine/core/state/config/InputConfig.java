package dev.prozilla.pine.core.state.config;

import dev.prozilla.pine.core.state.config.option.BooleanConfigOption;
import dev.prozilla.pine.core.state.input.Input;

/**
 * Manages configuration options related to input.
 */
public class InputConfig {
	
	// Predefined keys
	public static final ConfigKey<Boolean> ENABLE_KEYBOARD = new ConfigKey<>("enableKeyboard", Boolean.class);
	public static final ConfigKey<Boolean> ENABLE_MOUSE = new ConfigKey<>("enableMouse", Boolean.class);
	public static final ConfigKey<Boolean> ENABLE_GAMEPAD = new ConfigKey<>("enableGamepad", Boolean.class);
	
	// Predefines options
	/** Determines whether the application listens to keyboard input. Defaults to {@code true}. The shorthand for this option is {@link Input#setKeyboardEnabled(boolean)}. */
	public final BooleanConfigOption enableKeyboard = new BooleanConfigOption(true);
	/** Determines whether the application listens to mouse input. Defaults to {@code true}. The shorthand for this option is {@link Input#setMouseEnabled(boolean)}. */
	public final BooleanConfigOption enableMouse = new BooleanConfigOption(true);
	/** Determines whether the application listens to gamepad input. Defaults to {@code true}. The shorthand for this option is {@link Input#setGamepadEnabled(boolean)}. */
	public final BooleanConfigOption enableGamepad = new BooleanConfigOption(true);
	
}
