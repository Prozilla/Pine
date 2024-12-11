package dev.prozilla.pine.core.entity;

import dev.prozilla.pine.common.Lifecycle;
import dev.prozilla.pine.common.array.ArrayUtils;
import dev.prozilla.pine.core.component.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Utility class for querying entities with specific components to be processed by a system.
 */
public class EntityQuery implements Lifecycle {
	
	/** List of entities that match this query. */
	public final ArrayList<EntityMatch> entityMatches;
	
	/**
	 * Map of entity IDs to their respective match.
	 */
	private final Map<Integer, EntityMatch> entityIdToMatch;
	
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
		
		Objects.requireNonNull(includedComponentTypes, "includedComponentTypes must not be null.");
		
		if (includedComponentTypes.length == 0) {
			throw new IllegalArgumentException("Length of includedComponentTypes must be greater than 0.");
		}
		
		if (ArrayUtils.overlaps(excludedComponentTypes, includedComponentTypes)) {
			throw new IllegalArgumentException("Excluded component types must not overlap with included component types.");
		}
		
		entityMatches = new ArrayList<>();
		entityIdToMatch = new HashMap<>();
	}
	
	/**
	 * Removes all component groups.
	 */
	@Override
	public void destroy() {
		entityMatches.clear();
		entityIdToMatch.clear();
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
		
		if (entityIdToMatch.containsKey(entity.id)) {
			return false;
		}
		
		EntityMatch entityMatch = null;
		try {
			entityMatch = new EntityMatch(includedComponentTypes);
			entityMatch.setComponents(matchComponents);
		} catch (Exception e) {
			System.err.println("Failed to create entity match.");
			e.printStackTrace();
			return false;
		} finally {
			if (entityMatch != null) {
				entityMatches.add(entityMatch);
				entityIdToMatch.put(entity.id, entityMatch);
			}
		}
		
		return true;
	}
	
	/**
	 * Unregisters an entity in this query.
	 * @param entity The entity
	 * @return True if this query was affected.
	 */
	public boolean unregister(Entity entity) {
		if (!entityIdToMatch.containsKey(entity.id)) {
			return false;
		}
		
		EntityMatch match = entityIdToMatch.get(entity.id);
		entityMatches.remove(match);
		entityIdToMatch.remove(entity.id);
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
	
	public void print() {
		int componentCount = includedComponentTypes.length;
		String[] componentNames = new String[componentCount];
		
		for (int i = 0; i < componentCount; i++) {
			Class<? extends Component> componentClass = includedComponentTypes[i];
			componentNames[i] = componentClass.getSimpleName();
		}
		
		System.out.printf("EntityQuery: [%s] (%s)%n", String.join(", ", componentNames), componentCount);
	}
}
