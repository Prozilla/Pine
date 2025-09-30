package dev.prozilla.pine.core.entity;

import dev.prozilla.pine.common.ProviderOf;
import dev.prozilla.pine.core.component.Transform;

/**
 * Interface with utility methods for finding certain entities in the current context.
 */
@ProviderOf(Entity.class)
@FunctionalInterface
public interface EntityProvider extends EntityContext {
	
	Entity getEntity();
	
	default Transform getTransform() {
		if (getEntity() == null) {
			return null;
		}
		return getEntity().transform;
	}
	
	@Override
	default Entity getFirstChild() {
		if (getTransform() == null) {
			return null;
		}
		return getTransform().getFirstChild();
	}
	
	@Override
	default Entity getLastChild() {
		if (getTransform() == null) {
			return null;
		}
		return getTransform().getLastChild();
	}
	
	@Override
	default boolean isDescendantOf(Transform parent) {
		if (getTransform() == null) {
			return false;
		}
		return getTransform().isDescendantOf(parent);
	}
	
	@Override
	default Entity getChildWithTag(String tag) {
		if (getTransform() == null) {
			return null;
		}
		return getTransform().getChildWithTag(tag);
	}
	
	@Override
	default Entity getParentWithTag(String tag) {
		if (getTransform() == null) {
			return null;
		}
		return getTransform().getParentWithTag(tag);
	}
	
}
