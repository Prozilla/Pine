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
		Entity entity = getEntity();
		return entity != null ? entity.transform : null;
	}
	
	@Override
	default Entity getFirstChild() {
		Transform transform = getTransform();
		return transform != null ? transform.getFirstChild() : null;
	}
	
	@Override
	default Entity getLastChild() {
		Transform transform = getTransform();
		return transform != null ? transform.getLastChild() : null;
	}
	
	@Override
	default Entity getChild(int i) {
		Transform transform = getTransform();
		return transform != null ? transform.getChild(i) : null;
	}
	
	@Override
	default Entity getParent() {
		Transform transform = getTransform();
		return transform != null && transform.parent != null ? transform.parent.getEntity() : null;
	}
	
	@Override
	default boolean hasParent() {
		Transform transform = getTransform();
		return transform != null && transform.parent != null;
	}
	
	@Override
	default boolean isDescendantOf(Transform parent) {
		Transform transform = getTransform();
		return transform != null && transform.isDescendantOf(parent);
	}
	
	@Override
	default Entity getChildWithTag(String tag) {
		Transform transform = getTransform();
		return transform != null ? transform.getChildWithTag(tag) : null;
	}
	
	@Override
	default Entity getParentWithTag(String tag) {
		Transform transform = getTransform();
		return transform != null ? transform.getParentWithTag(tag) : null;
	}
	
}
