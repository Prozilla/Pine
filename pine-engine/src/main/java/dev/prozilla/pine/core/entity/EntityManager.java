package dev.prozilla.pine.core.entity;

import dev.prozilla.pine.core.ECSManager;
import dev.prozilla.pine.core.scene.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Manages entities in the world.
 */
public class EntityManager extends ECSManager {
	
	/** List of all entities */
	private final List<Entity> entities;
	
	private static int lastEntityId = 0;
	
	public EntityManager(World world) {
		super(world);
		
		entities = new ArrayList<>();
	}
	
	/**
	 * Destroys all entities.
	 */
	@Override
	public void destroy() {
		for (Entity entity : entities) {
			entity.destroy();
		}
		
		entities.clear();
	}
	
	/**
	 * Registers an entity.
	 * @param entity Entity to register
	 * @throws IllegalStateException If the entity is already registered.
	 */
	public void addEntity(Entity entity) throws IllegalStateException {
		Objects.requireNonNull(entity, "Entity must not be null.");
		
		if (entities.contains(entity)) {
			throw new IllegalStateException("Entity has already been added.");
		}
		
		entities.add(entity);
		
		getTracker().addEntity();
	}
	
	/**
	 * Unregisters an entity.
	 * @param entity Entity to unregister.
	 * @throws IllegalStateException If the entity has not been registered yet.
	 */
	public void removeEntity(Entity entity) throws IllegalStateException {
		Objects.requireNonNull(entity, "Entity must not be null.");
		
		if (!entities.contains(entity)) {
			throw new IllegalStateException("Entity has not been added yet.");
		}
		
		entities.remove(entity);
		
		getTracker().removeEntity();
	}
	
	/**
	 * Checks whether a given entity has been registered.
	 */
	public boolean contains(Entity entity) {
		return entities.contains(entity);
	}
	
	/**
	 * @return List of all entities.
	 */
	public List<Entity> getEntities() {
		return entities;
	}
	
	/**
	 * Checks if there are any entities.
	 */
	public boolean hasEntities() {
		return !entities.isEmpty();
	}
	
	/**
	 * Generates a new unique entity ID.
	 * @return Entity ID
	 */
	public static int generateEntityId() {
		return lastEntityId++;
	}
}
