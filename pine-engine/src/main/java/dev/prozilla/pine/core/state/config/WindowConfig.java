package dev.prozilla.pine.core.state.config;

public class WindowConfig {
	
	// Predefined keys
	public static final ConfigKey<Boolean> SHOW_DECORATIONS = new ConfigKey<>("showDecorations", Boolean.class);
	public static final ConfigKey<Boolean> FULLSCREEN = new ConfigKey<>("fullscreen", Boolean.class);
	
	// Predefines options
	/** Whether the window will have window decorations such as a border, a close widget, etc. Defaults to <code>true</code>. */
	public final ConfigOption<Boolean> showDecorations = new ConfigOption<>(true);
	/** Defaults to <code>false</code>. */
	public final ConfigOption<Boolean> fullscreen = new ConfigOption<>(false);
	
}
