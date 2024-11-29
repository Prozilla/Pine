package dev.prozilla.pine.core.object;

import dev.prozilla.pine.core.Game;
import dev.prozilla.pine.common.Lifecycle;
import dev.prozilla.pine.core.component.Component;
import dev.prozilla.pine.core.context.Window;
import dev.prozilla.pine.core.object.camera.Camera;
import dev.prozilla.pine.core.state.Scene;
import dev.prozilla.pine.core.rendering.Renderer;
import dev.prozilla.pine.core.state.Timer;
import dev.prozilla.pine.core.state.Tracker;
import dev.prozilla.pine.core.state.input.Input;

import java.util.ArrayList;

/**
 * Represents a game object
 */
public class GameObject implements Lifecycle {
	
	private final int id;
	protected boolean active;
	
	/** X position value */
	public float x;
	/** Y position value */
	public float y;
	/** Rotation in degrees */
	public float rotation;
	
	/** Reference to the game */
	public final Game game;
	public Scene scene;
	
	/** Children of this game object */
	protected final ArrayList<GameObject> children;
	/** Parent of this game object */
	protected GameObject parent;
	
	/** Components of this game object */
	protected final ArrayList<Component> components;
	
	protected boolean initialized;
	protected boolean started;
	
	private static int lastId = 0;
	
	/**
	 * Creates a game object at the position (0, 0)
	 * @param game Reference to game
	 */
	public GameObject(Game game) {
		this(game, 0, 0);
	}
	
	/**
	 * Creates a game object at the position (x, y)
	 * @param game Reference to game
	 * @param x X value
	 * @param y Y value
	 */
	public GameObject(Game game, float x, float y) {
		this.game = game;
		this.x = x;
		this.y = y;
		
		rotation = 0;
		
		children = new ArrayList<>();
		components = new ArrayList<>();
		id = generateId();
		
		initialized = false;
		started = false;
		active = true;
	}
	
	/**
	 * Initializes this game object before the start of the game loop.
	 */
	@Override
	public void init(long window) throws IllegalStateException {
		if (initialized) {
			throw new IllegalStateException("Game object has already been initialized");
		}
		
		if (scene == null && parent != null) {
			scene = parent.scene;
		}
		
		if (!children.isEmpty()) {
			for (GameObject child : children) {
				child.init(window);
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
		
		if (!children.isEmpty()) {
			for (GameObject child : children) {
				if (child.isActive()) {
					child.start();
				}
			}
		}
		if (!components.isEmpty()) {
			for (Component component : components) {
				if (component.enabled) {
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
		if (!children.isEmpty() && game.running) {
			int childCount = children.size();
			for (int i = childCount - 1; i >= 0; i--) {
				if (!game.running) {
					break;
				}
				GameObject child = children.get(i);
				if (child.isActive()) {
					child.input(deltaTime);
				}
			}
		}
	}
	
	protected void inputComponents(float deltaTime) {
		if (!components.isEmpty() && game.running) {
			for (Component component : components) {
				if (!game.running) {
					break;
				}
				if (component.enabled) {
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
		if (!children.isEmpty() && game.running) {
			for (GameObject child : children) {
				if (!game.running) {
					break;
				}
				if (child.isActive()) {
					child.update(deltaTime);
				}
			}
		}
	}
	
	protected void updateComponents(float deltaTime) {
		if (!components.isEmpty() && game.running) {
			for (Component component : components) {
				if (!game.running) {
					break;
				}
				if (component.enabled) {
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
		if (!children.isEmpty() && game.running) {
			for (GameObject child : children) {
				if (!game.running) {
					break;
				}
				if (child.isActive()) {
					child.render(renderer);
				}
			}
		}
	}
	
	protected void renderComponents(Renderer renderer) {
		if (!components.isEmpty() && game.running) {
			for (Component component : components) {
				if (!game.running) {
					break;
				}
				if (component.enabled) {
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
		if (parent != null && game.running) {
			parent.removeChild(this);
		}
		if (!children.isEmpty()) {
			for (GameObject child : children) {
				child.destroy();
			}
			children.clear();
		}
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
		scene = null;
		parent = null;
	}
	
	/**
	 * Adds a child to this game object.
	 * @param child Child object
	 */
	public GameObject addChild(GameObject child) throws IllegalStateException, IllegalArgumentException {
		if (child == null) {
			throw new IllegalArgumentException("Child can't be null");
		}
		
		boolean added = this.children.add(child);
		if (!added) {
			throw new IllegalStateException("GameObject is already a child");
		}
		child.setParent(this);
		
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
	public void addChildren(GameObject... children) throws IllegalStateException, IllegalArgumentException {
		for (GameObject child : children) {
			addChild(child);
		}
	}
	
	/**
	 * Removes a child from this game object.
	 * @param child Child object
	 */
	public void removeChild(GameObject child) throws IllegalStateException, IllegalArgumentException {
		if (child == null) {
			throw new IllegalArgumentException("Child can't be null");
		}
		
		boolean removed = this.children.remove(child);
		if (!removed) {
			throw new IllegalStateException("GameObject is not a child");
		}
		child.setParent(null);
		getTracker().removeGameObject();
	}
	
	/**
	 * Removes children from this game object.
	 * @param children Child objects
	 */
	public void removeChildren(GameObject... children) throws IllegalStateException, IllegalArgumentException {
		for (GameObject child : children) {
			removeChild(child);
		}
	}
	
	public ArrayList<GameObject> getChildren() {
		return children;
	}
	
	/**
	 * Setter for the parent object.
	 * @param parent Parent object
	 */
	public void setParent(GameObject parent) {
		this.parent = parent;
		
		if (parent != null) {
			scene = parent.scene;
		} else {
			scene = null;
		}
	}
	
	/**
	 * Getter for the parent object.
	 * @return Parent object
	 */
	public GameObject getParent() {
		return parent;
	}
	
	/**
	 * Enables/disables this game object.
	 */
	public void setActive(boolean active) {
		if (this.active == active) {
			return;
		}
		
		this.active = active;
		
//		if (active) {
//			restart();
//		}
	}
	
	public boolean isActive() {
		return active && (parent == null || parent.isActive());
	}
	
	/**
	 * Moves this game object by an x and y amount.
	 * @param x Amount to move on the x-axis
	 * @param y Amount to move on the y-axis
	 */
	public void moveBy(float x, float y) {
		this.x += x;
		this.y += y;
	}
	
	/**
	 * Moves this game object to an XY-coordinate.
	 * @param x X value
	 * @param y Y value
	 */
	public void moveTo(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Adds a component to this game object.
	 * @param component Component
	 */
	public void addComponent(Component component) {
		if (!components.add(component)) {
			return;
		}
		
		component.attach(this);
		
		if (initialized) {
			component.init(getWindow().id);
		}
		if (started) {
			component.start();
		}
	}
	
	/**
	 * Removes a component from this game object.
	 * @param component Component
	 */
	public void removeComponent(Component component) {
		if (!components.remove(component)) {
			return;
		}
		
		component.remove();
	}
	
	public <ComponentType extends Component> ComponentType getComponentInParent(Class<ComponentType> componentClass) {
		return getComponentInParent(componentClass, true);
	}
	
	public <ComponentType extends Component> ComponentType getComponentInParent(Class<ComponentType> componentClass, boolean includeAncestors) {
		if (parent == null) {
			return null;
		}
		
		ComponentType component = parent.getComponent(componentClass);
		
		if (component == null && includeAncestors) {
			return parent.getComponentInParent(componentClass);
		}
		
		return component;
	}
	
	public <ComponentType extends Component> ArrayList<ComponentType> getComponentsInChildren(Class<ComponentType> componentClass) {
		if (children.isEmpty()) {
			return null;
		}
		
		ArrayList<ComponentType> components = new ArrayList<>();
		
		for (GameObject child : children) {
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
		return "GameObject";
	}
	
	public Input getInput() {
		return game.getInput();
	}
	
	public Window getWindow() {
		return game.getWindow();
	}
	
	public Renderer getRenderer() {
		return game.getRenderer();
	}
	
	public Timer getTimer() {
		return game.getTimer();
	}
	
	public Tracker getTracker() {
		return game.getTracker();
	}
	
	public World getWorld() {
		return scene.getWorld();
	}
	
	public Camera getCamera() {
		return scene.getCamera();
	}
	
	/**
	 * Checks whether two game objects are equal.
	 * @param gameObject Other game object
	 * @return True if the game objects are equal
	 */
	public boolean equals(GameObject gameObject) {
		return (id == gameObject.getId());
	}
	
	public boolean isInitialized() {
		return this.initialized;
	}
	
	public boolean isStarted() {
		return this.started;
	}
	
	public void print() {
		System.out.printf("%s: %s (%s, %s)%n", getClass().getName(), getName(), x, y);
	}
}
