package dev.prozilla.pine.core.entity;

import dev.prozilla.pine.core.ECSManager;
import dev.prozilla.pine.core.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EntityManager extends ECSManager {
	
	private final List<Entity> entities;
	
	private static int lastEntityId = 0;
	
	public EntityManager(World world) {
		super(world);
		
		entities = new ArrayList<>();
	}
	
	@Override
	public void destroy() {
		for (Entity entity : entities) {
			entity.destroy();
		}
		
		entities.clear();
	}
	
	public void addEntity(Entity entity) throws NullPointerException, IllegalStateException {
		Objects.requireNonNull(entity, "Entity must not be null.");
		
		if (entities.contains(entity)) {
			throw new IllegalStateException("Entity has already been added.");
		}
		
		entities.add(entity);
		
		getTracker().addEntity();
	}
	
	public void removeEntity(Entity entity) {
		Objects.requireNonNull(entity, "Entity must not be null.");
		
		if (!entities.contains(entity)) {
			throw new IllegalStateException("Entity has not been added yet.");
		}
		
		entities.remove(entity);
		
		getTracker().removeEntity();
	}
	
	public boolean contains(Entity entity) {
		return entities.contains(entity);
	}
	
	public List<Entity> getEntities() {
		return entities;
	}
	
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
