package dev.prozilla.pine.core;

import dev.prozilla.pine.core.rendering.RenderMode;
import dev.prozilla.pine.core.scene.Scene;
import dev.prozilla.pine.core.state.config.Config;
import dev.prozilla.pine.core.state.config.LogConfig;
import dev.prozilla.pine.core.state.config.RenderConfig;
import dev.prozilla.pine.core.state.config.WindowConfig;

import java.util.function.Function;

/**
 * Utility class for building applications.
 * @see Application
 */
public class ApplicationBuilder {
	
	// App constructor parameters
	private String title;
	private int windowWidth;
	private int windowHeight;
	private Scene initialScene;
	private int targetFps;
	private ApplicationMode mode;
	
	// App setters parameters
	private String[] icons;
	private String defaultFontPath;
	
	// App configuration
	private Config config;

	// Factories
	private Function<Application, ApplicationManager> applicationManagerFactory;
	
	public ApplicationBuilder() {
		title = Application.DEFAULT_TITLE;
		windowWidth = 800;
		windowHeight = 600;
		initialScene = null;
		targetFps = Application.DEFAULT_TARGET_FPS;
		mode = Application.DEFAULT_MODE;
		
		config = new Config();
		config.removeOption(Config.FPS);
		config.removeOption(WindowConfig.TITLE);
		config.removeOption(WindowConfig.WIDTH);
		config.removeOption(WindowConfig.HEIGHT);
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
	
	public ApplicationBuilder setMode(ApplicationMode mode) {
		this.mode = mode;
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
	
	public ApplicationBuilder setApplicationManagerFactory(Function<Application, ApplicationManager> applicationManagerFactory) {
		this.applicationManagerFactory = applicationManagerFactory;
		return this;
	}
	
	public ApplicationBuilder setFullscreen(boolean fullscreen) {
		config.window.fullscreen.setValue(fullscreen);
		return this;
	}
	
	public ApplicationBuilder setShowWindowDecorations(boolean showWindowDecorations) {
		config.window.showDecorations.setValue(showWindowDecorations);
		return this;
	}
	
	public ApplicationBuilder setEnableLogs(boolean enableLogs) {
		config.logging.enableLogs.setValue(enableLogs);
		return this;
	}
	
	public ApplicationBuilder setRenderMode(RenderMode renderMode) {
		config.rendering.renderMode.setValue(renderMode);
		return this;
	}
	
	public Config getConfig() {
		return config;
	}
	
	public void setConfig(Config config) {
		this.config = config;
	}
	
	public LogConfig getLogConfig() {
		return config.logging;
	}
	
	public WindowConfig getWindowConfig() {
		return config.window;
	}
	
	public RenderConfig getRenderConfig() {
		return config.rendering;
	}
	
	/**
	 * Creates a new application.
	 */
	public Application build() {
		Application application = new Application(title, windowWidth, windowHeight, initialScene, targetFps, mode);
		
		if (applicationManagerFactory != null) {
			application.setApplicationManager(applicationManagerFactory.apply(application));
		}
		
		if (applicationManagerFactory != null) {
			application.setApplicationManager(applicationManagerFactory.apply(application));
		}
		
		// Setters
		if (icons != null) {
			application.setIcons(icons);
		}
		if (defaultFontPath != null) {
			application.setDefaultFont(defaultFontPath);
		}
		
		// Configuration
		application.getConfig().copyFrom(this.config);
		
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
