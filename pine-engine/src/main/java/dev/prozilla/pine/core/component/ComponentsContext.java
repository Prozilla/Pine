package dev.prozilla.pine.core.component;

import java.util.List;

public interface ComponentsContext {
	
	<ComponentType extends Component> ComponentType getComponentInParent(Class<ComponentType> componentClass);
	
	<ComponentType extends Component> ComponentType getComponentInParent(Class<ComponentType> componentClass, boolean recursive);
	
	<ComponentType extends Component> List<ComponentType> getComponentsInChildren(Class<ComponentType> componentClass);
	
	/**
	 * Gets a component of a given class.
	 * @param componentClass Class of the component
	 * @return Component of the given class, or null if there isn't one.
	 */
	<ComponentType extends Component> ComponentType getComponent(Class<ComponentType> componentClass);
	
	/**
	 * Gets all components of a given class.
	 * @param componentClass Class of the components
	 * @return Components of the given class.
	 */
	<ComponentType extends Component> List<ComponentType> getComponents(Class<ComponentType> componentClass);
	
}
