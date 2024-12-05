package dev.prozilla.pine.core.entity;

import dev.prozilla.pine.core.ECSManager;
import dev.prozilla.pine.core.World;

import java.util.ArrayList;
import java.util.Objects;

public class EntityManager extends ECSManager {
	
	private final ArrayList<Entity> entities;
	
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
	}
	
	public boolean contains(Entity entity) {
		return entities.contains(entity);
	}
	
	public ArrayList<Entity> getEntities() {
		return entities;
	}
	
	public boolean hasEntities() {
		return !entities.isEmpty();
	}
}
