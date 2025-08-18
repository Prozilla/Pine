package dev.prozilla.pine.core.entity;

import dev.prozilla.pine.common.lifecycle.Destructible;
import dev.prozilla.pine.common.logging.Logger;
import dev.prozilla.pine.common.util.DeferredList;
import dev.prozilla.pine.common.util.checks.Checks;
import dev.prozilla.pine.core.component.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Utility class for querying entities with specific components to be processed by a system.
 */
public class EntityQuery implements Destructible {
	
	/** List of entities that match this query. */
	public final DeferredList<EntityChunk> entityChunks;
	
	/** Map of entity IDs to their respective match. */
	private final Map<Integer, EntityChunk> entityChunkMap;
	
	// Primary query options
	/** Entities must have components of all these types to match this query. */
	private final Class<? extends Component>[] includedComponentTypes;
	/** Entities must not have any components of these types to match this query. */
	private final Class<? extends Component>[] excludedeComponentTypes;
	
	// Secondary query options
	/** Entities must have this tag to match this query, unless this tag is <code>null</code>. */
	private final String entityTag;
	/** Indicates whether results of this query may be disposed by the system that consumes it. */
	private final boolean isDisposable;
	
	public EntityQuery(Class<? extends Component>[] includedComponentTypes, Class<? extends Component>[] excludedComponentTypes, boolean disposable, String tag) {
		this.includedComponentTypes = includedComponentTypes;
		this.excludedeComponentTypes = excludedComponentTypes;
		this.isDisposable = disposable;
		this.entityTag = tag;
		
		// Validate arguments
		Checks.isNotNull(includedComponentTypes, "includedComponentTypes");
		Checks.isNotEmpty(includedComponentTypes, "length of includedComponentTypes must be greater than 0");
		
		if (excludedComponentTypes != null) {
			Checks.areDisjunct(excludedComponentTypes, includedComponentTypes, "excludedComponentTypes and includedComponentTypes must be disjunct");
		}
		
		entityChunks = new DeferredList<>();
		entityChunkMap = new HashMap<>();
	}
	
	/**
	 * Removes all component groups.
	 */
	@Override
	public void destroy() {
		if (entityChunks.isIterating()) {
			entityChunks.endIteration();
		}
		entityChunks.destroy();
		entityChunkMap.clear();
	}
	
	/**
	 * Prepares this entity query for an iteration.
	 * @throws IllegalStateException If this entity query is already being iterated.
	 * @deprecated Replaced by {@link DeferredList#endIteration()} as of 2.0.3
	 */
	@Deprecated
	public void startIteration() {
		entityChunks.startIteration();
	}
	
	/**
	 * Marks the ongoing iteration of this entity query as done.
	 * @throws IllegalStateException If this entity query is not being iterated.
	 * @deprecated Replaced by {@link DeferredList#endIteration()} as of 2.0.3
	 */
	@Deprecated
	public void endIteration() {
		entityChunks.endIteration();
	}
	
	/**
	 * Checks if an entity matches this query and if it does, adds the entity to the array of matches.
	 * @return True if the entity matches this query
	 */
	public boolean register(Entity entity) {
		Component[] matchComponents = getMatchingComponents(entity);
		
		if (matchComponents == null) {
			unregister(entity);
			return false;
		}
		
		if (entityChunkMap.containsKey(entity.id)) {
			return false;
		}
		
		try {
			EntityChunk entityChunk = new EntityChunk(includedComponentTypes);
			entityChunk.setComponents(matchComponents);
			
			entityChunks.add(entityChunk);
			entityChunkMap.put(entity.id, entityChunk);
		} catch (Exception e) {
			Logger.system.error("Failed to create entity chunk", e);
			return false;
		}
		
		return true;
	}
	
	/**
	 * Unregisters an entity in this query.
	 * @param entity The entity
	 * @return True if this query was affected.
	 */
	public boolean unregister(Entity entity) {
		if (!entityChunkMap.containsKey(entity.id)) {
			return false;
		}
		
		EntityChunk chunk = entityChunkMap.get(entity.id);
		entityChunks.remove(chunk);
		entityChunkMap.remove(entity.id);
		return true;
	}
	
	/**
	 * Returns an array of all matching components of an entity if it matches this query.
	 * Returns null if any component is missing.
	 */
	private Component[] getMatchingComponents(Entity entity) {
		// Check if entity has any components
		if (entity.components.isEmpty()) {
			return null;
		}
		// Check if entity matches tag
		if (entityTag != null && (entity.tag == null || !entity.tag.equals(entityTag))) {
			return null;
		}
		
		Component[] components = new Component[includedComponentTypes.length];
		
		// Check if entity has components of all included types and none of the excluded types
		boolean match = true;
		if (excludedeComponentTypes != null) {
			for (Class<? extends Component> componentType : excludedeComponentTypes) {
				if (entity.getComponent(componentType) != null) {
					match = false;
					break;
				}
			}
		}
		
		if (!match) {
			return null;
		}
		
		for (int i = 0; i < includedComponentTypes.length; i++) {
			Component component = entity.getComponent(includedComponentTypes[i]);
			if (component == null) {
				match = false;
				break;
			}
			components[i] = component;
		}
		
		if (!match) {
			return null;
		} else {
			return components;
		}
	}
	
	/**
	 * Checks if this entity query has matched any entities.
	 */
	public boolean hasEntityChunks() {
		return !entityChunks.isSnapshotEmpty();
	}
	
	public void print() {
		print(Logger.system);
	}
	
	public void print(Logger logger) {
		int componentCount = includedComponentTypes.length;
		String[] componentNames = new String[componentCount];
		
		for (int i = 0; i < componentCount; i++) {
			Class<? extends Component> componentClass = includedComponentTypes[i];
			componentNames[i] = componentClass.getSimpleName();
		}
		
		logger.logf("EntityQuery: [%s] (%s)", String.join(", ", componentNames), componentCount);
	}
}
