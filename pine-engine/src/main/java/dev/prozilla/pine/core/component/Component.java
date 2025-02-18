package dev.prozilla.pine.core.component;

import dev.prozilla.pine.common.Lifecycle;
import dev.prozilla.pine.common.Printable;
import dev.prozilla.pine.common.logging.Logger;
import dev.prozilla.pine.core.Window;
import dev.prozilla.pine.core.World;
import dev.prozilla.pine.core.component.camera.CameraData;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.rendering.Renderer;
import dev.prozilla.pine.core.state.Timer;
import dev.prozilla.pine.core.state.Tracker;
import dev.prozilla.pine.core.state.input.Input;

import java.util.List;

/**
 * Contains a partition of the data of an entity.
 */
public abstract class Component implements Lifecycle, Printable {
	
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
	
	public String getName() {
		return "Component";
	}
	
	public Input getInput() {
		return entity.getInput();
	}
	
	public Window getWindow() {
		return entity.getWindow();
	}
	
	public Renderer getRenderer() {
		return entity.getRenderer();
	}
	
	public Timer getTimer() {
		return entity.getTimer();
	}
	
	public Tracker getTracker() {
		return entity.getTracker();
	}
	
	public World getWorld() {
		return entity.getWorld();
	}
	
	public CameraData getCamera() {
		return entity.getCamera();
	}
	
	public Logger getLogger() {
		return entity.getLogger();
	}
	
	public Transform getTransform() {
		return entity.transform;
	}
	
	public <ComponentType extends Component> ComponentType getComponentInParent(Class<ComponentType> componentClass) {
		return entity.getComponentInParent(componentClass);
	}
	
	public <ComponentType extends Component> ComponentType getComponentInParent(Class<ComponentType> componentClass, boolean includeAncestors) {
		return entity.getComponentInParent(componentClass, includeAncestors);
	}
	
	public <ComponentType extends Component> List<ComponentType> getComponentsInChildren(Class<ComponentType> componentClass) {
		return entity.getComponentsInChildren(componentClass);
	}
	
	public <ComponentType extends Component> ComponentType getComponent(Class<ComponentType> componentClass) {
		return entity.getComponent(componentClass);
	}
	
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
