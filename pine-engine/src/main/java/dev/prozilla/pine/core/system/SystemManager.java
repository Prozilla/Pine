package dev.prozilla.pine.core.system;

import dev.prozilla.pine.core.ECSManager;
import dev.prozilla.pine.core.component.Component;
import dev.prozilla.pine.core.World;
import dev.prozilla.pine.core.rendering.Renderer;

import java.util.ArrayList;
import java.util.Objects;

public class SystemManager extends ECSManager {
	
	private final ArrayList<InputSystem<Component>> inputSystems;
	private final ArrayList<UpdateSystem<Component>> updateSystems;
	private final ArrayList<RenderSystem<Component>> renderSystems;
	
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
		
		for (InputSystem<Component> inputSystem : inputSystems) {
			if (inputSystem.hasComponents()) {
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
		
		for (UpdateSystem<Component> updateSystem : updateSystems) {
			if (updateSystem.hasComponents()) {
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
		
		for (RenderSystem<Component> renderSystem : renderSystems) {
			if (renderSystem.hasComponents()) {
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
	 * Registers a component to all systems.
	 */
	public void registerComponent(Component component) {
		Objects.requireNonNull(component, "System must not be null.");
		
		for (InputSystem<Component> inputSystem : inputSystems) {
			inputSystem.registerComponent(component);
		}
		for (UpdateSystem<Component> updateSystem : updateSystems) {
			updateSystem.registerComponent(component);
		}
		for (RenderSystem<Component> renderSystem : renderSystems) {
			renderSystem.registerComponent(component);
		}
	}
	
	public <C extends Component, S extends SystemBase<C>> void addSystem(S system) {
		Objects.requireNonNull(system, "System must not be null.");
		
		boolean added = true;
		if (system instanceof InputSystem) {
			inputSystems.add((InputSystem<Component>)system);
		} else if (system instanceof UpdateSystem) {
			updateSystems.add((UpdateSystem<Component>)system);
		} else if (system instanceof RenderSystem) {
			renderSystems.add((RenderSystem<Component>)system);
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
