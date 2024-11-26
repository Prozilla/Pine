package dev.prozilla.pine.core.state;

import dev.prozilla.pine.core.Game;
import dev.prozilla.pine.common.Lifecycle;
import dev.prozilla.pine.core.object.GameObject;
import dev.prozilla.pine.core.object.World;
import dev.prozilla.pine.core.object.camera.Camera;
import dev.prozilla.pine.core.rendering.Renderer;
import dev.prozilla.pine.core.state.input.Input;

public class Scene implements Lifecycle {
	
	// Scene properties
	public String name;
	private final int id;
	
	// References
	public Game game;
	public Input input;
	public World world;
	public Camera camera;
	
	// Scene state
	public boolean loaded;
	public boolean initialized;
	public boolean started;
	
	private static int lastId = 0;
	
	public Scene() {
		this(null);
	}
	
	public Scene(Game game) {
		this.id = generateId();
		this.name = "Scene #" + this.id;
		
		setGame(game);
		reset();
	}
	
	public Scene(Game game, String name) {
		this.name = name;
		this.id = generateId();
		
		setGame(game);
		reset();
	}
	
	/**
	 * Setter for the game reference.
	 * @param game Reference to the game
	 */
	public void setGame(Game game) {
		if (game == null) {
			return;
		}
		
		this.game = game;
		this.input = game.input;
	}
	
	/**
	 * Resets the scene.
	 */
	public void reset() {
		loaded = false;
		initialized = false;
		started = false;
	}
	
	/**
	 * Fills the scene with a new world and camera.
	 */
	protected void load() {
		if (world == null) {
			world = new World(game, this);
		}
		if (camera == null) {
			camera = new Camera(game);
			add(camera);
		}
		loaded = true;
	}
	
	/**
	 * Initializes the scene and its children.
	 */
	@Override
	public void init(long window) throws IllegalStateException {
		if (initialized) {
			throw new IllegalStateException("Scene has already been initialized");
		}
		
		load();
		
		world.init(window);
		initialized = true;
	}
	
	/**
	 * Starts the scene.
	 */
	@Override
	public void start() throws IllegalStateException {
		if (started) {
			throw new IllegalStateException("Scene has already started");
		}
		if (!initialized) {
			throw new IllegalStateException("Scene has not been initialized yet");
		}
		
		world.start();
		started = true;
	}
	
	/**
	 * Handles input for the scene.
	 * @param deltaTime Delta time in seconds
	 */
	@Override
	public void input(float deltaTime) throws IllegalStateException {
		checkStatus();
		world.input(deltaTime);
	}
	
	/**
	 * Updates the scene every frame.
	 * @param deltaTime Delta time in seconds
	 */
	@Override
	public void update(float deltaTime) throws IllegalStateException {
		checkStatus();
		world.update(deltaTime);
	}
	
	/**
	 * Renders the scene every frame.
	 */
	@Override
	public void render(Renderer renderer) throws IllegalStateException {
		checkStatus();
		world.render(renderer);
	}
	
	/**
	 * Destroys the scene.
	 */
	@Override
	public void destroy() throws IllegalStateException {
		checkStatus();
		world.destroy();
		
		// Remove all references
//		game = null;
		input = null;
		world = null;
		camera = null;
		
//		Texture.reset();
	}
	
	/**
	 * Checks whether the scene is ready.
	 */
	protected void checkStatus() throws IllegalStateException {
		if (!started || !initialized || !loaded) {
			throw new IllegalStateException("Scene is not ready yet");
		}
	}
	
	/**
	 * Adds a game object to the scene.
	 * @param gameObject GameObject
	 */
	public GameObject add(GameObject gameObject) {
		return world.addChild(gameObject);
	}
	
	/**
	 * Removes a game object from the scene.
	 * @param gameObject GameObject
	 */
	public void remove(GameObject gameObject) {
		world.removeChild(gameObject);
	}
	
	/**
	 * Generates a new unique scene ID.
	 * @return Scene ID
	 */
	public static int generateId() {
		return lastId++;
	}
	
	/**
	 * Getter for the scene ID.
	 * @return Scene ID
	 */
	public int getId() {
		return id;
	}
	
}
