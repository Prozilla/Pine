package dev.prozilla.pine.core;

import dev.prozilla.pine.common.Lifecycle;
import dev.prozilla.pine.core.component.Component;
import dev.prozilla.pine.core.component.ComponentManager;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.EntityManager;
import dev.prozilla.pine.core.entity.EntityQueryPool;
import dev.prozilla.pine.core.rendering.Renderer;
import dev.prozilla.pine.core.state.Scene;
import dev.prozilla.pine.core.system.SystemBase;
import dev.prozilla.pine.core.system.SystemManager;
import dev.prozilla.pine.core.system.camera.CameraControlInitializer;
import dev.prozilla.pine.core.system.camera.CameraInitializer;
import dev.prozilla.pine.core.system.camera.CameraControlInputHandler;
import dev.prozilla.pine.core.system.canvas.*;
import dev.prozilla.pine.core.system.sprite.SpriteRenderSystem;
import dev.prozilla.pine.core.system.camera.CameraControlUpdater;
import dev.prozilla.pine.core.system.camera.CameraResizer;
import dev.prozilla.pine.core.system.sprite.TileMover;

import java.util.ArrayList;

/**
 * An isolated collection of entities, components and systems that live inside a scene.
 */
public class World implements Lifecycle {
	
	public EntityManager entityManager;
	public ComponentManager componentManager;
	public SystemManager systemManager;
	
	public EntityQueryPool queryPool;
	
	public final Application application;
	public final Scene scene;
	
	/**
	 * List of all systems that are added during initialization.
	 * Systems of the same type are executed in the order in which they appear in this list.
	 */
	private final ArrayList<SystemBase> initialSystems;
	
	public World(Application application, Scene scene) {
		this.application = application;
		this.scene = scene;
		
		entityManager = new EntityManager(this);
		componentManager = new ComponentManager(this);
		systemManager = new SystemManager(this);
		
		queryPool = new EntityQueryPool();
		
		initialSystems = new ArrayList<>();
		useDefaultSystems();
	}
	
	public void initSystems() {
		systemManager.initSystems(initialSystems);
	}
	
	/**
	 * Prepares the default systems.
	 */
	public void useDefaultSystems() {
		if (systemManager.isInitialized()) {
			throw new IllegalStateException("Initial systems must be specified before the initialization of the system manager.");
		}
		
		// Camera
		initialSystems.add(new CameraInitializer());
		initialSystems.add(new CameraControlInitializer());
		
		initialSystems.add(new CameraControlInputHandler());
		
		initialSystems.add(new CameraResizer());
		initialSystems.add(new CameraControlUpdater());
		
		// Sprites
		initialSystems.add(new TileMover());
		
		initialSystems.add(new SpriteRenderSystem());

		// Canvas
		initialSystems.add(new CanvasGroupInitializer());
		initialSystems.add(new TextInitializer());
		
		initialSystems.add(new CanvasGroupInputHandler());
		initialSystems.add(new RectInputHandler());
		
		initialSystems.add(new CanvasResizer());
		initialSystems.add(new CanvasGroupResizer());
		initialSystems.add(new RectMover());
		initialSystems.add(new CanvasGroupArranger());

		initialSystems.add(new CanvasRenderSystem());
		initialSystems.add(new CanvasGroupRenderer());
		initialSystems.add(new RectRenderSystem());
		initialSystems.add(new TextRenderSystem());
	}
	
	private void useSystem(SystemBase system) {
		if (systemManager.isInitialized()) {
			throw new IllegalStateException("Initial systems must be specified before the initialization of the system manager.");
		}
		
		initialSystems.add(system);
	}
	
	@Override
	public void init(long window) {
		systemManager.init(window);
	}
	
	@Override
	public void input(float deltaTime) {
		systemManager.input(deltaTime);
	}
	
	@Override
	public void update(float deltaTime) {
		systemManager.update(deltaTime);
	}
	
	@Override
	public void render(Renderer renderer) {
		systemManager.render(renderer);
	}
	
	@Override
	public void destroy() {
		entityManager.destroy();
		componentManager.destroy();
		systemManager.destroy();
		queryPool.clear();
	}
	
	// TO DO: Refactor component loading so components are always added after entity without explicit checks
	public Entity addEntity(Entity entity) {
		if (entityManager.contains(entity)) {
			return entity;
		}
		entityManager.addEntity(entity);
		systemManager.register(entity);
		return entity;
	}
	
	public Component addComponent(Entity entity, Component component) {
		if (!entityManager.contains(entity)) {
			entityManager.addEntity(entity);
		}
		componentManager.addComponent(entity, component);
		systemManager.register(entity);
		return component;
	}
	
	public SystemBase addSystem(SystemBase system) {
		if (!systemManager.isInitialized()) {
			useSystem(system);
		} else {
			systemManager.addSystem(system);
		}
		
		return system;
	}
}
