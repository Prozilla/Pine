package dev.prozilla.pine.core;

import dev.prozilla.pine.common.Lifecycle;
import dev.prozilla.pine.common.Printable;
import dev.prozilla.pine.core.component.camera.CameraData;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.prefab.Prefab;
import dev.prozilla.pine.core.entity.prefab.camera.CameraPrefab;
import dev.prozilla.pine.core.rendering.Renderer;

public class Scene implements Lifecycle, Printable {
	
	// Scene properties
	public String name;
	private final int id;
	
	// References
	protected Application application;
	protected World world;
	protected CameraData cameraData;
	/** Prefab that will be used during scene loading to create a camera entity. */
	protected Prefab cameraPrefab;
	
	// Scene state
	public boolean loaded;
	public boolean initialized;
	
	private static int lastId = 0;
	
	/**
	 * Creates a new scene with a generated name.
	 * The name consists of <code>Scene #</code> followed by the scene ID.
	 */
	public Scene() {
		this(null);
	}
	
	/**
	 * Creates a new scene with a given name.
	 * @param name Name of the scene
	 */
	public Scene(String name) {
		this.id = generateId();
		this.name = (name != null) ? name : "Scene #" + this.id;
		
		reset();
	}
	
	public void setApplication(Application application) {
		this.application = application;
	}
	
	/**
	 * Resets the state of this scene.
	 */
	public void reset() {
		loaded = false;
		initialized = false;
	}
	
	protected void load() {
		load(null);
	}
	
	/**
	 * Fills the scene with a new world and camera.
	 * @param cameraPrefab Prefab for the camera entity.
	 */
	protected void load(Prefab cameraPrefab) throws IllegalStateException {
		// Create new world
		if (world == null) {
			world = new World(application, this);
			world.initSystems();
		}
		
		// Prepare camera prefab
		if (cameraPrefab == null) {
			cameraPrefab = new CameraPrefab();
		}
		this.cameraPrefab = cameraPrefab;
		
		// Create new camera from prefab
		if (cameraData == null) {
			Entity camera = world.addEntity(this.cameraPrefab);
			cameraData = camera.getComponent(CameraData.class);
			
			if (cameraData == null) {
				throw new IllegalStateException("Camera prefab is missing a CameraData component.");
			}
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
		application.getLogger().log("Loaded scene");
		
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
		world = null;
		cameraData = null;
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
	 * Generates a new unique scene ID.
	 * @return Scene ID
	 */
	public static int generateId() {
		return lastId++;
	}
	
	public int getId() {
		return id;
	}
	
	@Override
	public int hashCode() {
		return id;
	}
	
	/**
	 * Checks if this scene is equal to another scene by comparing both ID's.
	 * @param scene Other scene
	 * @return True if both scenes have the same ID.
	 */
	public boolean equals(Scene scene) {
		return scene.id == id;
	}
	
	/**
	 * Checks whether this scene is the application's current scene.
	 */
	public boolean isActive() {
		return application.currentScene.equals(this);
	}
	
	public World getWorld() {
		return world;
	}
	
	public CameraData getCameraData() {
		return cameraData;
	}
	
	@Override
	public String toString() {
		return String.format("%s (%s)", name, id);
	}
}
