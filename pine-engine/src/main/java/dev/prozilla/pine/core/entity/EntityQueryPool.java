package dev.prozilla.pine.core.entity;

import dev.prozilla.pine.core.component.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a pool that creates and stores entity queries.
 * Each world has its own pool, because each world has different entities and components.
 * Certain entity queries are disposable, meaning the system that consumes them might dispose the results of that query.
 * Disposable queries cannot be included in the pool as their results are temporary.
 */
public class EntityQueryPool {
	
	private final Map<String, EntityQuery> entityQueries;
	
	public EntityQueryPool() {
		entityQueries = new HashMap<>();
	}
	
	/**
	 * Fetches an entity query from the pool or creates a new one if it doesn't exist yet.
	 * If <code>disposable</code> is set to <code>true</code>, a new query will always be created.
	 * Disposable queries are queries that are consumed by systems that might dispose the results of their query,
	 * which means the query cannot be reused by other systems.
	 * @param includedComponentTypes Component types of the query
	 * @param disposable Indicates whether the system that will consume this query might dispose its results.
	 * @param tag Tag that the entity must have
	 */
	public EntityQuery getQuery(Class<? extends Component>[] includedComponentTypes, Class<? extends Component>[] excludedComponentTypes, boolean disposable, String tag) {
		if (disposable) {
			return new EntityQuery(includedComponentTypes, excludedComponentTypes, true, tag);
		}
		
		String key = generateQueryKey(includedComponentTypes, excludedComponentTypes, tag);
		
		// Fetch existing query from pool
		if (entityQueries.containsKey(key)) {
			return entityQueries.get(key);
		}
		
		// Create new query and add to pool
		EntityQuery entityQuery = new EntityQuery(includedComponentTypes, excludedComponentTypes, false, tag);
		entityQueries.put(key, entityQuery);
		
		return entityQuery;
	}
	
	public void clear() {
		for (EntityQuery query : entityQueries.values()) {
			query.destroy();
		}
		entityQueries.clear();
	}
	
	/**
	 * Generates a unique string key based on an array of component types for an entity query.
	 * The key is generated by concatenating the names of the component types in alphabetical order, separated by colons ":".
	 * @param includedComponentTypes Component types of the query
	 * @param tag Entity tag of the query
	 * @return String key
	 */
	private static String generateQueryKey(Class<? extends Component>[] includedComponentTypes, Class<? extends Component>[] excludedComponentTypes, String tag) {
		// Collect names of component types
		ArrayList<String> includedTypeNames = new ArrayList<>();
		for (Class<?> componentType : includedComponentTypes) {
			includedTypeNames.add(componentType.getName());
		}
		// Sort component types to ensure the order is consistent
		Collections.sort(includedTypeNames);
		
		// Compose string of component types separated by colons
		StringBuilder stringBuilder = new StringBuilder(String.join(":", includedTypeNames));
		
		stringBuilder.append(',');
		if (excludedComponentTypes != null && excludedComponentTypes.length > 0) {
			// Collect names of component types
			ArrayList<String> excludedTypeNames = new ArrayList<>();
			for (Class<?> componentType : excludedComponentTypes) {
				excludedTypeNames.add(componentType.getName());
			}
			// Sort component types to ensure the order is consistent
			Collections.sort(excludedTypeNames);
			
			// Compose string of component types separated by colons
			stringBuilder.append(String.join(":", excludedTypeNames));
		}
		
		// Append tag, if present
		stringBuilder.append(',');
		if (tag != null) {
			stringBuilder.append(tag);
		}

		return stringBuilder.toString();
	}
}
