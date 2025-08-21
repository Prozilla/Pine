package dev.prozilla.pine.core.scene;

import dev.prozilla.pine.common.Printable;
import dev.prozilla.pine.common.lifecycle.*;
import dev.prozilla.pine.common.logging.Logger;
import dev.prozilla.pine.common.util.checks.Checks;
import dev.prozilla.pine.core.Application;
import dev.prozilla.pine.core.ApplicationProvider;
import dev.prozilla.pine.core.component.camera.CameraData;
import dev.prozilla.pine.core.component.ui.NodeRoot;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.EntityEventType;
import dev.prozilla.pine.core.entity.prefab.Prefab;
import dev.prozilla.pine.core.entity.prefab.camera.CameraPrefab;
import dev.prozilla.pine.core.entity.prefab.ui.LayoutPrefab;
import dev.prozilla.pine.core.entity.prefab.ui.NodeRootPrefab;
import dev.prozilla.pine.core.entity.prefab.ui.dev.DevConsolePrefab;
import dev.prozilla.pine.core.rendering.Renderer;
import dev.prozilla.pine.core.state.input.Key;
import dev.prozilla.pine.core.system.standard.ui.dev.DevConsoleInputHandler;
import org.jetbrains.annotations.NotNull;

/**
 * Responsible for loading objects into the world.
 */
public class Scene implements Initializable, InputHandler, Updatable, Renderable, Destructible, Printable, SceneContext, ApplicationProvider {
	
	// Scene properties
	public String name;
	private final int id;
	
	// References
	protected Application application;
	protected Logger logger;
	protected World world;
	protected CameraData cameraData;
	/** Prefab that will be used during scene loading to create a camera entity. */
	protected Prefab cameraPrefab;
	
	// Developer console
	protected LayoutPrefab devConsolePrefab;
	protected Entity devConsole;
	protected NodeRoot devConsoleRoot;
	
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
		
		devConsolePrefab = new DevConsolePrefab();
		
		reset();
	}
	
	public void setApplication(Application application) {
		this.application = Checks.isNotNull(application, "application");
		logger = application.getLogger();
	}
	
	/**
	 * Resets the state of this scene.
	 */
	public void reset() {
		loaded = false;
		initialized = false;
	}
	
	/**
	 * Loads this scene with the default camera prefab.
	 */
	protected void load() {
		load(null);
	}
	
	/**
	 * Fills this scene with a new world and camera.
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
	public void init() throws IllegalStateException {
		if (initialized) {
			throw new IllegalStateException("Scene has already been initialized");
		}
		
		load();
		logger.log("Loaded scene");
		
		world.init();
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
		
		if (getInput().getKeyDown(Key.F12)) {
			toggleDevConsole();
		}
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
	 * Destroys this scene.
	 */
	@Override
	public void destroy() throws IllegalStateException {
		checkStatus();
		world.destroy();
		
		// Remove all references
		world = null;
		cameraData = null;
		devConsoleRoot = null;
		devConsole = null;
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
		return scene != null && scene.id == id;
	}
	
	/**
	 * Checks whether this scene is the application's current scene.
	 */
	public boolean isActive() {
		return application.getCurrentScene().equals(this);
	}
	
	@Override
	public Application getApplication() {
		return application;
	}
	
	@Override
	public World getWorld() {
		return world;
	}
	
	@Override
	public CameraData getCameraData() {
		return cameraData;
	}
	
	@Override
	public @NotNull String toString() {
		return String.format("%s (%s)", name, id);
	}

	public void toggleDevConsole() {
		toggleDevConsole(devConsole == null || !devConsole.isActive());
	}
	
	public void toggleDevConsole(boolean active) {
		if (!Application.isDevMode()) {
			return;
		}
		
		if (active) {
			if (devConsole == null) {
				world.addSystem(new DevConsoleInputHandler());
				
				if (devConsoleRoot == null) {
					devConsoleRoot = world.addEntity(new NodeRootPrefab()).getComponent(NodeRoot.class);
				}
				
				devConsoleRoot.getEntity().addListener(EntityEventType.DESCENDANT_ADD, (event) -> {
					logger.logCollection(event.getTarget().transform.children);
				});
				
				devConsole = devConsoleRoot.getEntity().addChild(devConsolePrefab);
			}
			devConsole.setActive(true);
		} else if (devConsole != null) {
			devConsole.setActive(false);
		}
	}
	
}
