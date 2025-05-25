package dev.prozilla.pine.core.component;

import dev.prozilla.pine.common.Printable;
import dev.prozilla.pine.common.lifecycle.Destructable;
import dev.prozilla.pine.core.Application;
import dev.prozilla.pine.core.ApplicationProvider;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.EntityProvider;
import dev.prozilla.pine.core.scene.Scene;
import dev.prozilla.pine.core.scene.SceneProvider;

/**
 * Contains a partition of the data of an entity.
 */
public abstract class Component implements Printable, Destructable, EntityProvider, ComponentsProvider, ApplicationProvider, SceneProvider {
	
	// Identifier
	public final int id;
	
	// State
	public boolean isActive;
	/** The entity that this component is attached to. */
	public Entity entity;
	
	public Component() {
		id = ComponentManager.generateComponentId();
		isActive = true;
	}
	
	/**
	 * Removes this component from its entity.
	 */
	@Override
	public void destroy() {
		if (entity != null) {
			entity.removeComponent(this);
		}
	}
	
	/**
	 * Enables or disables this component.
	 * @param active True enables this component, false disables it.
	 */
	public void setActive(boolean active) {
		this.isActive = active;
	}
	
	/**
	 * Getter for the entity this component is attached to.
	 * @return Entity
	 */
	public Entity getEntity() {
		return entity;
	}
	
	@Override
	public Application getApplication() {
		return entity.getApplication();
	}
	
	@Override
	public Scene getScene() {
		return entity.getScene();
	}
	
	public String getName() {
		return getClass().getSimpleName();
	}
	
	@Override
	public int hashCode() {
		return id;
	}
	
	public boolean equals(Component component) {
		return component != null && component.id == id;
	}
	
	@Override
	public String toString() {
		return String.format("%s: %s", getName(), getEntity().getName());
	}
}
