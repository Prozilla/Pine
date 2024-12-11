package dev.prozilla.pine.core.state;

import dev.prozilla.pine.core.Application;
import dev.prozilla.pine.common.Lifecycle;
import dev.prozilla.pine.core.context.Window;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.World;
import dev.prozilla.pine.core.entity.camera.Camera;
import dev.prozilla.pine.core.rendering.Renderer;

public class Scene implements Lifecycle {
	
	// Scene properties
	public String name;
	private final int id;
	
	// References
	protected Application application;
	protected World world;
	protected Camera camera;
	
	// Scene state
	public boolean loaded;
	public boolean initialized;
	
	private static int lastId = 0;
	
	public Scene() {
		this(null);
	}
	
	public Scene(Application application) {
		this.id = generateId();
		this.name = "Scene #" + this.id;
		
		setApplication(application);
		reset();
	}
	
	public Scene(Application application, String name) {
		this.name = name;
		this.id = generateId();
		
		setApplication(application);
		reset();
	}
	
	public void setApplication(Application application) {
		if (application == null) {
			return;
		}
		
		this.application = application;
	}
	
	public void reset() {
		loaded = false;
		initialized = false;
	}
	
	/**
	 * Fills the scene with a new world and camera.
	 */
	protected void load() {
		if (world == null) {
			world = new World(application, this);
			world.initSystems();
		}
		if (camera == null) {
			camera = new Camera(world);
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
		System.out.println("Loaded scene");
		
		world.init(window);
		initialized = true;
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
//		application = null;
		world = null;
		camera = null;
		
//		Texture.reset();
	}
	
	/**
	 * Checks whether the scene is ready.
	 */
	protected void checkStatus() throws IllegalStateException {
		if (!initialized || !loaded) {
			throw new IllegalStateException("Scene is not ready yet");
		}
	}
	
	/**
	 * Adds an entity to the scene.
	 */
	@Deprecated
	public Entity add(Entity entity) {
		return world.addEntity(entity);
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
	
	public World getWorld() {
		return world;
	}
	
	public Camera getCamera() {
		return camera;
	}
}
