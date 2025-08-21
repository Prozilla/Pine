package dev.prozilla.pine.core.component;

import dev.prozilla.pine.core.entity.Entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Interface with utility methods for finding certain components in the current context.
 */
@FunctionalInterface
public interface ComponentsProvider extends ComponentsContext {
	
	Entity getEntity();
	
	@Override
	default <ComponentType extends Component> ComponentType getComponentInParent(Class<ComponentType> componentClass) {
		if (getEntity() == null) {
			return null;
		}
		return getEntity().getComponentInParent(componentClass);
	}
	
	@Override
	default <ComponentType extends Component> ComponentType getComponentInParent(Class<ComponentType> componentClass, boolean recursive) {
		if (getEntity() == null) {
			return null;
		}
		return getEntity().getComponentInParent(componentClass, recursive);
	}
	
	@Override
	default <ComponentType extends Component> List<ComponentType> getComponentsInChildren(Class<ComponentType> componentClass) {
		if (getEntity() == null) {
			return new ArrayList<>();
		}
		return getEntity().getComponentsInChildren(componentClass);
	}
	
	@Override
	default <ComponentType extends Component> ComponentType getComponent(Class<ComponentType> componentClass) {
		if (getEntity() == null) {
			return null;
		}
		return getEntity().getComponent(componentClass);
	}
	
	@Override
	default <ComponentType extends Component> List<ComponentType> getComponents(Class<ComponentType> componentClass) {
		if (getEntity() == null) {
			return new ArrayList<>();
		}
		return getEntity().getComponents(componentClass);
	}
	
}
