package dev.prozilla.pine.core.scene;

import dev.prozilla.pine.common.Printable;
import dev.prozilla.pine.common.lifecycle.*;
import dev.prozilla.pine.common.logging.Logger;
import dev.prozilla.pine.common.util.checks.Checks;
import dev.prozilla.pine.core.Application;
import dev.prozilla.pine.core.ApplicationProvider;
import dev.prozilla.pine.core.component.Component;
import dev.prozilla.pine.core.component.ComponentManager;
import dev.prozilla.pine.core.component.Transform;
import dev.prozilla.pine.core.component.camera.CameraData;
import dev.prozilla.pine.core.component.camera.OverlayCameraData;
import dev.prozilla.pine.core.component.ui.NodeRoot;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.EntityManager;
import dev.prozilla.pine.core.entity.EntityQueryPool;
import dev.prozilla.pine.core.entity.prefab.Prefab;
import dev.prozilla.pine.core.entity.prefab.camera.CameraPrefab;
import dev.prozilla.pine.core.entity.prefab.ui.LayoutPrefab;
import dev.prozilla.pine.core.entity.prefab.ui.NodeRootPrefab;
import dev.prozilla.pine.core.entity.prefab.ui.dev.DevConsolePrefab;
import dev.prozilla.pine.core.rendering.Renderer;
import dev.prozilla.pine.core.state.input.Key;
import dev.prozilla.pine.core.system.SystemBase;
import dev.prozilla.pine.core.system.SystemBuilder;
import dev.prozilla.pine.core.system.SystemManager;
import dev.prozilla.pine.core.system.standard.animation.AnimationInitializer;
import dev.prozilla.pine.core.system.standard.animation.AnimationUpdater;
import dev.prozilla.pine.core.system.standard.audio.AudioPlayerInitializer;
import dev.prozilla.pine.core.system.standard.camera.*;
import dev.prozilla.pine.core.system.standard.driver.TransformDriverUpdater;
import dev.prozilla.pine.core.system.standard.layer.RenderLayerInitializer;
import dev.prozilla.pine.core.system.standard.layer.RenderLayerUpdater;
import dev.prozilla.pine.core.system.standard.mesh.MeshRenderSystem;
import dev.prozilla.pine.core.system.standard.mesh.QuadRenderSystem;
import dev.prozilla.pine.core.system.standard.particle.ParticleFlowUpdater;
import dev.prozilla.pine.core.system.standard.particle.ParticleInitializer;
import dev.prozilla.pine.core.system.standard.particle.ParticleUpdater;
import dev.prozilla.pine.core.system.standard.sprite.GridInitializer;
import dev.prozilla.pine.core.system.standard.sprite.GridInputHandler;
import dev.prozilla.pine.core.system.standard.sprite.MultiTileInitializer;
import dev.prozilla.pine.core.system.standard.sprite.TileMover;
import dev.prozilla.pine.core.system.standard.ui.*;
import dev.prozilla.pine.core.system.standard.ui.dev.DevConsoleInputHandler;
import dev.prozilla.pine.core.system.standard.ui.frame.FrameRenderer;
import dev.prozilla.pine.core.system.standard.ui.frame.FrameResizer;
import dev.prozilla.pine.core.system.standard.ui.image.ImageInitializer;
import dev.prozilla.pine.core.system.standard.ui.image.ImageRenderer;
import dev.prozilla.pine.core.system.standard.ui.layout.*;
import dev.prozilla.pine.core.system.standard.ui.text.*;
import dev.prozilla.pine.core.system.standard.ui.tooltip.TooltipInitializer;
import dev.prozilla.pine.core.system.standard.ui.tooltip.TooltipInputHandler;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a collection of entities, components and systems.
 */
public class Scene implements Initializable, InputHandler, Updatable, Renderable, Destructible, Printable, SceneContext, ApplicationProvider {
	
	// Scene properties
	public String name;
	private final int id;
	
	// References
	protected Application application;
	protected Logger logger;
	protected CameraData cameraData;
	protected OverlayCameraData overlayCameraData;
	/**
	 * Prefab that will be used during scene loading to create a camera entity.
	 */
	protected Prefab cameraPrefab;
	protected RenderLayerUpdater renderLayerUpdater;
	
	// Developer console
	protected LayoutPrefab devConsolePrefab;
	protected Entity devConsole;
	protected NodeRoot devConsoleRoot;
	
	// Scene state
	public boolean loaded;
	public boolean initialized;
	
	// ECS
	protected final EntityManager entityManager;
	protected final ComponentManager componentManager;
	protected final SystemManager systemManager;
	protected final EntityQueryPool queryPool;
	
	/**
	 * List of all systems that are added during initialization.
	 * Systems of the same type are executed in the order in which they appear in this list.
	 */
	private final List<SystemBase> initialSystems;
	
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
	 *
	 * @param name Name of the scene
	 */
	public Scene(String name) {
		this.id = generateId();
		this.name = (name != null) ? name : "Scene #" + this.id;
		
		entityManager = new EntityManager(this);
		componentManager = new ComponentManager(this);
		systemManager = new SystemManager(this);
		
		queryPool = new EntityQueryPool();
		
		initialSystems = new ArrayList<>();
		useStandardSystems();
		
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
	 * Loads the entities, components and systems of this scene.
	 */
	protected void load() {
		load(null);
	}
	
	/**
	 * Loads the entities, components and systems of this scene.
	 *
	 * @param cameraPrefab Prefab for the camera entity.
	 */
	protected void load(Prefab cameraPrefab) throws IllegalStateException {
		initSystems();
		
		// Prepare camera prefab
		if (cameraPrefab == null) {
			cameraPrefab = new CameraPrefab();
		}
		this.cameraPrefab = cameraPrefab;
		
		// Create new camera from prefab
		if (cameraData == null) {
			Entity camera = addEntity(this.cameraPrefab, 0, 10f, 10f);
			cameraData = camera.getComponent(CameraData.class);
			
			if (cameraData == null) {
				throw new IllegalStateException("Camera prefab is missing a CameraData component.");
			}
		}
		
		if (overlayCameraData == null) {
			overlayCameraData = new OverlayCameraData();
		}
		
		loaded = true;
	}
	
	/**
	 * Initializes all systems in this scene.
	 */
	public void initSystems() {
		systemManager.initSystems(initialSystems);
	}
	
	/**
	 * Adds all standard systems to the list of initial systems.
	 */
	public void useStandardSystems() {
		if (systemManager.isInitialized()) {
			throw new IllegalStateException("Initial systems must be specified before the initialization of the system manager.");
		}
		
		// Rendering
		initialSystems.add(new SceneCameraRenderSystem());
		initialSystems.add(new OverlayCameraRenderSystem());
		
		// Z-index
		renderLayerUpdater = new RenderLayerUpdater();
		initialSystems.add(renderLayerUpdater);
		initialSystems.add(new RenderLayerInitializer());
		
		// Animations
		initialSystems.add(new AnimationInitializer());
		initialSystems.add(new AnimationUpdater());
		initialSystems.add(new TransformDriverUpdater());
		
		initialSystems.add(new NodeStyler());
		initialSystems.add(new LayoutNodeStyler());
		
		// Camera
		initialSystems.add(new CameraInitializer());
		initialSystems.add(new CameraControlInitializer());
		
		initialSystems.add(new CameraControlInputHandler());
		
		initialSystems.add(new CameraResizer());
		initialSystems.add(new CameraControlUpdater());
		
		// Particles
		initialSystems.add(new ParticleInitializer());
		initialSystems.add(new ParticleFlowUpdater());
		initialSystems.add(new ParticleUpdater());
		
		// Sprites
		initialSystems.add(new GridInitializer());
		initialSystems.add(new MultiTileInitializer());
		initialSystems.add(new TileMover());
		
		// Meshes
		initialSystems.add(new MeshRenderSystem());
		initialSystems.add(new QuadRenderSystem());
		
		// Nodes
		initialSystems.add(new NodeRootInitializer());
		initialSystems.add(new TooltipInitializer());
		initialSystems.add(new NodeInitializer());
		initialSystems.add(new LayoutNodeInitializer());
		initialSystems.add(new TextInitializer());
		initialSystems.add(new ImageInitializer());
		initialSystems.add(new TextInputInitializer());
		
		initialSystems.add(new NodeRootInputHandler());
		initialSystems.add(new LayoutNodeInputHandler());
		initialSystems.add(new NodeInputHandler());
		initialSystems.add(new TooltipInputHandler());
		initialSystems.add(new ButtonInputHandler());
		initialSystems.add(new TextInputInputHandler());
		
		initialSystems.add(new DynamicTextUpdater());
		initialSystems.add(new NodeRootResizer());
		initialSystems.add(new TextResizer());
		initialSystems.add(new FrameResizer());
		initialSystems.add(new LayoutNodeResizer());
		initialSystems.add(new LayoutNodeArranger());
		initialSystems.add(new NodeUpdater());
		
		initialSystems.add(new NodeRootRenderer());
		initialSystems.add(new NodeRenderer());
		initialSystems.add(new TextRenderer());
		initialSystems.add(new ImageRenderer());
		initialSystems.add(new FrameRenderer());
		initialSystems.add(new BorderImageRenderer());
		initialSystems.add(new TextInputRenderer());
		
		// Audio
		initialSystems.add(new AudioPlayerInitializer());
		
		// Sprite input
		initialSystems.add(new GridInputHandler());
	}
	
	/**
	 * Adds a system to the list of initial systems that will be added when this scene is initialized.
	 */
	private void useSystem(SystemBase system) {
		Checks.isNotNull(system, "system");
		
		if (systemManager.isInitialized()) {
			throw new IllegalStateException("Initial systems must be specified before the initialization of the system manager.");
		}
		
		initialSystems.add(system);
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
		
		updateZIndices();
		systemManager.init();
		initialized = true;
	}
	
	/**
	 * Handles input for the scene.
	 *
	 * @param deltaTime Delta time in seconds
	 */
	@Override
	public void input(float deltaTime) throws IllegalStateException {
		checkStatus();
		systemManager.input(deltaTime);
		
		if (getInput().getKeyDown(Key.F12)) {
			toggleDevConsole();
		}
	}
	
	/**
	 * Updates the scene every frame.
	 *
	 * @param deltaTime Delta time in seconds
	 */
	@Override
	public void update(float deltaTime) throws IllegalStateException {
		checkStatus();
		systemManager.update(deltaTime);
	}
	
	/**
	 * Renders the scene every frame.
	 */
	@Override
	public void render(Renderer renderer) throws IllegalStateException {
		checkStatus();
		systemManager.render(renderer);
	}
	
	/**
	 * Destroys this scene.
	 */
	@Override
	public void destroy() throws IllegalStateException {
		checkStatus();
		entityManager.destroy();
		componentManager.destroy();
		systemManager.destroy();
		queryPool.destroy();
		application.getTracker().reset();
		
		// Remove all references
		cameraData = null;
		renderLayerUpdater = null;
		devConsoleRoot = null;
		devConsole = null;
		
		reset();
	}
	
	/**
	 * Instantiates a prefab into this scene at (0, 0, 0).
	 *
	 * @param prefab The prefab to instantiate
	 * @return The instantiated entity
	 */
	public Entity addEntity(Prefab prefab) {
		Checks.isNotNull(prefab, "prefab");
		return addEntity(prefab.instantiate(this));
	}
	
	/**
	 * Instantiates a prefab into this scene.
	 *
	 * @param prefab The prefab to instantiate
	 * @param x X position
	 * @param y Y position
	 * @param z Z position
	 * @return The instantiated entity
	 */
	public Entity addEntity(Prefab prefab, float x, float y, float z) {
		Checks.isNotNull(prefab, "prefab");
		return addEntity(prefab.instantiate(this, x, y, z));
	}
	
	/**
	 * Adds an entity into this scene.
	 *
	 * @param entity The entity to add
	 * @return The added entity
	 */
	// TO DO: Refactor component loading so components are always added after entity without explicit checks
	public Entity addEntity(Entity entity) {
		Checks.isNotNull(entity, "entity");
		if (entityManager.contains(entity)) {
			systemManager.register(entity); // Check if entity was changed since it was added (e.g. tag changed after components added)
			updateZIndices();
			return entity;
		}
		entityManager.addEntity(entity);
		updateZIndices();
		systemManager.register(entity);
		return entity;
	}
	
	public void removeEntity(Entity entity) {
		Checks.isNotNull(entity, "entity");
		entityManager.removeEntity(entity);
		updateZIndices();
		systemManager.unregister(entity);
		componentManager.removeComponents(entity);
	}
	
	public void activateEntity(Entity entity) {
		Checks.isNotNull(entity, "entity");
		systemManager.activateEntity(entity);
		for (Transform child : entity.transform.children) {
			activateEntity(child.getEntity());
		}
	}
	
	/**
	 * Adds a component to an entity in this scene.
	 *
	 * @param entity The entity
	 * @param component The component to add to the entity
	 * @return The added component
	 */
	public Component addComponent(Entity entity, Component component) {
		Checks.isNotNull(entity, "entity");
		Checks.isNotNull(component, "component");
		
		if (!entityManager.contains(entity)) {
			entityManager.addEntity(entity);
		}
		componentManager.addComponent(entity, component);
		systemManager.register(entity);
		return component;
	}
	
	/**
	 * Removes a component from an entity in this scene.
	 *
	 * @param entity The entity
	 * @param component The component to remove from the entity
	 */
	public void removeComponent(Entity entity, Component component) {
		Checks.isNotNull(entity, "entity");
		Checks.isNotNull(component, "component");
		
		componentManager.removeComponent(entity, component);
		systemManager.register(entity);
	}
	
	/**
	 * Builds a system and adds it to this scene.
	 *
	 * @param systemBuilder Builder of the system
	 * @param <B> Type of the system builder
	 * @return System that was built and added
	 */
	public <S extends SystemBase, B extends SystemBuilder<S, B>> S addSystem(B systemBuilder) {
		Checks.isNotNull(systemBuilder, "systemBuilder");
		return addSystem(systemBuilder.build());
	}
	
	/**
	 * Adds a system to this scene.
	 *
	 * @param system The system to add
	 * @return The added system
	 */
	public <S extends SystemBase> S addSystem(S system) {
		Checks.isNotNull(system, "system");
		
		if (!systemManager.isInitialized()) {
			useSystem(system);
		} else {
			systemManager.addSystem(system);
		}
		
		return system;
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
	 *
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
	 *
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
	public EntityManager getEntityManager() {
		return entityManager;
	}
	
	@Override
	public ComponentManager getComponentManager() {
		return componentManager;
	}
	
	@Override
	public SystemManager getSystemManager() {
		return systemManager;
	}
	
	@Override
	public EntityQueryPool getQueryPool() {
		return queryPool;
	}
	
	@Override
	public CameraData getCameraData() {
		return cameraData;
	}
	
	@Override
	public OverlayCameraData getOverlayCameraData() {
		return overlayCameraData;
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
				addSystem(new DevConsoleInputHandler());
				
				if (devConsoleRoot == null) {
					devConsoleRoot = addEntity(new NodeRootPrefab()).getComponent(NodeRoot.class);
				}
				
				devConsole = devConsoleRoot.getEntity().addChild(devConsolePrefab);
			}
			devConsole.setActive(true);
		} else if (devConsole != null) {
			devConsole.setActive(false);
		}
	}
	
	public void updateZIndices() {
		if (renderLayerUpdater != null) {
			renderLayerUpdater.updateZIndices();
		}
	}
}
