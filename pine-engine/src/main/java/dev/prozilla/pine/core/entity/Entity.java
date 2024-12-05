package dev.prozilla.pine.core.entity;

import dev.prozilla.pine.core.Application;
import dev.prozilla.pine.common.Lifecycle;
import dev.prozilla.pine.core.World;
import dev.prozilla.pine.core.component.Component;
import dev.prozilla.pine.core.component.Transform;
import dev.prozilla.pine.core.context.Window;
import dev.prozilla.pine.core.entity.camera.Camera;
import dev.prozilla.pine.core.state.Scene;
import dev.prozilla.pine.core.rendering.Renderer;
import dev.prozilla.pine.core.state.Timer;
import dev.prozilla.pine.core.state.Tracker;
import dev.prozilla.pine.core.state.input.Input;

import java.util.ArrayList;

/**
 * Represents a unique entity in the world with a list of components.
 */
public class Entity implements Lifecycle {
	
	private final int id;
	private final String name;
	protected boolean isActive;
	
	public final Transform transform;
	
	protected final World world;
	protected final Application application;
	protected final Scene scene;
	
	/** Components of this entity */
	public final ArrayList<Component> components;
	
	protected boolean initialized;
	protected boolean started;
	
	private static int lastId = 0;
	
	/**
	 * Creates a game object at the position (0, 0)
	 *
	 * @param world Reference to game
	 */
	public Entity(World world) {
		this(world, 0, 0);
	}
	
	public Entity(World world, String name) {
		this(world, name, 0, 0);
	}
	
	public Entity(World world, float x, float y) {
		this(world, null, x, y);
	}
	
	/**
	 * Creates a game object at the position (x, y)
	 *
	 * @param world Reference to game
	 * @param x     X value
	 * @param y     Y value
	 */
	public Entity(World world, String name, float x, float y) {
		this.world = world;
		this.name = name;

		application = world.application;
		scene = world.scene;
		
		id = generateId();
		
		transform = new Transform(x, y);
		components = new ArrayList<>();
		addComponent(transform);
		
		initialized = false;
		started = false;
		isActive = true;
	}
	
	/**
	 * Initializes this game object before the start of the game loop.
	 */
	@Override
	public void init(long window) throws IllegalStateException {
		if (initialized) {
			throw new IllegalStateException("Game object has already been initialized");
		}
		
		if (!transform.children.isEmpty()) {
			for (Transform child : transform.children) {
				if (child.getEntity() != null) {
					child.getEntity().init(window);
				}
			}
		}
		if (!components.isEmpty()) {
			for (Component component : components) {
				component.init(window);
			}
		}
		
		initialized = true;
	}
	
	/**
	 * Runs once at the start of the game loop and whenever this game object is activated.
	 * Runs after the initialization.
	 */
	@Override
	public void start() throws IllegalStateException {
		if (started) {
			throw new IllegalStateException("Game object has already been started");
		}
		
		if (!transform.children.isEmpty()) {
			for (Transform child : transform.children) {
				if (child.getEntity() != null && child.getEntity().isActive()) {
					child.getEntity().start();
				}
			}
		}
		if (!components.isEmpty()) {
			for (Component component : components) {
				if (component.isEnabled) {
					component.start();
				}
			}
		}
		
		started = true;
	}
	
	// TO DO: must also restart children
	private void restart() {
		started = false;
		start();
	}
	
	/**
	 * Runs every iteration of the game loop to handle input.
	 * Runs before the update method.
	 */
	@Override
	public void input(float deltaTime) {
		inputChildren(deltaTime);
		inputComponents(deltaTime);
	}
	
	/**
	 * Handle input of children in reverse order.
	 * Children are rendered in order, so children that appear on top will be
	 * at the end of the list, these need to receive input first.
	 */
	protected void inputChildren(float deltaTime) {
		if (!transform.children.isEmpty() && application.isRunning) {
			int childCount = transform.getChildCount();
			for (int i = childCount - 1; i >= 0; i--) {
				if (!application.isRunning) {
					break;
				}
				Transform child = transform.children.get(i);
				if (child.getEntity() != null && child.getEntity().isActive()) {
					child.getEntity().input(deltaTime);
				}
			}
		}
	}
	
	protected void inputComponents(float deltaTime) {
		if (!components.isEmpty() && application.isRunning) {
			for (Component component : components) {
				if (!application.isRunning) {
					break;
				}
				if (component.isEnabled) {
					component.input(deltaTime);
				}
			}
		}
	}
	
	/**
	 * Runs every iteration of the game loop to update this game object.
	 * Runs after the input method and before the render method.
	 */
	@Override
	public void update(float deltaTime) {
		updateChildren(deltaTime);
		updateComponents(deltaTime);
	}
	
	protected void updateChildren(float deltaTime) {
		if (!transform.children.isEmpty() && application.isRunning) {
			for (Transform child : transform.children) {
				if (!application.isRunning) {
					break;
				}
				if (child.getEntity() != null && child.getEntity().isActive()) {
					child.getEntity().update(deltaTime);
				}
			}
		}
	}
	
	protected void updateComponents(float deltaTime) {
		if (!components.isEmpty() && application.isRunning) {
			for (Component component : components) {
				if (!application.isRunning) {
					break;
				}
				if (component.isEnabled) {
					component.update(deltaTime);
				}
			}
		}
	}
	
	/**
	 * Renders this game object for each frame.
	 * Runs after the input and update methods.
	 */
	public void render(Renderer renderer) {
		renderChildren(renderer);
		renderComponents(renderer);
	}
	
	protected void renderChildren(Renderer renderer) {
		if (!transform.children.isEmpty() && application.isRunning) {
			for (Transform child : transform.children) {
				if (!application.isRunning) {
					break;
				}
				if (child.getEntity() != null && child.getEntity().isActive()) {
					child.getEntity().render(renderer);
				}
			}
		}
	}
	
	protected void renderComponents(Renderer renderer) {
		if (!components.isEmpty() && application.isRunning) {
			for (Component component : components) {
				if (!application.isRunning) {
					break;
				}
				if (component.isEnabled) {
					component.render(renderer);
				}
			}
		}
	}
	
	/**
	 * Removes this game object from the scene.
	 * Destroys this game object at the end of the game loop.
	 */
	public void destroy() {
		if (transform.parent != null && application.isRunning) {
			transform.parent.getEntity().removeChild(this);
		}
//		if (!transform.children.isEmpty()) {
//			for (Transform child : transform.children) {
//				child.getEntity().destroy();
//			}
//			transform.children.clear();
//		}
		if (!components.isEmpty()) {
			for (Component component : components) {
				component.destroy();
			}
			components.clear();
		}
		
		// Reset state
		initialized = false;
		started = false;
		
		// Remove all references
//		game = null;
//		world = null;
//		scene = null;
	}
	
	/**
	 * Adds a child to this game object.
	 * @param child Child object
	 */
	public Entity addChild(Entity child) throws IllegalStateException, IllegalArgumentException {
		if (child == null) {
			throw new IllegalArgumentException("Child can't be null");
		}
		
		boolean added = transform.children.add(child.transform);
		if (!added) {
			throw new IllegalStateException("GameObject is already a child");
		}
		child.transform.setParent(transform);
		
		// Initialize and start child
		if (initialized && !child.isInitialized()) {
			child.init(getWindow().id);
		}
		if (started && !child.isStarted()) {
			child.start();
		}
		
		getTracker().addGameObject();
		return child;
	}
	
	/**
	 * Adds children to this game object.
	 * @param children Child objects
	 */
	public void addChildren(Entity... children) throws IllegalStateException, IllegalArgumentException {
		for (Entity child : children) {
			addChild(child);
		}
	}
	
	/**
	 * Removes a child from this game object.
	 * @param child Child object
	 */
	public void removeChild(Entity child) throws IllegalStateException, IllegalArgumentException {
		if (child == null) {
			throw new IllegalArgumentException("Child can't be null");
		}
		
		boolean removed = transform.children.remove(child.transform);
		if (!removed) {
			throw new IllegalStateException("GameObject is not a child");
		}
		child.transform.setParent(transform);
		getTracker().removeGameObject();
	}
	
	/**
	 * Removes children from this game object.
	 * @param children Child objects
	 */
	public void removeChildren(Entity... children) throws IllegalStateException, IllegalArgumentException {
		for (Entity child : children) {
			removeChild(child);
		}
	}
	
	/**
	 * Setter for the parent object.
	 * @param parent Parent object
	 */
	public void setParent(Entity parent) {
		transform.setParent(parent.transform);
	}
	
	/**
	 * Enables/disables this game object.
	 */
	public void setActive(boolean active) {
		if (this.isActive == active) {
			return;
		}
		
		this.isActive = active;
		
//		if (active) {
//			restart();
//		}
	}
	
	public boolean isActive() {
//		return isActive && (transform.parent == null || transform.parent.getEntity().isActive());
		return isActive;
	}
	
	/**
	 * Adds a component to this game object.
	 * @param component Component
	 */
	public void addComponent(Component component) {
		world.addComponent(this, component);
	}
	
	/**
	 * Removes a component from this game object.
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
	
	public <ComponentType extends Component> ArrayList<ComponentType> getComponentsInChildren(Class<ComponentType> componentClass) {
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
	 * @return An instance of <code>componentClass</code> that is attached to this game object.
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
	 * @return An ArrayList of instance of <code>componentClass</code> that are attached to this game object.
	 */
	public <ComponentType extends Component> ArrayList<ComponentType> getComponents(Class<ComponentType> componentClass) {
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
	
	/**
	 * Generates a new unique game object ID.
	 * @return Game object ID
	 */
	public static int generateId() {
		return lastId++;
	}
	
	public int getId() {
		return id;
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
			return "GameObject";
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
	
	public World getWorld() {
		return world;
	}
	
	public Scene getScene() {
		return scene;
	}
	
	public Camera getCamera() {
		return scene.getCamera();
	}
	
	/**
	 * Checks whether two game objects are equal.
	 * @param entity Other game object
	 * @return True if the game objects are equal
	 */
	public boolean equals(Entity entity) {
		return (id == entity.getId());
	}
	
	public boolean isInitialized() {
		return this.initialized;
	}
	
	public boolean isStarted() {
		return this.started;
	}
	
	public void print() {
		String className = getClass().getSimpleName();
		int componentCount = components.size();
		String[] componentNames = new String[componentCount];
		
		for (int i = 0; i < componentCount; i++) {
			Class<? extends Component> componentClass = components.get(i).getClass();
			componentNames[i] = componentClass.getSimpleName();
		}
		
		System.out.printf("%s: %s (%s, %s) [%s] (%s)%n",
			className,
			getName(),
			transform.getGlobalX(),
			transform.getGlobalY(),
			String.join(", ", componentNames),
			componentCount
		);
	}
}
