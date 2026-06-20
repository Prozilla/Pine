package dev.prozilla.pine.core.component;

import org.jetbrains.annotations.Contract;

import java.util.List;

public interface ComponentsContext {
	
	default <ComponentType extends Component> ComponentType getComponentAbove(Class<ComponentType> componentClass) {
		return getComponentAbove(componentClass, true);
	}
	
	default <ComponentType extends Component> ComponentType getComponentAbove(Class<ComponentType> componentClass, boolean includeGrandParents) {
		return getComponentAbove(componentClass, includeGrandParents, false);
	}
	
	<ComponentType extends Component> ComponentType getComponentAbove(Class<ComponentType> componentClass, boolean includeGrandParents, boolean includeSelf);
	
	@Contract("_ -> !null")
	default <ComponentType extends Component> List<ComponentType> getComponentsAbove(Class<ComponentType> componentClass) {
		return getComponentsAbove(componentClass, false);
	}
	
	@Contract("_, _ -> !null")
	default <ComponentType extends Component> List<ComponentType> getComponentsAbove(Class<ComponentType> componentClass, boolean includeGrandParents) {
		return getComponentsAbove(componentClass, includeGrandParents, false);
	}
	
	@Contract("_, _, _ -> !null")
	<ComponentType extends Component> List<ComponentType> getComponentsAbove(Class<ComponentType> componentClass, boolean includeGrandParents, boolean includeSelf);
	
	@Contract("_ -> !null")
	default <ComponentType extends Component> List<ComponentType> getComponentsBelow(Class<ComponentType> componentClass) {
		return getComponentsBelow(componentClass, false);
	}
	
	@Contract("_, _ -> !null")
	default <ComponentType extends Component> List<ComponentType> getComponentsBelow(Class<ComponentType> componentClass, boolean includeGrandChildren) {
		return getComponentsBelow(componentClass, includeGrandChildren, false);
	}
	
	@Contract("_, _, _ -> !null")
	default <ComponentType extends Component> List<ComponentType> getComponentsBelow(Class<ComponentType> componentClass, boolean includeGrandChildren, boolean includeSelf) {
		return getComponentsBelow(componentClass, includeGrandChildren, includeSelf, true);
	}
	
	@Contract("_, _, _, _ -> !null")
	<ComponentType extends Component> List<ComponentType> getComponentsBelow(Class<ComponentType> componentClass, boolean includeGrandChildren, boolean includeSelf, boolean includeNested);
	
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
