package dev.prozilla.pine.core.system;

import dev.prozilla.pine.core.ECSManager;
import dev.prozilla.pine.core.World;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.rendering.Renderer;

import java.util.ArrayList;
import java.util.Objects;

public class SystemManager extends ECSManager {
	
	private final ArrayList<InputSystem> inputSystems;
	private final ArrayList<UpdateSystem> updateSystems;
	private final ArrayList<RenderSystem> renderSystems;
	
	public SystemManager(World world) {
		super(world);
		
		inputSystems = new ArrayList<>();
		updateSystems = new ArrayList<>();
		renderSystems = new ArrayList<>();
	}
	
	/**
	 * Runs all systems that handle input.
	 */
	@Override
	public void input(float deltaTime) {
		if (inputSystems.isEmpty()) {
			return;
		}
		
		for (InputSystem inputSystem : inputSystems) {
			if (inputSystem.hasComponentGroups()) {
				inputSystem.input(deltaTime);
			}
		}
	}
	
	/**
	 * Runs all systems that update component data.
	 */
	@Override
	public void update(float deltaTime) {
		if (updateSystems.isEmpty()) {
			return;
		}
		
		for (UpdateSystem updateSystem : updateSystems) {
			if (updateSystem.hasComponentGroups()) {
				updateSystem.update(deltaTime);
			}
		}
	}
	
	/**
	 * Runs all systems that render components.
	 */
	@Override
	public void render(Renderer renderer) {
		if (renderSystems.isEmpty()) {
			return;
		}
		
		for (RenderSystem renderSystem : renderSystems) {
			if (renderSystem.hasComponentGroups()) {
				renderSystem.render(renderer);
			}
		}
	}
	
	/**
	 * Destroys all systems.
	 */
	@Override
	public void destroy() {
		inputSystems.clear();
		updateSystems.clear();
		renderSystems.clear();
	}
	
	/**
	 * Registers an entity in all systems.
	 */
	public void register(Entity entity) {
		Objects.requireNonNull(entity, "Entity must not be null.");
		
		for (InputSystem inputSystem : inputSystems) {
			inputSystem.register(entity);
		}
		for (UpdateSystem updateSystem : updateSystems) {
			updateSystem.register(entity);
		}
		for (RenderSystem renderSystem : renderSystems) {
			renderSystem.register(entity);
		}
	}
	
	public void addSystem(SystemBase system) {
		Objects.requireNonNull(system, "System must not be null.");
		
		boolean added = true;
		if (system instanceof InputSystem) {
			inputSystems.add((InputSystem)system);
		} else if (system instanceof UpdateSystem) {
			updateSystems.add((UpdateSystem)system);
		} else if (system instanceof RenderSystem) {
			renderSystems.add((RenderSystem)system);
		} else {
			added = false;
		}
		
		if (added) {
			system.init(world);
		} else {
			system.destroy();
		}
	}
}
