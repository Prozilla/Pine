package dev.prozilla.pine.core.entity;

import dev.prozilla.pine.common.Lifecycle;
import dev.prozilla.pine.core.Application;
import dev.prozilla.pine.core.Scene;
import dev.prozilla.pine.core.Window;
import dev.prozilla.pine.core.World;
import dev.prozilla.pine.core.component.Component;
import dev.prozilla.pine.core.component.Transform;
import dev.prozilla.pine.core.component.camera.CameraData;
import dev.prozilla.pine.core.entity.prefab.Prefab;
import dev.prozilla.pine.core.rendering.Renderer;
import dev.prozilla.pine.core.state.Timer;
import dev.prozilla.pine.core.state.Tracker;
import dev.prozilla.pine.core.state.input.Input;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a unique entity in the world with a list of components.
 */
public class Entity implements Lifecycle {
	
	public final int id;
	private final String name;
	public String tag;
	protected boolean isActive;
	
	public final Transform transform;
	
	protected final World world;
	protected final Application application;
	protected final Scene scene;
	
	/** Components of this entity */
	public final List<Component> components;
	
	/**
	 * Creates an entity at the position (0, 0)
	 */
	public Entity(World world) {
		this(world, 0, 0);
	}
	
	/**
	 * Creates an entity at the position (0, 0)
	 */
	public Entity(World world, String name) {
		this(world, name, 0, 0);
	}
	
	/**
	 * Creates an entity at the position (x, y)
	 */
	public Entity(World world, float x, float y) {
		this(world, null, x, y);
	}
	
	/**
	 * Creates an entity at the position (x, y)
	 */
	public Entity(World world, String name, float x, float y) {
		this.world = world;
		this.name = name;

		application = world.application;
		scene = world.scene;
		
		id = EntityManager.generateEntityId();
		
		transform = new Transform(x, y);
		components = new ArrayList<>();
		addComponent(transform);

		isActive = true;
	}
	
	/**
	 * Destroys this entity at the end of the game loop.
	 */
	public void destroy() {
		if (transform.parent != null && application.isRunning) {
			transform.parent.getEntity().removeChild(this);
		}
		
		// Remove all references
//		game = null;
//		world = null;
//		scene = null;
	}
	
	/**
	 * Instantiates a prefab and adds the instance as a child of this entity
	 * @param prefab Prefab for the child entity
	 * @return Child entity
	 */
	public Entity addChild(Prefab prefab) throws IllegalStateException, IllegalArgumentException {
		return addChild(prefab.instantiate(world));
	}
	
	/**
	 * Adds a child to this entity.
	 * @param child Entity to add as a child
	 * @return Child entity
	 */
	public Entity addChild(Entity child) throws IllegalStateException, IllegalArgumentException {
		if (child == null) {
			throw new IllegalArgumentException("Child can't be null");
		}
		
		if (transform.children.contains(child.transform)) {
			throw new IllegalStateException("Entity is already a child");
		}
		
		transform.children.add(child.transform);
		child.transform.setParent(transform);
		
		if (isRegistered()) {
			world.addEntity(child);
		}
		
		return child;
	}
	
	/**
	 * Adds children to this entity.
	 * @param children Child objects
	 */
	public void addChildren(Entity... children) throws IllegalStateException, IllegalArgumentException {
		for (Entity child : children) {
			addChild(child);
		}
	}
	
	/**
	 * Removes a child from this entity.
	 * @param child Child object
	 */
	public void removeChild(Entity child) throws IllegalStateException, IllegalArgumentException {
		if (child == null) {
			throw new IllegalArgumentException("Child can't be null");
		}
		
		boolean removed = transform.children.remove(child.transform);
		if (!removed) {
			throw new IllegalStateException("Entity is not a child");
		}
		child.transform.setParent(transform);
		getTracker().removeEntity();
	}
	
	/**
	 * Removes children from this entity.
	 * @param children Child entities
	 */
	public void removeChildren(Entity... children) throws IllegalStateException, IllegalArgumentException {
		for (Entity child : children) {
			removeChild(child);
		}
	}
	
	/**
	 * Setter for the parent entity.
	 * @param parent Parent entity
	 */
	public void setParent(Entity parent) {
		transform.setParent(parent.transform);
	}
	
	/**
	 * Enables/disables this entity.
	 */
	public void setActive(boolean active) {
		if (this.isActive == active) {
			return;
		}
		
		this.isActive = active;
		
		if (active) {
			world.activateEntity(this);
		}
	}
	
	public boolean isActive() {
		if (transform.parent == null) {
			return isActive;
		} else {
			Entity parent = transform.parent.getEntity();
			return isActive && (parent == null || parent.isActive());
		}
	}
	
	/**
	 * Adds a component to this entity.
	 * @param component Component
	 */
	public <C extends Component> C addComponent(C component) {
		world.addComponent(this, component);
		return component;
	}
	
	/**
	 * Removes a component from this entity.
	 * @param component Component
	 */
	public void removeComponent(Component component) {
//		if (!components.remove(component)) {
//			return;
//		}
//
//		component.remove();
	}
	
	public <ComponentType extends Component> ComponentType getComponentInParent(Class<ComponentType> componentClass) {
		return getComponentInParent(componentClass, true);
	}
	
	public <ComponentType extends Component> ComponentType getComponentInParent(Class<ComponentType> componentClass, boolean includeAncestors) {
		if (transform.parent == null) {
			return null;
		}
		
		ComponentType component = transform.parent.getComponent(componentClass);
		
		if (component == null && includeAncestors) {
			return transform.parent.getComponentInParent(componentClass);
		}
		
		return component;
	}
	
	public <ComponentType extends Component> List<ComponentType> getComponentsInChildren(Class<ComponentType> componentClass) {
		if (transform.children.isEmpty()) {
			return new ArrayList<>();
		}
		
		ArrayList<ComponentType> components = new ArrayList<>();
		
		for (Transform child : transform.children) {
			ComponentType component = child.getComponent(componentClass);
			if (component != null) {
				components.add(component);
			}
		}
		
		return components;
	}
	
	/**
	 * Returns a component of a given class or null of there isn't one.
	 * @param componentClass Class of the component.
	 * @return An instance of <code>componentClass</code> that is attached to this entity.
	 */
	public <ComponentType extends Component> ComponentType getComponent(Class<ComponentType> componentClass) {
		if (components.isEmpty()) {
			return null;
		}

		// Find component that is an instance of componentClass
		ComponentType match = null;
		for (Component component : components) {
			if (componentClass.isInstance(component)) {
				match = componentClass.cast(component);
				break;
			}
		}
		
		return match;
	}
	
	/**
	 * Returns all components of a given class.
	 * @param componentClass Class of the components.
	 * @return An ArrayList of instance of <code>componentClass</code> that are attached to this entity.
	 */
	public <ComponentType extends Component> List<ComponentType> getComponents(Class<ComponentType> componentClass) {
		if (components.isEmpty()) {
			return new ArrayList<>();
		}
		
		// Find all components that are instances of componentClass
		ArrayList<ComponentType> matches = new ArrayList<>();
		for (Component component : components) {
			if (componentClass.isInstance(component)) {
				matches.add(componentClass.cast(component));
			}
		}
		
		return matches;
	}
	
	public String getName() {
		return getName(null);
	}
	
	public String getName(String defaultName) {
		if (name != null) {
			return name;
		} else if (defaultName != null) {
			return defaultName;
		} else {
			return "Entity";
		}
	}
	
	public Input getInput() {
		return application.getInput();
	}
	
	public Window getWindow() {
		return application.getWindow();
	}
	
	public Renderer getRenderer() {
		return application.getRenderer();
	}
	
	public Timer getTimer() {
		return application.getTimer();
	}
	
	public Tracker getTracker() {
		return application.getTracker();
	}
	
	public Application getApplication() {
		return application;
	}
	
	public World getWorld() {
		return world;
	}
	
	public Scene getScene() {
		return scene;
	}
	
	public CameraData getCamera() {
		return scene.getCameraData();
	}
	
	public boolean isRegistered() {
		return world.entityManager.contains(this);
	}
	
	@Override
	public int hashCode() {
		return id;
	}
	
	/**
	 * Checks whether two entities are equal.
	 * @param entity Other entity
	 * @return True if the entities are equal
	 */
	public boolean equals(Entity entity) {
		return (id == entity.id);
	}
	
	public void print() {
		String className = getClass().getSimpleName();
		int componentCount = components.size();
		String[] componentNames = new String[componentCount];
		
		for (int i = 0; i < componentCount; i++) {
			Class<? extends Component> componentClass = components.get(i).getClass();
			componentNames[i] = componentClass.getSimpleName();
		}
		
		System.out.printf("%s: %s (%s, %s) [%s] (%s) {%S}%n",
			className,
			getName(),
			transform.getGlobalX(),
			transform.getGlobalY(),
			String.join(", ", componentNames),
			componentCount,
			transform.getDepth()
		);
	}
}
