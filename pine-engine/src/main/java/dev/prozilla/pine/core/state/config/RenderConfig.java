package dev.prozilla.pine.core.state.config;

import dev.prozilla.pine.common.system.Color;
import dev.prozilla.pine.core.rendering.RenderMode;

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
	public final ConfigOption<Color> fallbackRenderColor = new ConfigOption<>(Color.white(), Objects::nonNull);
	/** Defaults to <code>true</code>. */
	public final ConfigOption<Boolean> enableBlend = new ConfigOption<>(true, Objects::nonNull);
	/** Defaults to <code>true</code>. */
	public final ConfigOption<Boolean> enableDepthTest = new ConfigOption<>(true, Objects::nonNull);
	/** Defaults to <code>RenderMode.NORMAL</code>. */
	public final ConfigOption<RenderMode> renderMode = new ConfigOption<>(RenderMode.NORMAL, Objects::nonNull);
	/** Enables snapping of pixels. Defaults to <code>false</code>. */
	public final ConfigOption<Boolean> snapPixels = new ConfigOption<>(false, Objects::nonNull);
	/** Enables the snapping of text pixels. Defaults to <code>true</code>. */
	public final ConfigOption<Boolean> snapText = new ConfigOption<>(true, Objects::nonNull);
	
}
