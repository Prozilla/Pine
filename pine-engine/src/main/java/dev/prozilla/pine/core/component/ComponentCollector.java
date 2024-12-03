package dev.prozilla.pine.core.component;

import dev.prozilla.pine.common.Lifecycle;

import java.util.ArrayList;

/**
 * Utility class for collecting a certain type of component.
 * @param <C> Type of the components in this collection.
 */
public class ComponentCollector<C extends Component> implements Lifecycle {
	
	/**
	 * Collection of collected components.
	 */
	public final ArrayList<C> components;
	
	/**
	 * Class this determines which components are included in the collection.
	 */
	private final Class<C> componentClass;
	
	public ComponentCollector(Class<C> componentClass) {
		this.componentClass = componentClass;
		
		components = new ArrayList<>();
	}
	
	/**
	 * Removes all components from the collection.
	 */
	@Override
	public void destroy() {
		components.clear();
	}
	
	/**
	 * Adds a component to the collection, if it meets the requirements of the collection.
	 */
	public void register(Component component) {
		if (componentClass.isInstance(component)) {
			components.add(componentClass.cast(component));
		}
	}
}
