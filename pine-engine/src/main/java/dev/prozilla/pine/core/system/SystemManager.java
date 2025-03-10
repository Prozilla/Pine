package dev.prozilla.pine.core.system;

import dev.prozilla.pine.core.ECSManager;
import dev.prozilla.pine.core.scene.World;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.rendering.Renderer;
import dev.prozilla.pine.core.system.init.InitSystemBase;
import dev.prozilla.pine.core.system.input.InputSystemBase;
import dev.prozilla.pine.core.system.render.RenderSystemBase;
import dev.prozilla.pine.core.system.update.UpdateSystemBase;

import java.util.Collection;
import java.util.Objects;

public class SystemManager extends ECSManager {
	
	private final SystemGroup<InitSystemBase> initSystems;
	private final SystemGroup<InputSystemBase> inputSystems;
	private final SystemGroup<UpdateSystemBase> updateSystems;
	private final SystemGroup<RenderSystemBase> renderSystems;
	
	private final SystemGroup<? extends SystemBase>[] systemGroups;
	
	private boolean initialized;
	
	@SuppressWarnings("unchecked")
	public SystemManager(World world) {
		super(world);
		
		initialized = false;
		
		initSystems = new SystemGroup<>(world, InitSystemBase.class);
		inputSystems = new SystemGroup<>(world, InputSystemBase.class);
		updateSystems = new SystemGroup<>(world, UpdateSystemBase.class);
		renderSystems = new SystemGroup<>(world, RenderSystemBase.class);
		
		systemGroups = new SystemGroup[]{
			initSystems,
			inputSystems,
			updateSystems,
			renderSystems
		};
	}
	
	public void initSystems(Collection<SystemBase> initialSystems) {
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
		initSystems.forEach(InitSystemBase::init);
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
	
	/**
	 * Unregisters an entity from all systems.
	 */
	public void unregister(Entity entity) {
		Objects.requireNonNull(entity, "Entity must not be null.");
		
		for (SystemGroup<? extends SystemBase> systemGroup : systemGroups) {
			systemGroup.unregister(entity);
		}
	}
	
	public void activateEntity(Entity entity) {
		Objects.requireNonNull(entity, "Entity must not be null.");
		
		initSystems.forEach((initSystem) -> {
			initSystem.activateEntity(entity);
		});
	}
	
	public boolean addSystem(SystemBase system) {
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
			getTracker().addSystem();
		} else {
			system.destroy();
		}
		
		return added;
	}
	
	/**
	 * Updates all systems that depend on entity depth.
	 */
	public void updateEntityDepth() {
		renderSystems.forEach(RenderSystemBase::sort);
		inputSystems.forEach(InputSystemBase::sort);
	}
	
	public boolean isInitialized() {
		return initialized;
	}
}
