package dev.prozilla.pine.core.component;

import dev.prozilla.pine.core.entity.Entity;

import java.util.Collections;
import java.util.List;

/**
 * Interface with utility methods for finding certain components in the current context.
 */
@FunctionalInterface
public interface ComponentQueryProvider extends ComponentQueryContext {
	
	Entity getEntity();
	
	@Override
	default <ComponentType extends Component> ComponentType getComponentAbove(Class<ComponentType> componentClass, ComponentQuery query) {
		if (getEntity() == null) {
			return null;
		}
		return getEntity().getComponentAbove(componentClass, query);
	}
	
	@Override
	default <ComponentType extends Component> List<ComponentType> getComponentsAbove(Class<ComponentType> componentClass, ComponentQuery query) {
		if (getEntity() == null) {
			return Collections.emptyList();
		}
		return getEntity().getComponentsAbove(componentClass, query);
	}
	
	@Override
	default <ComponentType extends Component> List<ComponentType> getComponentsBelow(Class<ComponentType> componentClass, ComponentQuery query) {
		if (getEntity() == null) {
			return Collections.emptyList();
		}
		return getEntity().getComponentsBelow(componentClass, query);
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
			return Collections.emptyList();
		}
		return getEntity().getComponents(componentClass);
	}
	
}
