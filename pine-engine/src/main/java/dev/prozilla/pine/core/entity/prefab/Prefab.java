package dev.prozilla.pine.core.entity.prefab;

import dev.prozilla.pine.core.World;
import dev.prozilla.pine.core.entity.Entity;

/**
 * Abstract class representing a prefab for creating entities with predefined components and values.
 */
public abstract class Prefab {
	
	private String name;
	private String tag;
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setTag(String tag) {
		this.tag = tag;
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
		
		apply(entity);
		return entity;
	}
	
	/**
	 * Adds this prefab's predefined components to a given entity and copies values from this prefab.
	 */
	protected abstract void apply(Entity entity);
}