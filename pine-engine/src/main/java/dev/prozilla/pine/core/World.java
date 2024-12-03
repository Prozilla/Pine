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
import dev.prozilla.pine.core.system.render.SpriteRenderSystem;
import dev.prozilla.pine.core.system.update.TileUpdateSystem;

public class World implements Lifecycle {
	
	public EntityManager entityManager;
	public ComponentManager componentManager;
	public SystemManager systemManager;
	
	public final Game game;
	public final Scene scene;
	
	public World(Game game, Scene scene) {
		this.game = game;
		this.scene = scene;
		
		entityManager = new EntityManager(this);
		componentManager = new ComponentManager(this);
		systemManager = new SystemManager(this);
	}
	
	@Override
	public void init(long window) {
		systemManager.addSystem(new SpriteRenderSystem());
		systemManager.addSystem(new TileUpdateSystem());
		
		for (Entity entity : entityManager.getEntities()) {
			entity.init(window);
		}
	}
	
	@Override
	public void start() {
		for (Entity entity : entityManager.getEntities()) {
			entity.start();
		}
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
	
	public Entity addEntity(Entity entity) {
		entityManager.addEntity(entity);
		return entity;
	}
	
	public Component addComponent(Entity entity, Component component) {
		componentManager.addComponent(entity, component);
		return component;
	}
	
	public SystemBase addSystem(SystemBase systemBase) {
		systemManager.addSystem(systemBase);
		return systemBase;
	}
}
