package dev.prozilla.pine.core.entity;

import dev.prozilla.pine.common.Lifecycle;
import dev.prozilla.pine.core.component.Component;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Utility class for querying entities with specific components to be processed by a system.
 */
public class EntityQuery implements Lifecycle {
	
	/** List of entities that match this query. */
	public final ArrayList<EntityMatch> entityMatches;
	
	/**
	 * IDs of entities that have been registered by this collector.
	 * This is used to avoid duplication in cases where this collector is shared between multiple systems.
	 */
	private final ArrayList<Integer> registeredEntityIds;
	
	// Primary query options
	/** Entities must have components of these types to match this query. */
	private final Class<? extends Component>[] componentTypes;
	
	// Secondary query options
	/** Entities must have this tag to match this query, unless this tag is <code>null</code>. */
	private final String entityTag;
	/** Indicates whether results of this query may be disposed by the system that consumes it. */
	private final boolean isDisposable;
	
	public EntityQuery(Class<? extends Component>[] componentTypes, boolean disposable, String tag) {
		this.componentTypes = componentTypes;
		this.isDisposable = disposable;
		this.entityTag = tag;
		
		Objects.requireNonNull(componentTypes, "componentTypes must not be null.");
		
		if (componentTypes.length == 0) {
			throw new IllegalArgumentException("Length of componentTypes must be greater than 0.");
		}
		
		entityMatches = new ArrayList<>();
		registeredEntityIds = new ArrayList<>();
	}
	
	/**
	 * Removes all component groups.
	 */
	@Override
	public void destroy() {
		entityMatches.clear();
		registeredEntityIds.clear();
	}
	
	/**
	 * Checks if an entity matches this query and if it does, adds the entity to the array of matches.
	 * @return True if the entity matches this query
	 */
	public boolean register(Entity entity) {
		if (registeredEntityIds.contains(entity.id)) {
			return false;
		}
		
		Component[] components = getMatchingComponents(entity);
		
		if (components == null) {
			return false;
		}
		
		try {
			EntityMatch entityMatch = new EntityMatch(componentTypes);
			entityMatch.setComponents(components);
			entityMatches.add(entityMatch);
		} catch (Exception e) {
			System.err.println("Failed to create component group.");
			e.printStackTrace();
			return false;
		} finally {
			registeredEntityIds.add(entity.id);
		}
		
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
		
		Component[] components = new Component[componentTypes.length];
		
		// Check if entity has components of all required types
		boolean match = true;
		for (int i = 0; i < componentTypes.length; i++) {
			Component component = entity.getComponent(componentTypes[i]);
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
		int componentCount = componentTypes.length;
		String[] componentNames = new String[componentCount];
		
		for (int i = 0; i < componentCount; i++) {
			Class<? extends Component> componentClass = componentTypes[i];
			componentNames[i] = componentClass.getSimpleName();
		}
		
		System.out.printf("EntityQuery: [%s] (%s)%n", String.join(", ", componentNames), componentCount);
	}
}
