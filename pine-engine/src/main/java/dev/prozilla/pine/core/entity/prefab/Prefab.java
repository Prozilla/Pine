package dev.prozilla.pine.core.entity.prefab;

import dev.prozilla.pine.common.util.checks.Checks;
import dev.prozilla.pine.core.component.Transform;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.scene.World;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Abstract class representing a prefab for creating entities with predefined components and values.
 */
@Components({ Transform.class })
public abstract class Prefab {
	
	protected String name;
	protected String tag;
	protected boolean isActive = true;
	protected final List<Prefab> children;
	
	public Prefab() {
		children = new ArrayList<>();
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setTag(String tag) {
		this.tag = tag;
	}
	
	public void setActive(boolean active) {
		isActive = active;
	}
	
	public void addChildren(Prefab... children) {
		addChildren(List.of(children));
	}
	
	public void addChildren(Collection<Prefab> children) {
		this.children.addAll(children);
	}
	
	public void addChild(Prefab child) {
		Checks.isNotNull(child, "child");
		children.add(child);
	}
	
	public void removeChild(Prefab child) {
		children.remove(child);
	}
	
	/**
	 * Creates a new entity instance with the prefab's default components at position (0, 0).
	 * @param world The world where the entity will be added.
	 * @return A new entity instance.
	 */
	public Entity instantiate(World world) {
		return instantiate(world, 0, 0);
	}
	
	/**
	 * Creates a new entity instance with the prefab's default components.
	 * @param world The world where the entity will be added.
	 * @param x X position
	 * @param y Y position
	 * @return A new entity instance.
	 */
	public Entity instantiate(World world, float x, float y) {
		Entity entity;
		if (name != null) {
			entity = new Entity(world, name, x, y);
		} else {
			entity = new Entity(world, x, y);
		}
		
		if (tag != null) {
			entity.tag = tag;
		}
		
		try {
			apply(entity);
		} catch (RuntimeException e) {
			String message = "Failed to instantiate prefab";
			if (name != null) {
				message += ": " + name;
			}
			
			world.application.getLogger().error(message, e);
		}
		
		if (!isActive) {
			entity.setActive(false);
		}
		
		for (Prefab child : children) {
			entity.addChild(child);
		}
		
		return entity;
	}
	
	/**
	 * Adds this prefab's predefined components to a given entity and copies values from this prefab.
	 */
	protected abstract void apply(Entity entity);
	
}
