package dev.prozilla.pine.core.component;

import org.jetbrains.annotations.Contract;

import java.util.List;

public interface ComponentQueryContext {
	
	ComponentQuery DEFAULT_PARENT_QUERY = ComponentQuery.NEAREST_LEVEL;
	ComponentQuery DEFAULT_CHILDREN_QUERY = ComponentQuery.SHALLOW;
	
	default <ComponentType extends Component> ComponentType getComponentAbove(Class<ComponentType> componentClass) {
		return getComponentAbove(componentClass, DEFAULT_PARENT_QUERY);
	}
	
	<ComponentType extends Component> ComponentType getComponentAbove(Class<ComponentType> componentClass, ComponentQuery query);
	
	@Contract("_ -> !null")
	default <ComponentType extends Component> List<ComponentType> getComponentsAbove(Class<ComponentType> componentClass) {
		return getComponentsAbove(componentClass, DEFAULT_PARENT_QUERY);
	}
	
	@Contract("_, _ -> !null")
	<ComponentType extends Component> List<ComponentType> getComponentsAbove(Class<ComponentType> componentClass, ComponentQuery query);
	
	@Contract("_ -> !null")
	default <ComponentType extends Component> List<ComponentType> getComponentsBelow(Class<ComponentType> componentClass) {
		return getComponentsBelow(componentClass, DEFAULT_CHILDREN_QUERY);
	}
	
	@Contract("_, _ -> !null")
	<ComponentType extends Component> List<ComponentType> getComponentsBelow(Class<ComponentType> componentClass, ComponentQuery query);
	
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
	@Contract("_ -> !null")
	<ComponentType extends Component> List<ComponentType> getComponents(Class<ComponentType> componentClass);
	
}
