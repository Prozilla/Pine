package dev.prozilla.pine.core;

import dev.prozilla.pine.common.Lifecycle;
import dev.prozilla.pine.common.system.resource.ResourcePool;
import dev.prozilla.pine.common.system.resource.Texture;
import dev.prozilla.pine.core.state.Tracker;
import dev.prozilla.pine.core.state.input.Input;
import dev.prozilla.pine.common.system.resource.Image;
import dev.prozilla.pine.core.rendering.Renderer;
import dev.prozilla.pine.core.state.Scene;
import dev.prozilla.pine.core.state.Timer;
import dev.prozilla.pine.core.context.Window;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import java.util.HashMap;
import java.util.Map;

import static java.lang.Thread.sleep;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL43.GL_DEBUG_OUTPUT;

/**
 * 2D game using the LWJGL library.
 */
public class Game implements Lifecycle {
	
	// Game
	/** Title of the game */
	public String title;
	/** True if the game is paused */
	public boolean paused = false;
	protected final static int TARGET_FPS = 120;
	
	/** True if the game has been initialized */
	public static boolean initialized = false;
	/** True if OpenGL has been initialized */
	public static boolean initializedOpenGL = false;
	public boolean running;
	private boolean shouldStop;
	
	// Window
	public int windowWidth;
	public int windowHeight;
	
	// Scene
	public Scene currentScene;
	private final Map<Integer, Scene> scenes;
	
	// System
	public final Timer timer;
	protected final Renderer renderer;
	public final Window window;
	public final Input input;
	public final Tracker tracker;
	
	private GLFWErrorCallback errorCallback;
	
	/**
	 * Creates a game titled "Game".
	 * @param width Width of the window
	 * @param height height of the window
	 */
	public Game(int width, int height) {
		this("Game", width, height);
	}
	
	/**
	 * Creates a game.
	 * @param title Title of the game
	 * @param width Width of the window
	 * @param height height of the window
	 */
	public Game(String title, int width, int height) {
		this(title, width, height, null);
	}
	
	/**
	 * Creates a game.
	 * @param title Title of the game
	 * @param width Width of the window
	 * @param height height of the window
	 * @param scene Starting scene
	 */
	public Game(String title, int width, int height, Scene scene) {
		timer = new Timer();
		tracker = new Tracker(this);
		renderer = new Renderer(tracker);
		window = new Window(width, height, title);
		input = new Input(this);
		
		running = false;
		shouldStop = false;

		// Prepare scene
		if (scene == null) {
			scene = new Scene(this);
		}
		scenes = new HashMap<>();
		addScene(scene);
		currentScene = scene;
		
		this.title = title;
		this.windowWidth = width;
		this.windowHeight = height;
	}
	
	/**
	 * Initializes the game.
	 */
	@Override
	public void init() throws RuntimeException {
		running = true;
		
		// Set error callback
		errorCallback = GLFWErrorCallback.createPrint(System.err).set();
		glfwSetErrorCallback(errorCallback);
		
		// Initialize GLFW
		if (!glfwInit()) {
			throw new IllegalStateException("Unable to initialize GLFW");
		}
		System.out.println("Initialized GLFW (Initialization: 1/4)");
		
		// Create window
		window.init();
		System.out.println("Created window (Initialization: 2/4)");
		
		// Initialize OpenGL
		GL.createCapabilities();
		glEnable(GL_DEBUG_OUTPUT);
		initializedOpenGL = true;
		System.out.println("Initialized OpenGL (Initialization: 3/4)");
		
		// Initialize game
		timer.init();
		renderer.init();
		input.init(window.id);
		currentScene.init(window.id);
		System.out.println("Initialized game (Initialization: 4/4)");
		
		initialized = true;
	}
	
	/**
	 * Starts the game loop.
	 */
	@Override
	public void start() {
		long targetTime = 1000L / TARGET_FPS;
		
		System.out.printf("Starting game (fps: %s)%n", TARGET_FPS);
		currentScene.start();
		
		// Game loop
		while (!window.shouldClose() && !shouldStop) {
			long startTime = (long) (timer.getTime() * 1000);
			float deltaTime = timer.getDelta();
			
			// Handle input and update game
			try {
				input(deltaTime);
				update(deltaTime);
			} catch (Exception e) {
				System.err.println("Failed to update game");
				e.printStackTrace();
			} finally {
				timer.updateUPS();
			}
			
			// Abort game loop
			if (shouldStop) {
				break;
			}
			
			// Render game
			try {
				resize();
				render(renderer);
			} catch (Exception e) {
				System.err.println("Failed to render game");
				e.printStackTrace();
				
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
				System.err.println("OpenGL error: " + error);
			}
			
			// Update the window and timer
			window.update();
			timer.update();
			
			long endTime = (long) (timer.getTime() * 1000);
			try {
				long timeout = startTime + targetTime - endTime;
				
				if (timeout > 0) {
					sleep(timeout);
				}
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}
		
		// Stop the game
		destroy();
	}
	
	/**
	 * Handles input for the game.
	 * @param deltaTime Delta time in seconds
	 */
	@Override
	public void input(float deltaTime) {
		input.input();
		if (currentScene != null && currentScene.started) {
			currentScene.input(deltaTime);
		}
	}
	
	/**
	 * Updates the game.
	 * @param deltaTime Delta time in seconds
	 */
	@Override
	public void update(float deltaTime) {
		input.update();
		if (currentScene != null && currentScene.started) {
			currentScene.update(deltaTime);
		}
	}
	
	/**
	 * Renders the game.
	 */
	@Override
	public void render(Renderer renderer) {
//		System.out.println("-- Start of render --");
		renderer.clear();
		
		renderer.begin();
		if (currentScene != null && currentScene.started) {
			currentScene.render(renderer);
		}
		renderer.end();
//		System.out.println("-- End of render --");
	}
	
	/**
	 * Pauses the game.
	 */
	public void pause() {
		timer.timeScale = 0;
		paused = true;
	}
	
	/**
	 * Resumes the game panel.
	 */
	public void resume() {
		timer.timeScale = 1;
		paused = false;
	}
	
	/**
	 * Pauses or resumes the game panel based on the current state.
	 */
	public void togglePause() {
		if (paused) {
			resume();
		} else {
			pause();
		}
	}
	
	/**
	 * Tells the game to stop after the current frame.
	 */
	public void stop() {
		shouldStop = true;
		running = false;
	}
	
	/**
	 * Stops the game and cleans up resources.
	 * This should not be called before the game loop ends.
	 */
	@Override
	public void destroy() {
		running = false;
		
		System.out.println("Stopping game");
		
		// Destroy instances
		renderer.destroy();
		input.destroy();
		
		// Reset resources
		Texture.reset();
		ResourcePool.clear();
		
		// Destroy window and release callbacks
		window.destroy();
		currentScene.destroy();
		
		// Terminate GLFW and release error callback
		glfwTerminate();
		errorCallback.free();
	}
	
	/**
	 * Logs versions of libraries to the console
	 */
	public static void printVersions() {
		System.out.println("Java " + System.getProperty("java.version"));
		System.out.println("LWJGL " + Version.getVersion());
	}
	
	/**
	 * Adds a scene and returns its ID.
	 * @param scene Scene
	 * @return Scene ID
	 */
	public int addScene(Scene scene) {
		int id = scene.getId();
		scenes.put(id, scene);
		scene.setGame(this);
		return id;
	}
	
	/**
	 * Loads a scene by reference.
	 * @param scene Reference to the scene
	 */
	public void loadScene(Scene scene) {
		loadScene(scene.getId());
	}
	
	/**
	 * Loads a scene by ID.
	 * @param id Scene ID
	 */
	public void loadScene(int id) {
		if (currentScene.getId() == id) {
			return;
		}
		
		if (currentScene != null) {
			unloadScene();
		}
		
		System.out.println("Loading scene: " + id);
		
		currentScene = scenes.get(id);
		running = true;
		
		if (!currentScene.initialized) {
			currentScene.init(window.id);
		}
		if (!currentScene.started) {
			currentScene.start();
		}
	}
	
	/**
	 * Unloads the current scene.
	 */
	public void unloadScene() {
		System.out.println("Unloading scene: " + currentScene.getId());
		running = false;
		
		if (currentScene != null) {
			if (currentScene.loaded) {
				currentScene.destroy();
			}
			currentScene.reset();
		}
		currentScene = null;
	}
	
	/**
	 * Sets the window icons.
	 * @param icons Array of paths to icons.
	 */
	public void setIcons(String[] icons) {
		try {
			Image[] images = new Image[icons.length];
			for (int i = 0; i < icons.length; i++) {
				images[i] = ResourcePool.loadImage(icons[i]);
			}
			window.setIcons(images);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Resizes the game window.
	 */
	public void resize() {
		if (window.getWidth() != renderer.getHeight() || window.getHeight() != renderer.getHeight()) {
			renderer.resize();
		}
	}
	
	public Renderer getRenderer() {
		return renderer;
	}
}
