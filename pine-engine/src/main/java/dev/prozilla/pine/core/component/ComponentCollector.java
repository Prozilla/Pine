package dev.prozilla.pine.core.component;

import dev.prozilla.pine.common.Lifecycle;
import dev.prozilla.pine.core.entity.Entity;

import java.util.ArrayList;

/**
 * Utility class for collecting a certain type of component.
 */
public class ComponentCollector implements Lifecycle {
	
	/**
	 * Groups of components that belong to the same entity and match the requirements of the collection.
	 */
	public final ArrayList<ComponentGroup> componentGroups;
	
	/**
	 * IDs of entities that have been registered by this collector.
	 * This is used to avoid duplication in cases where this collector is shared between multiple systems.
	 */
	private final ArrayList<Integer> registeredEntityIds;
	
	/**
	 * Entities must have components of these classes to be included in the collection.
	 */
	private final Class<? extends Component>[] componentClasses;
	
	@SafeVarargs
	public ComponentCollector(Class<? extends Component>... componentClasses) {
		this.componentClasses = componentClasses;
		
		if (componentClasses.length == 0) {
			throw new IllegalArgumentException("Length of componentClasses must be greater than 0.");
		}
		
		componentGroups = new ArrayList<>();
		registeredEntityIds = new ArrayList<>();
	}
	
	/**
	 * Removes all component groups.
	 */
	@Override
	public void destroy() {
		componentGroups.clear();
		registeredEntityIds.clear();
	}
	
	/**
	 * Adds components of a given entity to a component group, if it meets the requirements of the collection.
	 */
	public void register(Entity entity) {
		if (entity.components.isEmpty() || registeredEntityIds.contains(entity.getId())) {
			return;
		}
		
		Component[] components = new Component[componentClasses.length];
		
		boolean match = true;
		for (int i = 0; i < componentClasses.length; i++) {
			Component component = entity.getComponent(componentClasses[i]);
			if (component == null) {
				match = false;
				break;
			}
			components[i] = component;
		}
		
		if (!match) {
			return;
		}
		
		try {
			ComponentGroup componentGroup = new ComponentGroup(componentClasses);
			componentGroup.setComponents(components);
			componentGroups.add(componentGroup);
		} catch (Exception e) {
			System.err.println("Failed to create component group.");
			e.printStackTrace();
		} finally {
			registeredEntityIds.add(entity.getId());
		}
	}
}
