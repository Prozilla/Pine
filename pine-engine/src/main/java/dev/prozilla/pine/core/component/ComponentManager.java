package dev.prozilla.pine.core.component;

import dev.prozilla.pine.common.util.checks.Checks;
import dev.prozilla.pine.core.ECSManager;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.scene.World;

import java.util.ArrayList;
import java.util.List;

public class ComponentManager extends ECSManager {
	
	private final List<Component> components;
	
	private static int lastComponentId = 0;

	public ComponentManager(World world) {
		super(world);
		
		components = new ArrayList<>();
	}
	
	@Override
	public void destroy() {
		components.clear();
	}
	
	public void addComponent(Entity entity, Component component) throws NullPointerException, IllegalStateException {
		Checks.isNotNull(entity, "entity");
		Checks.isNotNull(component, "component");
		
		if (components.contains(component)) {
			throw new IllegalStateException("component has already been added to an entity");
		}
		if (!world.entityManager.contains(entity)) {
			throw new IllegalStateException("entity must be registered before attaching components");
		}
		
		components.add(component);
		component.entity = entity;
		entity.components.add(component);
		
		getTracker().addComponent();
	}
	
	public void removeComponents(Entity entity) {
		for (Component component : entity.components) {
			components.remove(component);
			component.entity = null;
			getTracker().removeComponent();
		}
		entity.components.clear();
	}
	
	public void removeComponent(Entity entity, Component component) {
		Checks.isNotNull(component, "component");
		
		if (!components.contains(component)) {
			throw new IllegalStateException("component has not been added to an entity yet");
		}
		if (!world.entityManager.contains(entity)) {
			throw new IllegalStateException("entity must be registered before removing components");
		}
		
		components.remove(component);
		component.entity = null;
		entity.components.remove(component);
		
		getTracker().removeComponent();
	}
	
	/**
	 * Generates a new unique component ID.
	 * @return Component ID
	 */
	public static int generateComponentId() {
		return lastComponentId++;
	}
}
