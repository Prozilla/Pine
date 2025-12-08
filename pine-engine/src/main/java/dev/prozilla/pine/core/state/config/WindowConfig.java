package dev.prozilla.pine.core.state.config;

import dev.prozilla.pine.core.state.config.option.BooleanConfigOption;
import dev.prozilla.pine.core.state.config.option.IntConfigOption;
import dev.prozilla.pine.core.state.config.option.ObjectConfigOption;
import dev.prozilla.pine.core.state.config.option.StringConfigOption;

import java.util.Objects;

public class WindowConfig {
	
	// Predefined keys
	public static final ConfigKey<Integer> WIDTH = new ConfigKey<>("width", Integer.class);
	public static final ConfigKey<Integer> HEIGHT = new ConfigKey<>("height", Integer.class);
	public static final ConfigKey<String> TITLE = new ConfigKey<>("title", String.class);
	public static final ConfigKey<String[]> ICON = new ConfigKey<>("icon", String[].class);
	public static final ConfigKey<Boolean> SHOW_DECORATIONS = new ConfigKey<>("showDecorations", Boolean.class);
	public static final ConfigKey<Boolean> FULLSCREEN = new ConfigKey<>("fullscreen", Boolean.class);
	public static final ConfigKey<Boolean> ENABLE_VSYNC = new ConfigKey<>("enableVSync", Boolean.class);
	public static final ConfigKey<Boolean> ENABLE_TOGGLE_FULLSCREEN = new ConfigKey<>("enableToggleFullscreen", Boolean.class);
	
	// Predefines options
	/** Width of the window. Defaults to <code>900</code>. */
	public final IntConfigOption width = new IntConfigOption(900, (width) -> width > 0);
	/** Height of the window. Defaults to <code>600</code>. */
	public final IntConfigOption height = new IntConfigOption(600, (height) -> height > 0);
	/** Title of the window. Defaults to <code>"Untitled"</code>. */
	public final StringConfigOption title = new StringConfigOption("Untitled", Objects::nonNull);
	public final ObjectConfigOption<String[]> icon = new ObjectConfigOption<>(null);
	/** Whether the window will have window decorations such as a border, a close widget, etc. Defaults to <code>true</code>. */
	public final BooleanConfigOption showDecorations = new BooleanConfigOption(true);
	/** Defaults to <code>false</code>. */
	public final BooleanConfigOption fullscreen = new BooleanConfigOption(false);
	/** Defaults to <code>true</code>. */
	public final BooleanConfigOption enableVSync = new BooleanConfigOption(true);
	public final BooleanConfigOption enableToggleFullscreen = new BooleanConfigOption(true);
	
}
