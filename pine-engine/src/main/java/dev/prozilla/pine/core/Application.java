package dev.prozilla.pine.core;

import dev.prozilla.pine.common.Lifecycle;
import dev.prozilla.pine.common.logging.AppLogger;
import dev.prozilla.pine.common.logging.Logger;
import dev.prozilla.pine.common.system.resource.Image;
import dev.prozilla.pine.common.system.resource.ResourcePool;
import dev.prozilla.pine.common.system.resource.Texture;
import dev.prozilla.pine.common.system.resource.text.Font;
import dev.prozilla.pine.core.mod.ModManager;
import dev.prozilla.pine.core.rendering.Renderer;
import dev.prozilla.pine.core.state.Timer;
import dev.prozilla.pine.core.state.Tracker;
import dev.prozilla.pine.core.state.config.Config;
import dev.prozilla.pine.core.state.input.Input;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import java.util.HashMap;
import java.util.Map;

import static java.lang.Thread.sleep;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL43.GL_DEBUG_OUTPUT;

/**
 * 2D application using the LWJGL library.
 */
public class Application implements Lifecycle {
	
	// State
	/** True if the application has been initialized */
	public boolean initialized;
	/** True if OpenGL has been initialized */
	public static boolean initializedOpenGL = false;
	public boolean isRunning;
	protected boolean shouldStop;
	public boolean isPaused;
	protected boolean isPreview;
	
	// Scene
	public Scene currentScene;
	private final Map<Integer, Scene> scenes;
	
	// Helpers
	protected final Config config;
	protected final AppLogger logger;
	protected final Timer timer;
	protected final Renderer renderer;
	protected final Window window;
	protected Input input;
	protected final Tracker tracker;
	protected final ModManager modManager;
	
	private GLFWErrorCallback errorCallback;
	
	public static final String DEFAULT_TITLE = "Untitled";
	public static final int DEFAULT_TARGET_FPS = 60;
	
	/**
	 * Creates an application titled "Application".
	 * @param width Width of the window
	 * @param height height of the window
	 */
	public Application(int width, int height) {
		this(DEFAULT_TITLE, width, height);
	}
	
	/**
	 * Creates an application.
	 * @param title Title of the application
	 * @param width Width of the window
	 * @param height height of the window
	 */
	public Application(String title, int width, int height) {
		this(title, width, height, null);
	}
	
	/**
	 * Creates an application.
	 * @param title Title of the application
	 * @param width Width of the window
	 * @param height height of the window
	 * @param scene Starting scene
	 */
	public Application(String title, int width, int height, Scene scene) {
		this(title, width, height, scene, DEFAULT_TARGET_FPS);
	}
	
	/**
	 * Creates an application.
	 * @param title Title of the application
	 * @param width Width of the window
	 * @param height height of the window
	 * @param scene Starting scene
	 * @param targetFps Amount of frames per second to target
	 */
	public Application(String title, int width, int height, Scene scene, int targetFps) {
		logger = new AppLogger(this);
		config = new Config(this);
		
		config.fps.set(targetFps);
		config.window.title.set(title);
		config.window.width.set(width);
		config.window.height.set(height);
		
		timer = new Timer();
		tracker = new Tracker(this);
		renderer = new Renderer(this);
		window = new Window(this);
		input = new Input(this);
		modManager = new ModManager(this);
		
		initialized = false;
		isRunning = false;
		isPaused = false;
		shouldStop = false;
		isPreview = false;

		// Prepare scene
		if (scene == null) {
			scene = new Scene();
			scene.setApplication(this);
		}
		scenes = new HashMap<>();
		addScene(scene);
		currentScene = scene;
	}
	
	/**
	 * Initializes and starts the application and handles exceptions.
	 */
	public void run() {
		try {
			init();
			start();
		} catch (RuntimeException e) {
			// Force application to quit if it is still running
			if (isRunning) {
				destroy();
			}
			
			// Log exception
			logger.error("Application failed", e);
		} finally {
			logger.log("Application finished");
		}
	}
	
	/**
	 * Initializes the application.
	 */
	public void init() throws RuntimeException {
		if (initialized) {
			throw new IllegalStateException("Application has already been initialized.");
		}
		
		isRunning = true;
		
		logger.init();
		
		// Set error callback
		errorCallback = GLFWErrorCallback.createPrint(System.err).set();
		
		// Initialize GLFW
		if (!glfwInit()) {
			throw new IllegalStateException("Unable to initialize GLFW");
		}
		logger.log("Initialized GLFW (Initialization: 1/4)");
		
		// Create window
		window.init();
		logger.log("Created window (Initialization: 2/4)");
		
		// Initialize OpenGL
		GL.createCapabilities();
		glEnable(GL_DEBUG_OUTPUT);
		initializedOpenGL = true;
		logger.log("Initialized OpenGL (Initialization: 3/4)");
		
		// Initialize application
		timer.init();
		renderer.init();
		input.init(window.id);
		currentScene.init(window.id);
		loadIcons();
		modManager.init();
		logger.log("Initialized application (Initialization: 4/4)");
		
		initialized = true;
	}
	
	public void initPreview(Input input, int width, int height) {
		if (initialized) {
			throw new IllegalStateException("Preview has already been initialized.");
		}
		
		isPreview = true;
		isRunning = true;
		
		this.input = input;
		
		timer.init();
		renderer.initPreview(width, height);
		currentScene.init(renderer.getFbo().getId());
		logger.log("Initialized preview");
		
		initialized = true;
	}
	
	/**
	 * Starts the application loop.
	 * Destroys the application after the application loop has been stopped.
	 */
	@Override
	public void start() {
		int fps = config.fps.get();
		long targetTime = (fps == 0) ? 0 : 1000L / fps;
		
		logger.logf("Starting application (fps: %s)", fps);
		
		// Application loop
		while (!window.shouldClose() && !shouldStop) {
			long startTime = (long) (timer.getTime() * 1000);
			float deltaTime = timer.getDelta();
			
			// Handle input and update application
			try {
				input(deltaTime);
				update(deltaTime);
			} catch (Exception e) {
				logger.error("Failed to update application", e);
			} finally {
				timer.updateUPS();
			}
			
			// Abort application loop
			if (shouldStop) {
				break;
			}
			
			// Render application
			try {
				resize();
				render(renderer);
			} catch (Exception e) {
				logger.error("Failed to render application", e);
				
				// Abort rendering
				if (renderer.isDrawing()) {
					renderer.end();
				}
			} finally {
				timer.updateFPS();
			}
			
			// Check for OpenGL errors
			int error = glGetError();
			if (error != GL_NO_ERROR) {
				logger.error("OpenGL error: " + error);
			}
			
			// Update the window and timer
			window.update();
			timer.update();
			
			long endTime = (long) (timer.getTime() * 1000);
			
			// Sleep until target time is reached, to match target fps
			if (fps != 0) {
				try {
					long timeout = startTime + targetTime - endTime;
					
					if (timeout > 0) {
						sleep(timeout);
					}
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				}
			}
		}
		
		// Stop the application
		destroy();
	}
	
	/**
	 * Handles input for the application.
	 */
	@Override
	public void input(float deltaTime) {
		input.input();
		modManager.beforeInput(deltaTime);
		if (currentScene != null && currentScene.initialized) {
			currentScene.input(deltaTime);
		}
		modManager.afterInput(deltaTime);
	}
	
	/**
	 * Updates the application.
	 */
	@Override
	public void update(float deltaTime) {
		input.update();
		modManager.beforeUpdate(deltaTime);
		if (currentScene != null && currentScene.initialized) {
			currentScene.update(deltaTime);
		}
		modManager.afterUpdate(deltaTime);
	}
	
	public void updatePreview(float deltaTime) {
		if (currentScene != null && currentScene.initialized) {
			currentScene.update(deltaTime);
		}
		timer.updateFPS();
		timer.update();
	}
	
	/**
	 * Renders the application.
	 */
	@Override
	public void render(Renderer renderer) {
		window.refreshSize();
		renderer.clear();
		renderer.begin();
		modManager.beforeRender();
		if (currentScene != null && currentScene.initialized) {
			currentScene.render(renderer);
		}
		modManager.afterRender();
		renderer.end();
	}
	
	public void renderPreview() {
		try {
			resize();
			render(renderer);
		} catch (Exception e) {
			logger.trace(e);
		}
	}
	
	/**
	 * Pauses the application.
	 */
	public void pause() {
		timer.timeScale = 0;
		isPaused = true;
	}
	
	/**
	 * Resumes the application panel.
	 */
	public void resume() {
		timer.timeScale = 1;
		isPaused = false;
	}
	
	/**
	 * Pauses or resumes the application panel based on the current state.
	 */
	public void togglePause() {
		if (isPaused) {
			resume();
		} else {
			pause();
		}
	}
	
	/**
	 * Tells the application to stop after the current frame.
	 */
	public void stop() {
		shouldStop = true;
		isRunning = false;
	}
	
	/**
	 * Stops the application and cleans up resources.
	 * This should not be called before the application loop ends.
	 */
	@Override
	public void destroy() {
		isRunning = false;
		
		if (isStandalone()) {
			logger.log("Stopping application");
		} else {
			logger.log("Stopping preview");
		}
		
		renderer.destroy();
		
		if (isStandalone()) {
			input.destroy();
			modManager.destroy();
			
			// Reset resources
			Texture.reset();
			ResourcePool.clear();
			
			// Destroy window and release callbacks
			window.destroy();
			if (currentScene.initialized) {
				currentScene.destroy();
			}
			
			// Terminate GLFW and release error callback
			glfwTerminate();
			errorCallback.free();
		} else {
			if (currentScene.initialized) {
				currentScene.destroy();
			}
		}
	}
	
	/**
	 * Logs versions of libraries to the console
	 */
	public static void printVersions() {
		Logger.system.log("Java " + System.getProperty("java.version"));
		Logger.system.log("LWJGL " + Version.getVersion());
	}
	
	/**
	 * Adds a scene and returns its ID.
	 * @param scene Scene
	 * @return Scene ID
	 */
	public int addScene(Scene scene) {
		int id = scene.getId();
		scenes.put(id, scene);
		scene.setApplication(this);
		return id;
	}
	
	/**
	 * Reloads the current scene.
	 */
	public void reloadScene() {
		int id = currentScene.getId();
		unloadScene();
		loadScene(id);
	}
	
	/**
	 * Loads a scene by reference.
	 * @param scene Reference to the scene
	 */
	public void loadScene(Scene scene) {
		loadScene(scene.getId());
	}
	
	/**
	 * Loads the next scene.
	 */
	public void nextScene() {
		loadScene((currentScene.getId() + 1) % scenes.size());
	}
	
	/**
	 * Loads the previous scene.
	 */
	public void previousScene() {
		loadScene((currentScene.getId() + scenes.size() - 1) % scenes.size());
	}
	
	/**
	 * Loads a scene by ID.
	 * @param id Scene ID
	 */
	public void loadScene(int id) {
		// Check if scene is already loaded
		if (currentScene != null) {
			if (currentScene.getId() == id) {
				return;
			} else {
				unloadScene();
			}
		}
		
		logger.log("Loading scene: " + id);
		
		currentScene = scenes.get(id);
		isRunning = true;
		
		if (!currentScene.initialized) {
			currentScene.init(window.id);
		}
		
		// Make sure game is resumed when new scene is loaded
		resume();
	}
	
	/**
	 * Unloads the current scene.
	 */
	public void unloadScene() {
		logger.log("Unloading scene: " + currentScene.getId());
		isRunning = false;
		
		if (currentScene != null) {
			if (currentScene.loaded) {
				currentScene.destroy();
			}
			currentScene.reset();
		}
		currentScene = null;
	}
	
	public void setIcons(String... icons) {
		config.window.icon.set(icons);
		
		// Reload icons if they were changed after initialization
		if (initializedOpenGL) {
			loadIcons();
		}
	}
	
	/**
	 * Loads the window icons.
	 */
	public void loadIcons() {
		if (!config.window.icon.exists()) {
			return;
		}
		
		String[] icons = config.window.icon.get();
		
		if (icons.length == 0 || isPreview()) {
			return;
		}
		
		try {
			Image[] images = new Image[icons.length];
			for (int i = 0; i < icons.length; i++) {
				images[i] = ResourcePool.loadImage(icons[i]);
			}
			window.setIcons(images);
		} catch (Exception e) {
			logger.error("Failed to load icons.", e);
		}
	}
	
	public void setDefaultFont(String fontPath) {
		config.defaultFontPath.set(fontPath);
	}
	
	public Font getDefaultFont() {
		if (!config.defaultFontPath.exists()) {
			return null;
		}
		return ResourcePool.loadFont(config.defaultFontPath.get());
	}
	
	/**
	 * Resizes the application window.
	 */
	public void resize() {
		if (window.getWidth() != renderer.getHeight() || window.getHeight() != renderer.getHeight()) {
			renderer.resize();
		}
	}
	
	public boolean isPreview() {
		return isPreview;
	}
	
	public boolean isStandalone() {
		return !isPreview;
	}
	
	public Input getInput() {
		return input;
	}
	
	public Window getWindow() {
		return window;
	}
	
	public Renderer getRenderer() {
		return renderer;
	}
	
	public Timer getTimer() {
		return timer;
	}
	
	public Tracker getTracker() {
		return tracker;
	}
	
	public Config getConfig() {
		return config;
	}
	
	public ModManager getModManager() {
		return modManager;
	}
	
	public Logger getLogger() {
		return logger;
	}
}
