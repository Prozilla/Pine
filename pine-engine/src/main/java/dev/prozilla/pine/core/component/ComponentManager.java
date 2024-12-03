package dev.prozilla.pine.core.component;

import dev.prozilla.pine.core.ECSManager;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.World;

import java.util.ArrayList;
import java.util.Objects;

public class ComponentManager extends ECSManager {
	
	private final ArrayList<Component> components;

	public ComponentManager(World world) {
		super(world);
		
		components = new ArrayList<>();
	}
	
	@Override
	public void destroy() {
		components.clear();
	}
	
	public void addComponent(Entity entity, Component component) throws NullPointerException {
		Objects.requireNonNull(entity, "Entity must not be null.");
		Objects.requireNonNull(component, "Component must not be null.");
		
		components.add(component);
		component.entity = entity;
		entity.components.add(component);
		
		world.systemManager.registerComponent(component);
	}
}
