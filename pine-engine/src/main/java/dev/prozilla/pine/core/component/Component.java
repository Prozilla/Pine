package dev.prozilla.pine.core.component;

import dev.prozilla.pine.common.Lifecycle;
import dev.prozilla.pine.core.context.Window;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.World;
import dev.prozilla.pine.core.entity.camera.Camera;
import dev.prozilla.pine.core.rendering.Renderer;
import dev.prozilla.pine.core.state.Timer;
import dev.prozilla.pine.core.state.Tracker;
import dev.prozilla.pine.core.state.input.Input;

import java.util.ArrayList;

/**
 * Represents the logics layer of a game object.
 * Components can be attached to game objects to affect their behaviour and/or visuals.
 * Components are split into the following categories:
 * <ul>
 *     <li><strong>Renderers</strong>: Responsible for rendering game objects on the screen.</li>
 *     <li><strong>Transforms</strong>: Responsible for positioning different game objects on the screen.</li>
 *     <li><strong>Groups</strong>: Responsible for aligning the children of an game object on the screen.</li>
 * </ul>
 */
public class Component implements Lifecycle {
	
	public boolean enabled;
	
	private final int id;
	
	/** The entity that this component is attached to. */
	public Entity entity;
	
	private static int lastId = 0;
	
	public Component() {
		id = generateId();
		enabled = true;
	}
	
	@Override
	public void init(long window) {}
	
	@Override
	public void start() {}
	
	@Override
	public void input(float deltaTime) {}
	
	@Override
	public void update(float deltaTime) {}
	
	@Override
	public void render(Renderer renderer) {}
	
	@Override
	public void destroy() {}
	
	/**
	 * Enables this component.
	 */
	public void enable() {
		enabled = true;
	}
	
	/**
	 * Disables this component.
	 */
	public void disable() {
		enabled = false;
	}
	
	/**
	 * Getter for the entity this component is attached to.
	 * @return Game object
	 */
	public Entity getEntity() {
		return entity;
	}
	
	/**
	 * Generates a new unique component ID.
	 * @return Component ID
	 */
	public static int generateId() {
		return lastId++;
	}
	
	public int getId() {
		return id;
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
	
	public Camera getCamera() {
		return entity.getCamera();
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
	
	public <ComponentType extends Component> ArrayList<ComponentType> getComponentsInChildren(Class<ComponentType> componentClass) {
		return entity.getComponentsInChildren(componentClass);
	}
	
	public <ComponentType extends Component> ComponentType getComponent(Class<ComponentType> componentClass) {
		return entity.getComponent(componentClass);
	}
	
	public <ComponentType extends Component> ArrayList<ComponentType> getComponents(Class<ComponentType> componentClass) {
		return entity.getComponents(componentClass);
	}
	
	public boolean equals(Component component) {
		return component.getId() == id;
	}
}
