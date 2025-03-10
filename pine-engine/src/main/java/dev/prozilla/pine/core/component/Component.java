package dev.prozilla.pine.core.component;

import dev.prozilla.pine.common.Lifecycle;
import dev.prozilla.pine.common.Printable;
import dev.prozilla.pine.core.Application;
import dev.prozilla.pine.core.ApplicationProvider;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.EntityProvider;
import dev.prozilla.pine.core.scene.Scene;
import dev.prozilla.pine.core.scene.SceneProvider;

import java.util.List;

/**
 * Contains a partition of the data of an entity.
 */
public abstract class Component implements Lifecycle, Printable, EntityProvider, ComponentProvider, ApplicationProvider, SceneProvider {
	
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
	public Entity getChildWithTag(String tag) {
		return entity.getChildWithTag(tag);
	}
	
	@Override
	public Entity getParentWithTag(String tag) {
		return entity.getParentWithTag(tag);
	}
	
	@Override
	public <ComponentType extends Component> ComponentType getComponentInParent(Class<ComponentType> componentClass) {
		return entity.getComponentInParent(componentClass);
	}
	
	@Override
	public <ComponentType extends Component> ComponentType getComponentInParent(Class<ComponentType> componentClass, boolean includeAncestors) {
		return entity.getComponentInParent(componentClass, includeAncestors);
	}
	
	@Override
	public <ComponentType extends Component> List<ComponentType> getComponentsInChildren(Class<ComponentType> componentClass) {
		return entity.getComponentsInChildren(componentClass);
	}
	
	@Override
	public <ComponentType extends Component> ComponentType getComponent(Class<ComponentType> componentClass) {
		return entity.getComponent(componentClass);
	}
	
	@Override
	public <ComponentType extends Component> List<ComponentType> getComponents(Class<ComponentType> componentClass) {
		return entity.getComponents(componentClass);
	}
	
	@Override
	public int hashCode() {
		return id;
	}
	
	public boolean equals(Component component) {
		return component.id == id;
	}
	
	@Override
	public String toString() {
		return String.format("%s: %s", getName(), getEntity().getName());
	}
}
