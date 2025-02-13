package dev.prozilla.pine.core;

import dev.prozilla.pine.core.state.config.Config;
import dev.prozilla.pine.core.state.config.LogConfig;
import dev.prozilla.pine.core.state.config.WindowConfig;

/**
 * Utility class for building applications.
 * @see Application
 */
public final class ApplicationBuilder {
	
	// App constructor parameters
	private String title;
	private int windowWidth;
	private int windowHeight;
	private Scene initialScene;
	private int targetFps;
	
	// App setters parameters
	private String[] icons;
	private String defaultFontPath;
	
	// App configuration
	private boolean fullscreen;
	private boolean showWindowDecorations;
	private boolean enableLogs;
	
	public ApplicationBuilder() {
		title = Application.DEFAULT_TITLE;
		windowWidth = 800;
		windowHeight = 600;
		initialScene = null;
		targetFps = Application.DEFAULT_TARGET_FPS;
		
		LogConfig defaultLogConfig = new LogConfig();
		enableLogs = defaultLogConfig.enableLogs.get();
		
		WindowConfig defaultWindowConfig = new WindowConfig();
		showWindowDecorations = defaultWindowConfig.showDecorations.get();
		fullscreen = defaultWindowConfig.fullscreen.get();
	}
	
	/**
	 * Sets the title of the application's window.
	 * The default value is <code>"Untitled"</code>.
	 */
	public ApplicationBuilder setTitle(String title) {
		if (title == null) {
			title = "";
		}
		
		this.title = title;
		return this;
	}
	
	/**
	 * Sets the default width and height of the application's window.
	 * The default value is <code>800x600</code>.
	 */
	public ApplicationBuilder setWindowSize(int width, int height) {
		setWindowWidth(width);
		setWindowHeight(height);
		return this;
	}
	
	/**
	 * Sets the default width of the application's window.
	 * The default value is <code>800</code>.
	 */
	public ApplicationBuilder setWindowWidth(int width) {
		this.windowWidth = width;
		return this;
	}
	
	/**
	 * Sets the default height of the application's window.
	 * The default value is <code>600</code>.
	 */
	public ApplicationBuilder setWindowHeight(int height) {
		this.windowHeight = height;
		return this;
	}
	
	/**
	 * Sets the initial scene of the application.
	 */
	public ApplicationBuilder setInitialScene(Scene scene) {
		this.initialScene = scene;
		return this;
	}
	
	/**
	 * Sets the target frames per second of the application.
	 * The default value is <code>60</code>.
	 * Value <code>0</code> disables the fps cap.
	 * @param targetFps Frames per second
	 */
	public ApplicationBuilder setTargetFps(int targetFps) {
		this.targetFps = targetFps;
		return this;
	}
	
	public ApplicationBuilder unsetTargetFps() {
		targetFps = 0;
		return this;
	}
	
	/**
	 * Sets the icons of the application's window.
	 * @param icons String paths of the icons relative to the resources directory.
	 */
	public ApplicationBuilder setIcons(String... icons) {
		this.icons = icons;
		return this;
	}
	
	public ApplicationBuilder setDefaultFont(String fontPath) {
		defaultFontPath = fontPath;
		return this;
	}
	
	public ApplicationBuilder setFullscreen(boolean fullscreen) {
		this.fullscreen = fullscreen;
		return this;
	}
	
	public ApplicationBuilder setShowWindowDecorations(boolean showWindowDecorations) {
		this.showWindowDecorations = showWindowDecorations;
		return this;
	}
	
	public ApplicationBuilder setEnableLogs(boolean enableLogs) {
		this.enableLogs = enableLogs;
		return this;
	}
	
	/**
	 * Creates a new application.
	 */
	public Application build() {
		Application application = new Application(title, windowWidth, windowHeight, initialScene, targetFps);
		
		// Setters
		if (icons != null) {
			application.setIcons(icons);
		}
		if (defaultFontPath != null) {
			application.setDefaultFont(defaultFontPath);
		}
		
		// Configuration
		Config config = application.getConfig();
		config.window.fullscreen.set(fullscreen);
		config.window.showDecorations.set(showWindowDecorations);
		config.logging.enableLogs.set(enableLogs);
		
		return application;
	}
	
	/**
	 * Creates a new application and runs it.
	 */
	public Application buildAndRun() {
		Application application = build();
		application.run();
		return application;
	}
}
