package dev.prozilla.pine.core.state.config;

import dev.prozilla.pine.common.system.Color;
import dev.prozilla.pine.core.rendering.RenderMode;
import dev.prozilla.pine.core.state.config.option.BooleanConfigOption;
import dev.prozilla.pine.core.state.config.option.ObjectConfigOption;

import java.util.Objects;

/**
 * Manages configuration options related to rendering.
 */
public class RenderConfig {
	
	// Predefined keys
	public static final ConfigKey<Color> FALLBACK_RENDER_COLOR = new ConfigKey<>("fallbackRenderColor", Color.class);
	public static final ConfigKey<Boolean> ENABLE_BLEND = new ConfigKey<>("enableBlend", Boolean.class);
	public static final ConfigKey<Boolean> ENABLE_DEPTH_TEST = new ConfigKey<>("enableDepthTest", Boolean.class);
	public static final ConfigKey<RenderMode> RENDER_MODE = new ConfigKey<>("renderMode", RenderMode.class);
	public static final ConfigKey<Boolean> SNAP_PIXELS = new ConfigKey<>("snapPixels", Boolean.class);
	public static final ConfigKey<Boolean> SNAP_TEXT = new ConfigKey<>("snapText", Boolean.class);
	
	// Predefines options
	/** Used as the render color for objects when no color is passed to the renderer. Defaults to white. */
	public final ObjectConfigOption<Color> fallbackRenderColor = new ObjectConfigOption<>(Color.white(), Objects::nonNull);
	/** Defaults to <code>true</code>. */
	public final BooleanConfigOption enableBlend = new BooleanConfigOption(true);
	/** Defaults to <code>true</code>. */
	public final BooleanConfigOption enableDepthTest = new BooleanConfigOption(true);
	/** Defaults to <code>RenderMode.NORMAL</code>. */
	public final ObjectConfigOption<RenderMode> renderMode = new ObjectConfigOption<>(RenderMode.NORMAL, Objects::nonNull);
	/** Enables snapping of pixels. Defaults to <code>false</code>. */
	public final BooleanConfigOption snapPixels = new BooleanConfigOption(false);
	/** Enables the snapping of text pixels. Defaults to <code>true</code>. */
	public final BooleanConfigOption snapText = new BooleanConfigOption(true);
	
}
