package dev.prozilla.pine.core;

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
	
	public ApplicationBuilder() {
		title = "Untitled";
		windowWidth = 800;
		windowHeight = 600;
		initialScene = null;
		targetFps = 60;
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
	 * @param targetFps Frames per second
	 */
	public ApplicationBuilder setTargetFps(int targetFps) {
		this.targetFps = targetFps;
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
	
	/**
	 * Creates a new application.
	 */
	public Application build() {
		Application application = new Application(title, windowWidth, windowHeight, initialScene, targetFps);
		
		if (icons != null) {
			application.setIcons(icons);
		}
		if (defaultFontPath != null) {
			application.setDefaultFont(defaultFontPath);
		}
		
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
