package dev.prozilla.pine.core;

import dev.prozilla.pine.common.Lifecycle;
import dev.prozilla.pine.core.component.Component;
import dev.prozilla.pine.core.component.ComponentManager;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.EntityManager;
import dev.prozilla.pine.core.rendering.Renderer;
import dev.prozilla.pine.core.state.Scene;
import dev.prozilla.pine.core.system.SystemBase;
import dev.prozilla.pine.core.system.SystemManager;
import dev.prozilla.pine.core.system.init.CameraControlInitializer;
import dev.prozilla.pine.core.system.init.CameraInitializer;
import dev.prozilla.pine.core.system.input.CameraControlInputHandler;
import dev.prozilla.pine.core.system.render.SpriteRenderSystem;
import dev.prozilla.pine.core.system.update.CameraControlUpdater;
import dev.prozilla.pine.core.system.update.CameraUpdater;
import dev.prozilla.pine.core.system.update.TileUpdater;

import java.util.ArrayList;

public class World implements Lifecycle {
	
	public EntityManager entityManager;
	public ComponentManager componentManager;
	public SystemManager systemManager;
	
	public final Application application;
	public final Scene scene;
	
	private final ArrayList<SystemBase> initialSystems;
	
	public World(Application application, Scene scene) {
		this.application = application;
		this.scene = scene;
		
		entityManager = new EntityManager(this);
		componentManager = new ComponentManager(this);
		systemManager = new SystemManager(this);
		
		initialSystems = new ArrayList<>();
		useDefaultSystems();
	}
	
	public void initSystems() {
		systemManager.initSystems(initialSystems);
	}
	
	public void useDefaultSystems() {
		if (systemManager.isInitialized()) {
			throw new IllegalStateException("Initial systems must be specified before the initialization of the system manager.");
		}
		
		initialSystems.add(new SpriteRenderSystem());
		initialSystems.add(new TileUpdater());
		initialSystems.add(new CameraInitializer());
		initialSystems.add(new CameraUpdater());
		initialSystems.add(new CameraControlInitializer());
		initialSystems.add(new CameraControlInputHandler());
		initialSystems.add(new CameraControlUpdater());
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
	public void start() {
		systemManager.start();
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
