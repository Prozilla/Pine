package dev.prozilla.pine.core.state.config;

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
	public final ConfigOption<Integer> width = new ConfigOption<>(900, (width) -> width != null && width > 0);
	/** Height of the window. Defaults to <code>600</code>. */
	public final ConfigOption<Integer> height = new ConfigOption<>(600, (height) -> height != null && height > 0);
	/** Title of the window. Defaults to <code>"Untitled"</code>. */
	public final ConfigOption<String> title = new ConfigOption<>("Untitled", Objects::nonNull);
	public final ConfigOption<String[]> icon = new ConfigOption<>(null);
	/** Whether the window will have window decorations such as a border, a close widget, etc. Defaults to <code>true</code>. */
	public final ConfigOption<Boolean> showDecorations = new ConfigOption<>(true, Objects::nonNull);
	/** Defaults to <code>false</code>. */
	public final ConfigOption<Boolean> fullscreen = new ConfigOption<>(false, Objects::nonNull);
	/** Defaults to <code>true</code>. */
	public final ConfigOption<Boolean> enableVSync = new ConfigOption<>(true, Objects::nonNull);
	public final ConfigOption<Boolean> enableToggleFullscreen = new ConfigOption<>(true, Objects::nonNull);
	
}
