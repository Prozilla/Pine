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
	default <ComponentType extends Component> ComponentType getComponentAbove(Class<ComponentType> componentClass, boolean includeGrandParents, boolean includeSelf) {
		if (getEntity() == null) {
			return null;
		}
		return getEntity().getComponentAbove(componentClass, includeGrandParents, includeSelf);
	}
	
	@Override
	default <ComponentType extends Component> List<ComponentType> getComponentsAbove(Class<ComponentType> componentClass, boolean includeGrandParents, boolean includeSelf) {
		if (getEntity() == null) {
			return new ArrayList<>();
		}
		return getEntity().getComponentsAbove(componentClass, includeGrandParents, includeSelf);
	}
	
	@Override
	default <ComponentType extends Component> List<ComponentType> getComponentsBelow(Class<ComponentType> componentClass, boolean includeGrandChildren, boolean includeSelf, boolean includeNested) {
		if (getEntity() == null) {
			return new ArrayList<>();
		}
		return getEntity().getComponentsBelow(componentClass, includeGrandChildren, includeSelf, includeNested);
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
