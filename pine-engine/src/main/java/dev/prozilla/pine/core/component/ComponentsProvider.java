package dev.prozilla.pine.core.component;

import dev.prozilla.pine.core.entity.Entity;

import java.util.List;

/**
 * Interface with utility methods for finding certain components in the current context.
 */
public interface ComponentsProvider extends ComponentsContext {
	
	Entity getEntity();
	
	@Override
	default <ComponentType extends Component> ComponentType getComponentInParent(Class<ComponentType> componentClass) {
		return getEntity().getComponentInParent(componentClass);
	}
	
	@Override
	default <ComponentType extends Component> ComponentType getComponentInParent(Class<ComponentType> componentClass, boolean recursive) {
		return getEntity().getComponentInParent(componentClass, recursive);
	}
	
	@Override
	default <ComponentType extends Component> List<ComponentType> getComponentsInChildren(Class<ComponentType> componentClass) {
		return getEntity().getComponentsInChildren(componentClass);
	}
	
	@Override
	default <ComponentType extends Component> ComponentType getComponent(Class<ComponentType> componentClass) {
		return getEntity().getComponent(componentClass);
	}
	
	@Override
	default <ComponentType extends Component> List<ComponentType> getComponents(Class<ComponentType> componentClass) {
		return getEntity().getComponents(componentClass);
	}
	
}
