package dev.prozilla.pine.core.component;

import dev.prozilla.pine.common.Lifecycle;
import dev.prozilla.pine.core.context.Window;
import dev.prozilla.pine.core.object.GameObject;
import dev.prozilla.pine.core.object.World;
import dev.prozilla.pine.core.object.camera.Camera;
import dev.prozilla.pine.core.rendering.Renderer;
import dev.prozilla.pine.core.state.Timer;
import dev.prozilla.pine.core.state.Tracker;
import dev.prozilla.pine.core.state.input.Input;

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
	
	/** The game object that this component is attached to. */
	protected GameObject gameObject;
	
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
	
	public String getName() {
		return "Component";
	}
	
	public Input getInput() {
		return gameObject.getInput();
	}
	
	public Window getWindow() {
		return gameObject.getWindow();
	}
	
	public Renderer getRenderer() {
		return gameObject.getRenderer();
	}
	
	public Timer getTimer() {
		return gameObject.getTimer();
	}
	
	public Tracker getTracker() {
		return gameObject.getTracker();
	}
	
	public World getWorld() {
		return gameObject.getWorld();
	}
	
	public Camera getCamera() {
		return gameObject.getCamera();
	}
	
	public boolean equals(Component component) {
		return component.getId() == id;
	}
}
