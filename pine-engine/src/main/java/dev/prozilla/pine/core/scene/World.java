package dev.prozilla.pine.core.scene;

import dev.prozilla.pine.common.lifecycle.*;
import dev.prozilla.pine.common.util.checks.Checks;
import dev.prozilla.pine.core.Application;
import dev.prozilla.pine.core.component.Component;
import dev.prozilla.pine.core.component.ComponentManager;
import dev.prozilla.pine.core.component.Transform;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.EntityManager;
import dev.prozilla.pine.core.entity.EntityQueryPool;
import dev.prozilla.pine.core.entity.prefab.Prefab;
import dev.prozilla.pine.core.rendering.Renderer;
import dev.prozilla.pine.core.system.SystemBase;
import dev.prozilla.pine.core.system.SystemBuilder;
import dev.prozilla.pine.core.system.SystemManager;
import dev.prozilla.pine.core.system.standard.animation.AnimationInitializer;
import dev.prozilla.pine.core.system.standard.animation.AnimationUpdater;
import dev.prozilla.pine.core.system.standard.audio.AudioPlayerInitializer;
import dev.prozilla.pine.core.system.standard.camera.*;
import dev.prozilla.pine.core.system.standard.particle.ParticleFlowUpdater;
import dev.prozilla.pine.core.system.standard.particle.ParticleInitializer;
import dev.prozilla.pine.core.system.standard.particle.ParticleUpdater;
import dev.prozilla.pine.core.system.standard.shape.RectRenderSystem;
import dev.prozilla.pine.core.system.standard.sprite.*;
import dev.prozilla.pine.core.system.standard.ui.*;
import dev.prozilla.pine.core.system.standard.ui.frame.FrameRenderer;
import dev.prozilla.pine.core.system.standard.ui.frame.FrameResizer;
import dev.prozilla.pine.core.system.standard.ui.image.ImageInitializer;
import dev.prozilla.pine.core.system.standard.ui.image.ImageRenderer;
import dev.prozilla.pine.core.system.standard.ui.layout.*;
import dev.prozilla.pine.core.system.standard.ui.text.*;
import dev.prozilla.pine.core.system.standard.ui.tooltip.TooltipInitializer;
import dev.prozilla.pine.core.system.standard.ui.tooltip.TooltipInputHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * An isolated collection of entities, components and systems that live inside a scene.
 */
public class World implements Initializable, InputHandler, Updatable, Renderable, Destructible {
	
	// ECS managers
	public final EntityManager entityManager;
	public final ComponentManager componentManager;
	public final SystemManager systemManager;
	
	public final EntityQueryPool queryPool;
	
	public final Application application;
	public final Scene scene;
	
	public boolean initialized;
	
	public int maxDepth;
	
	/**
	 * List of all systems that are added during initialization.
	 * Systems of the same type are executed in the order in which they appear in this list.
	 */
	private final List<SystemBase> initialSystems;
	
	public World(Application application, Scene scene) {
		this.application = Checks.isNotNull(application, "application");
		this.scene = Checks.isNotNull(scene, "scene");
		
		entityManager = new EntityManager(this);
		componentManager = new ComponentManager(this);
		systemManager = new SystemManager(this);
		
		queryPool = new EntityQueryPool();
		
		initialSystems = new ArrayList<>();
		useStandardSystems();
		
		initialized = false;
	}
	
	/**
	 * Initializes all systems in this world.
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
		
		// Animations
		initialSystems.add(new AnimationInitializer());
		initialSystems.add(new AnimationUpdater());
		
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
		initialSystems.add(new SpriteRenderSystem());
		
		// Shapes
		initialSystems.add(new RectRenderSystem());

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
	 * Adds a system to the list of initial systems that will be added when this world is initialized.
	 */
	private void useSystem(SystemBase system) {
		Checks.isNotNull(system, "system");
		
		if (systemManager.isInitialized()) {
			throw new IllegalStateException("Initial systems must be specified before the initialization of the system manager.");
		}
		
		initialSystems.add(system);
	}
	
	/**
	 * Executes all initialization systems in this world.
	 */
	@Override
	public void init() throws IllegalStateException {
		if (initialized) {
			throw new IllegalStateException("World has already been initialized.");
		}
		
		calculateDepth();
		systemManager.init();
		initialized = true;
	}
	
	/**
	 * Executes all input systems in this world.
	 */
	@Override
	public void input(float deltaTime) {
		systemManager.input(deltaTime);
	}
	
	/**
	 * Executes all update systems in this world.
	 */
	@Override
	public void update(float deltaTime) {
		systemManager.update(deltaTime);
	}
	
	/**
	 * Executes all render systems in this world.
	 */
	@Override
	public void render(Renderer renderer) {
		systemManager.render(renderer);
	}
	
	@Override
	public void destroy() {
		entityManager.destroy();
		componentManager.destroy();
		systemManager.destroy();
		queryPool.destroy();
		application.getTracker().reset();
	}
	
	/**
	 * Instantiates a prefab into this world at (0, 0).
	 * @param prefab The prefab to instantiate
	 * @return The instantiated entity
	 */
	public Entity addEntity(Prefab prefab) {
		Checks.isNotNull(prefab, "prefab");
		return addEntity(prefab.instantiate(this));
	}
	
	/**
	 * Instantiates a prefab into this world.
	 * @param prefab The prefab to instantiate
	 * @param x X position
	 * @param y Y position
	 * @return The instantiated entity
	 */
	public Entity addEntity(Prefab prefab, float x, float y) {
		Checks.isNotNull(prefab, "prefab");
		return addEntity(prefab.instantiate(this, x, y));
	}
	
	/**
	 * Adds an entity into this world.
	 * @param entity The entity to add
	 * @return The added entity
	 */
	// TO DO: Refactor component loading so components are always added after entity without explicit checks
	public Entity addEntity(Entity entity) {
		Checks.isNotNull(entity, "entity");
		if (entityManager.contains(entity)) {
			systemManager.register(entity); // Check if entity was changed since it was added (e.g. tag changed after components added)
			if (initialized) {
				calculateDepth();
			}
			return entity;
		}
		entityManager.addEntity(entity);
		if (initialized) {
			calculateDepth();
		}
		systemManager.register(entity);
		return entity;
	}
	
	public void removeEntity(Entity entity) {
		Checks.isNotNull(entity, "entity");
		entityManager.removeEntity(entity);
		if (initialized) {
			calculateDepth();
		}
		systemManager.unregister(entity);
		componentManager.removeComponents(entity);
	}
	
	public void activateEntity(Entity entity) {
		Checks.isNotNull(entity, "entity");
		systemManager.activateEntity(entity);
	}
	
	/**
	 * Adds a component to an entity in this world.
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
	 * Removes a component from an entity in this world.
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
	 * Builds a system and adds it to this world.
	 * @param systemBuilder Builder of the system
	 * @return System that was built and added
	 * @param <S> Type of the system builder
	 */
	public <S extends SystemBuilder<? extends SystemBase, S>> SystemBase addSystem(S systemBuilder) {
		Checks.isNotNull(systemBuilder, "systemBuilder");
		return addSystem(systemBuilder.build());
	}
	
	/**
	 * Adds a system to this world.
	 * @param system The system to add
	 * @return The added system
	 */
	public SystemBase addSystem(SystemBase system) {
		Checks.isNotNull(system, "system");
		
		if (!systemManager.isInitialized()) {
			useSystem(system);
		} else {
			systemManager.addSystem(system);
		}
		
		return system;
	}
	
	public void calculateDepth() {
		if (initialized && !application.getConfig().enableDepthRecalculation.getValue()) {
			return;
		}
		
		ArrayList<Transform> rootParents = new ArrayList<>();
		
		// Get root parent transforms
		for (Entity entity : entityManager.getEntities()) {
			if (entity.transform.parent == null) {
				rootParents.add(entity.transform);
			}
		}
		
		// Calculate depth for each root parent
		int depth = 0;
		for (Transform rootParent : rootParents) {
			depth = rootParent.calculateDepth(depth);
		}
		maxDepth = depth;
		
		// Check if depth indexes are unique
//		List<Integer> indexes = new ArrayList<>();
//		boolean unique = true;
//		for (Entity entity : entityManager.getEntities()) {
//			int depthIndex = entity.transform.getDepthIndex();
//			if (indexes.contains(depthIndex)) {
//				unique = false;
//				System.err.println("Duplicate depth index found: " + depthIndex);
//			} else {
//				indexes.add(depthIndex);
//			}
//		}
//		if (unique) {
//			System.out.println("Generated new unique depth indexes successfully");
//		}
		
		// Update systems that use depth
		systemManager.updateEntityDepth();
	}
	
	public boolean isActive() {
		return scene.isActive();
	}
}
