package dev.prozilla.pine.core.component;

import dev.prozilla.pine.common.Lifecycle;
import dev.prozilla.pine.core.object.GameObject;
import dev.prozilla.pine.core.rendering.Renderer;

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
	
	/** The name of this component. */
	public String name;
	private final int id;
	
	/** The game object that this component is attached to. */
	protected GameObject gameObject;
	
	private static int lastId = 0;
	
	public Component() {
		this("Component");
	}
	
	public Component(String name) {
		this.name = name;
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
	 * Attaches this component to a game object.
	 * @param gameObject Game object
	 */
	public void attach(GameObject gameObject) {
		this.gameObject = gameObject;
	}
	
	/**
	 * Removes this component from the game object it's attached to.
	 */
	public void remove() {
		if (gameObject != null) {
			gameObject.removeComponent(this);
			gameObject = null;
			
			destroy();
		}
	}
	
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
	 * Getter for the game object this component is attached to.
	 * @return Game object
	 */
	public GameObject getGameObject() {
		return gameObject;
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
	
	public boolean equals(Component component) {
		return component.getId() == id;
	}
}
