package dev.prozilla.pine.core;

import dev.prozilla.pine.core.state.Scene;

/**
 * Utility class for building applications.
 * @see Application
 */
public class ApplicationBuilder {
	
	// Constructor parameters
	private String title;
	private int windowWidth;
	private int windowHeight;
	private Scene initialScene;
	private int targetFps;
	
	// Setters parameters
	private String[] icons;
	
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
	public void setTitle(String title) {
		if (title == null) {
			title = "";
		}
		
		this.title = title;
	}
	
	/**
	 * Sets the default width and height of the application's window.
	 * The default value is <code>800x600</code>.
	 */
	public void setWindowSize(int width, int height) {
		setWindowWidth(width);
		setWindowHeight(height);
	}
	
	/**
	 * Sets the default width of the application's window.
	 * The default value is <code>800</code>.
	 */
	public void setWindowWidth(int width) {
		this.windowWidth = width;
	}
	
	/**
	 * Sets the default height of the application's window.
	 * The default value is <code>600</code>.
	 */
	public void setWindowHeight(int height) {
		this.windowHeight = height;
	}
	
	/**
	 * Sets the initial scene of the application.
	 */
	public void setInitialScene(Scene scene) {
		this.initialScene = scene;
	}
	
	/**
	 * Sets the target frames per second of the application.
	 * The default value is <code>60</code>.
	 * @param targetFps Frames per second
	 */
	public void setTargetFps(int targetFps) {
		this.targetFps = targetFps;
	}
	
	/**
	 * Sets the icons of the application's window.
	 * @param icons String paths of the icons relative to the resources directory.
	 */
	public void setIcons(String... icons) {
		this.icons = icons;
	}
	
	/**
	 * Creates an application.
	 */
	public Application build() {
		Application application = new Application(title, windowWidth, windowHeight, initialScene, targetFps);
		
		if (icons != null) {
			application.setIcons(icons);
		}
		
		return application;
	}
	
	/**
	 * Creates and runs the application.
	 */
	public Application buildAndRun() {
		Application application = build();
		application.run();
		return application;
	}
}
