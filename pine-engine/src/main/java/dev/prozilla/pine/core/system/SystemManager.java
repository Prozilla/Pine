package dev.prozilla.pine.core.system;

import dev.prozilla.pine.core.ECSManager;
import dev.prozilla.pine.core.World;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.rendering.Renderer;

import java.util.ArrayList;
import java.util.Objects;

public class SystemManager extends ECSManager {
	
	private final SystemGroup<InitSystem> initSystems;
	private final SystemGroup<InputSystem> inputSystems;
	private final SystemGroup<UpdateSystem> updateSystems;
	private final SystemGroup<RenderSystem> renderSystems;
	
	private final SystemGroup<? extends SystemBase>[] systemGroups;
	
	private boolean initialized;
	
	@SuppressWarnings("unchecked")
	public SystemManager(World world) {
		super(world);
		
		initialized = false;
		
		initSystems = new SystemGroup<>(InitSystem.class);
		inputSystems = new SystemGroup<>(InputSystem.class);
		updateSystems = new SystemGroup<>(UpdateSystem.class);
		renderSystems = new SystemGroup<>(RenderSystem.class);
		
		systemGroups = new SystemGroup[]{
			initSystems,
			inputSystems,
			updateSystems,
			renderSystems
		};
	}
	
	public void initSystems(ArrayList<SystemBase> initialSystems) {
		for (SystemBase system : initialSystems) {
			addSystem(system);
		}
		
		initialized = true;
	}
	
	/**
	 * Runs all systems that handle initialization.
	 */
	@Override
	public void init(long window) {
		initSystems.forEach(initSystem -> initSystem.init(window));
	}
	
	/**
	 * Runs all systems that handle input.
	 */
	@Override
	public void input(float deltaTime) {
		inputSystems.forEach(inputSystem -> inputSystem.input(deltaTime));
	}
	
	/**
	 * Runs all systems that update component data.
	 */
	@Override
	public void update(float deltaTime) {
		updateSystems.forEach(updateSystem -> updateSystem.update(deltaTime));
	}
	
	/**
	 * Runs all systems that render components.
	 */
	@Override
	public void render(Renderer renderer) {
		renderSystems.forEach(renderSystem -> renderSystem.render(renderer));
	}
	
	/**
	 * Destroys all systems.
	 */
	@Override
	public void destroy() {
		for (SystemGroup<? extends SystemBase> systemGroup : systemGroups) {
			systemGroup.clear();
		}
	}
	
	/**
	 * Registers an entity in all systems.
	 */
	public void register(Entity entity) {
		Objects.requireNonNull(entity, "Entity must not be null.");
		
		for (SystemGroup<? extends SystemBase> systemGroup : systemGroups) {
			systemGroup.register(entity);
		}
	}
	
	public void addSystem(SystemBase system) {
		Objects.requireNonNull(system, "System must not be null.");
		
		boolean added = false;
		for (SystemGroup<? extends SystemBase> systemGroup : systemGroups) {
			if (systemGroup.add(system)) {
				added = true;
				break;
			}
		}
		
		if (added) {
			system.initSystem(world);
		} else {
			system.destroy();
		}
	}
	
	public boolean isInitialized() {
		return initialized;
	}
}
