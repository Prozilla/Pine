package dev.prozilla.pine.core;

import dev.prozilla.pine.Pine;
import dev.prozilla.pine.common.asset.image.Image;
import dev.prozilla.pine.common.asset.pool.AssetPools;
import dev.prozilla.pine.common.asset.text.Font;
import dev.prozilla.pine.common.lifecycle.*;
import dev.prozilla.pine.common.logging.AppLogger;
import dev.prozilla.pine.common.logging.Logger;
import dev.prozilla.pine.common.opengl.GLUtils;
import dev.prozilla.pine.common.property.SystemProperty;
import dev.prozilla.pine.core.audio.AudioDevice;
import dev.prozilla.pine.core.mod.ModManager;
import dev.prozilla.pine.core.rendering.Renderer;
import dev.prozilla.pine.core.scene.Scene;
import dev.prozilla.pine.core.state.*;
import dev.prozilla.pine.core.state.config.Config;
import dev.prozilla.pine.core.state.input.Input;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static java.lang.Thread.sleep;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL43.GL_DEBUG_OUTPUT;

/**
 * 2D application using the LWJGL library.
 */
public class Application implements Initializable, InputHandler, Updatable, Renderable, Destructible, ApplicationContext, StateProvider<Application, ApplicationState> {
	
	// State
	/** True if OpenGL has been initialized */
	public static boolean initializedOpenGL = false;
	protected boolean shouldStop;
	protected boolean isPreview;
	private static final SystemProperty devModeProperty = new SystemProperty("dev-mode");
	
	// Scene
	protected Scene currentScene;
	private final Map<Integer, Scene> scenes;
	
	// Helpers
	protected final Config config;
	protected final AppLogger logger;
	protected final ApplicationTimer timer;
	protected final Renderer renderer;
	protected final AudioDevice audioDevice;
	protected final Window window;
	protected Input input;
	protected final Tracker tracker;
	protected final ModManager modManager;
	protected final StateMachine<Application, ApplicationState> stateMachine;
	
	protected ApplicationManager applicationManager;
	
	private GLFWErrorCallback errorCallback;
	
	public static final String DEFAULT_TITLE = "Untitled";
	public static final int DEFAULT_TARGET_FPS = 60;
	
	/**
	 * Creates an application titled {@value DEFAULT_TITLE}.
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
		stateMachine = new StateMachine<>(ApplicationState.INITIALIZING, this);
		
		config.fps.setValue(targetFps);
		config.window.title.setValue(title);
		config.window.width.setValue(width);
		config.window.height.setValue(height);
		
		timer = new ApplicationTimer();
		tracker = new Tracker(this);
		renderer = new Renderer(this);
		audioDevice = new AudioDevice(this);
		window = new Window(this);
		input = new Input(this);
		modManager = new ModManager(this);
		
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
			if (isRunning()) {
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
	@Override
	public void init() throws RuntimeException {
		if (!stateMachine.isState(ApplicationState.INITIALIZING)) {
			throw new IllegalStateException("Application has already been initialized");
		}
		
		logger.init();
		if (config.logging.enableApplicationStateLogs.getValue()) {
			logger.log("Initializing application");
		}
		
		// Set error callback
		errorCallback = GLFWErrorCallback.createPrint(System.err).set();
		
		// Initialize GLFW
		if (!glfwInit()) {
			throw new IllegalStateException("Failed to initialize GLFW");
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
		audioDevice.init();
		input.init();
		if (applicationManager != null) {
			applicationManager.onInit(window.getId());
		}
		currentScene.init();
		loadIcons();
		modManager.init();
		
		stateMachine.changeState(ApplicationState.LOADING);
	}
	
	public void initPreview(Input input, int width, int height) {
		if (!stateMachine.isState(ApplicationState.INITIALIZING)) {
			throw new IllegalStateException("Preview has already been initialized");
		}
		
		logger.init();
		if (config.logging.enableApplicationStateLogs.getValue()) {
			logger.log("Initializing preview");
		}
		
		isPreview = true;
		
		this.input = input;
		
		timer.init();
		renderer.initPreview(width, height);
		audioDevice.init();
		currentScene.init();
		
		stateMachine.changeState(ApplicationState.LOADING);
	}
	
	/**
	 * Starts the application loop.
	 * Destroys the application after the application loop has been stopped.
	 */
	public void start() {
		int fps = config.fps.getValue();
		double targetTime = (fps == 0) ? 0.0 : 1.0 / fps;
		
		stateMachine.changeState(ApplicationState.RUNNING);
		logger.logf("Starting application (fps: %s)", fps);
		
		if (applicationManager != null) {
			applicationManager.onStart();
		}
		
		// Application loop
		while (!window.shouldClose() && !shouldStop) {
			double startTime = timer.getCurrentTime();
			timer.update();
			float deltaTime = timer.getDeltaTime();
			
			// Handle input and update application
			try {
				input(deltaTime);
				update(deltaTime);
			} catch (Exception e) {
				logger.error("Failed to update application", e);
			} finally {
				timer.incrementUPS();
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
				timer.incrementFPS();
			}
			
			// Check for OpenGL errors
			int error = glGetError();
			if (error != GL_NO_ERROR) {
				logger.error(String.format("OpenGL error: %s - %s", error, GLUtils.getErrorString(error)));
			}
			
			// Update the window and timer
			window.update();
			timer.checkCounters();
			
			double endTime = timer.getCurrentTime();
			
			// Sleep until target time is reached, to match target fps
			if (fps != 0) {
				try {
					double timeout = startTime + targetTime - endTime;
					
					if (timeout > 0) {
						sleep((long)(timeout * 1000));
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
		if (applicationManager != null) {
			applicationManager.onInput(deltaTime);
		}
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
		if (applicationManager != null) {
			applicationManager.onUpdate(deltaTime);
		}
		if (currentScene != null && currentScene.initialized) {
			currentScene.update(deltaTime);
		}
		modManager.afterUpdate(deltaTime);
	}
	
	public void updatePreview(float deltaTime) {
		if (currentScene != null && currentScene.initialized) {
			currentScene.update(deltaTime);
		}
		timer.incrementFPS();
		timer.checkCounters();
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
		if (applicationManager != null) {
			applicationManager.onRender(renderer);
		}
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
		stateMachine.changeState(ApplicationState.RUNNING, ApplicationState.PAUSED);
		if (applicationManager != null) {
			applicationManager.onPause();
		}
	}
	
	/**
	 * Resumes the application panel.
	 */
	public void resume() {
		timer.timeScale = 1;
		stateMachine.changeState(ApplicationState.PAUSED, ApplicationState.RUNNING);
		if (applicationManager != null) {
			applicationManager.onResume();
		}
	}
	
	/**
	 * Pauses or resumes the application panel based on the current state.
	 */
	public void togglePause() {
		if (stateMachine.isState(ApplicationState.PAUSED)) {
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
		stateMachine.changeState(ApplicationState.STOPPED);
	}
	
	/**
	 * Stops the application and cleans up resources.
	 * This should not be called before the application loop ends.
	 */
	@Override
	public void destroy() {
		stateMachine.changeState(ApplicationState.STOPPED);
		
		renderer.destroy();
		audioDevice.destroy();
		
		if (isStandalone()) {
			input.destroy();
			modManager.destroy();
			
			// Reset resources
			AssetPools.clear();
			
			// Destroy window and release callbacks
			window.destroy();
			if (applicationManager != null) {
				applicationManager.onDestroy();
			}
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
	 * Prints system information and library versions.
	 */
	public void printInfo() {
		Logger logger = Objects.requireNonNullElse(this.logger, Logger.system);
		Pine.print(logger);
		if (audioDevice != null && audioDevice.isAvailable()) {
			logger.text( "OpenAL version: " + audioDevice.getALVersion());
		}
		logger.text("Dev mode: " + isDevMode());
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
		stateMachine.changeState(ApplicationState.LOADING);
		
		if (!currentScene.initialized) {
			currentScene.init();
		}
		
		// Make sure game is resumed when new scene is loaded
		resume();
		stateMachine.changeState(ApplicationState.RUNNING);
	}
	
	/**
	 * Unloads the current scene.
	 */
	public void unloadScene() {
		logger.log("Unloading scene: " + currentScene.getId());
		stateMachine.changeState(ApplicationState.LOADING);
		
		if (currentScene != null) {
			if (currentScene.loaded) {
				currentScene.destroy();
			}
			currentScene.reset();
		}
		currentScene = null;
	}
	
	public Scene getCurrentScene() {
		return currentScene;
	}
	
	public void setApplicationManager(ApplicationManager applicationManager) {
		this.applicationManager = applicationManager;
	}
	
	/**
	 * Checks whether this application hasn't been stopped yet.
	 */
	public boolean isRunning() {
		return !stateMachine.isState(ApplicationState.STOPPED);
	}
	
	public boolean isLoading() {
		return stateMachine.isState(ApplicationState.LOADING);
	}
	
	public boolean isPaused() {
		return stateMachine.isState(ApplicationState.PAUSED);
	}
	
	/**
	 * @return The state of this application
	 */
	@Override
	public ApplicationState getState() {
		return stateMachine.getState();
	}
	
	/**
	 * Checks whether this application is in a given state.
	 */
	@Override
	public boolean isState(ApplicationState state) {
		return StateProvider.super.isState(state);
	}
	
	/**
	 * Checks whether this application is in any of the given states.
	 */
	@Override
	public boolean isAnyState(ApplicationState[] states) {
		return StateProvider.super.isAnyState(states);
	}
	
	/**
	 * Sets the icons of the application's window.
	 * @param icons Paths of icons
	 */
	public void setIcons(String... icons) {
		config.window.icon.setValue(icons);
		
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
		
		String[] icons = config.window.icon.getValue();
		
		if (icons.length == 0 || isPreview()) {
			return;
		}
		
		try {
			Image[] images = new Image[icons.length];
			for (int i = 0; i < icons.length; i++) {
				images[i] = AssetPools.images.load(icons[i]);
			}
			window.setIcons(images);
		} catch (Exception e) {
			logger.error("Failed to load icons", e);
		}
	}
	
	/**
	 * Sets the default font path for text elements.
	 * @param fontPath Path to the font file
	 */
	public void setDefaultFont(String fontPath) {
		config.defaultFontPath.setValue(fontPath);
	}
	
	/**
	 * @return The default font of this application.
	 */
	public Font getDefaultFont() {
		if (!config.defaultFontPath.exists()) {
			return null;
		}
		return AssetPools.fonts.load(config.defaultFontPath.getValue());
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
	
	@Override
	public Input getInput() {
		return input;
	}
	
	@Override
	public Window getWindow() {
		return window;
	}
	
	@Override
	public Renderer getRenderer() {
		return renderer;
	}
	
	@Override
	public ApplicationTimer getTimer() {
		return timer;
	}
	
	@Override
	public Tracker getTracker() {
		return tracker;
	}
	
	@Override
	public Config getConfig() {
		return config;
	}
	
	@Override
	public ModManager getModManager() {
		return modManager;
	}
	
	@Override
	public Logger getLogger() {
		return logger;
	}
	
	@Override
	public AudioDevice getAudioDevice() {
		return audioDevice;
	}
	
	/**
	 * Throws an exception if OpenGL has not been initialized yet.
	 * @throws IllegalStateException If OpenGL has not been initialized yet.
	 */
	public static void requireOpenGL() throws IllegalStateException {
		if (!initializedOpenGL) {
			throw new IllegalStateException("OpenGL has not been initialized yet");
		}
	}
	
	/**
	 * Checks if the application is running in developer mode.
	 *
	 * <p>This method is optimized and will only read the system property once.</p>
	 * @see Application#readDevMode()
	 */
	public static boolean isDevMode() {
		return devModeProperty.getValue().equalsIgnoreCase("true");
	}
	
	/**
	 * Checks if the application is currently running in developer mode.
	 * @return {@code true} if system property {@code dev-mode} is currently set to {@code true}.
	 */
	public static boolean readDevMode() {
		return devModeProperty.fetch().equalsIgnoreCase("true");
	}
	
}
