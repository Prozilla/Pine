package dev.prozilla.pine.core.entity;

import dev.prozilla.pine.core.component.Transform;

/**
 * Interface with utility methods for finding certain entities in the current context.
 */
public interface EntityProvider extends EntityContext {
	
	Entity getEntity();
	
	default Transform getTransform() {
		return getEntity().transform;
	}
	
	@Override
	default Entity getFirstChild() {
		return getTransform().getFirstChild();
	}
	
	@Override
	default Entity getLastChild() {
		return getTransform().getLastChild();
	}
	
	@Override
	default Entity getChildWithTag(String tag) {
		return getTransform().getChildWithTag(tag);
	}
	
	@Override
	default Entity getParentWithTag(String tag) {
		return getTransform().getParentWithTag(tag);
	}
}
