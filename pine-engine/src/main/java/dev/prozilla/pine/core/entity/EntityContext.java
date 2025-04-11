package dev.prozilla.pine.core.entity;

import dev.prozilla.pine.core.component.Transform;

public interface EntityContext {
	
	Entity getFirstChild();
	
	Entity getLastChild();
	
	default boolean isDescendantOf(Entity parent) {
		return isDescendantOf(parent.transform);
	}
	
	boolean isDescendantOf(Transform parent);
	
	/**
	 * Gets a child entity with a given tag.
	 * @param tag Tag of the child entity.
	 * @return The child entity with the given tag, or null if there isn't one.
	 */
	Entity getChildWithTag(String tag);
	
	/**
	 * Gets a parent entity with a given tag.
	 * @param tag Tag of the parent entity.
	 * @return The parent entity with the given tag, or null if there isn't one.
	 */
	Entity getParentWithTag(String tag);
	
}
