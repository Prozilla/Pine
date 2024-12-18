package dev.prozilla.pine.core.component;

import dev.prozilla.pine.core.ECSManager;
import dev.prozilla.pine.core.World;
import dev.prozilla.pine.core.entity.Entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
		Objects.requireNonNull(entity, "Entity must not be null.");
		Objects.requireNonNull(component, "Component must not be null.");
		
		if (components.contains(component)) {
			throw new IllegalStateException("Component has already been added to an entity.");
		}
		if (!world.entityManager.contains(entity)) {
			throw new IllegalStateException("Entity must be registered before attaching components.");
		}
		
		components.add(component);
		component.entity = entity;
		entity.components.add(component);
		
		world.application.getTracker().addComponent();
	}
	
	public void removeComponents(Entity entity) {
		for (Component component : entity.components) {
			components.remove(component);
			component.entity = null;
			world.application.getTracker().removeComponent();
		}
		entity.components.clear();
	}
	
	public void removeComponent(Entity entity, Component component) {
		Objects.requireNonNull(component, "Component must not be null.");
		
		if (!components.contains(component)) {
			throw new IllegalStateException("Component has not been added to an entity yet.");
		}
		if (!world.entityManager.contains(entity)) {
			throw new IllegalStateException("Entity must be registered before removing components.");
		}
		
		components.remove(component);
		component.entity = null;
		entity.components.remove(component);
		
		world.application.getTracker().removeComponent();
	}
	
	/**
	 * Generates a new unique component ID.
	 * @return Component ID
	 */
	public static int generateComponentId() {
		return lastComponentId++;
	}
}
