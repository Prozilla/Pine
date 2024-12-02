package dev.prozilla.pine.core.state.input;

import dev.prozilla.pine.core.Game;
import dev.prozilla.pine.common.Lifecycle;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.camera.Camera;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWScrollCallback;

import java.awt.*;
import java.util.ArrayList;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Handles the GLFW input system.
 */
public class Input implements Lifecycle {
	
	/** Array of keys that are currently pressed. */
	private final ArrayList<Integer> keysPressed;
	/** Array of keys that are down in the current frame. */
	private final ArrayList<Integer> keysDown;
	private final ArrayList<Integer> previousKeysDown;
	
	private final ArrayList<Integer> mouseButtonsPressed;
	private final ArrayList<Integer> mouseButtonsDown;
	private final ArrayList<Integer> previousMouseButtonsDown;
	
	private float scrollX;
	private float scrollY;
	private float currentScrollX;
	private float currentScrollY;
	
	private final Point cursor;
	private int cursorType;
	private Entity cursorBlocker;
	
	private GLFWKeyCallback keyCallback;
	private GLFWScrollCallback scrollCallback;
	private GLFWCursorPosCallback cursorPosCallback;
	private GLFWMouseButtonCallback mouseButtonCallback;
	
	private final Game game;
	
	/**
	 * Creates an input system.
	 */
	public Input(Game game) {
		this.game = game;
		
		keysPressed = new ArrayList<>();
		keysDown = new ArrayList<>();
		previousKeysDown = new ArrayList<>();
		
		mouseButtonsPressed = new ArrayList<>();
		mouseButtonsDown = new ArrayList<>();
		previousMouseButtonsDown = new ArrayList<>();
		
		scrollX = 0;
		scrollY = 0;
		currentScrollX = 0;
		currentScrollY = 0;
		cursor = new Point(0, 0);
		cursorType = CursorType.DEFAULT.getValue();
	}
	
	/**
	 * Initializes the input system.
	 */
	@Override
	public void init(long window) {
		glfwSetKeyCallback(window, keyCallback = new GLFWKeyCallback() {
			@Override
			public void invoke(long window, int key, int scancode, int action, int mods) {
				if (action == GLFW_PRESS) {
					keysPressed.add(key);
					keysDown.add(key);
				} else if (action == GLFW_RELEASE) {
					keysPressed.remove((Integer)key);
				}
			}
		});
		
		glfwSetScrollCallback(window, scrollCallback = new GLFWScrollCallback() {
			@Override
			public void invoke(long window, double xOffset, double yOffset) {
				scrollX = (float)xOffset;
				scrollY = (float)yOffset;
			}
		});
		
		glfwSetCursorPosCallback(window, cursorPosCallback = new GLFWCursorPosCallback() {
			@Override
			public void invoke(long window, double xPos, double yPos) {
				cursor.x = (int)xPos;
				cursor.y = (int)yPos;
			}
		});
		
		glfwSetMouseButtonCallback(window, mouseButtonCallback = new GLFWMouseButtonCallback() {
			@Override
			public void invoke(long window, int button, int action, int mods) {
				if (action == GLFW_PRESS) {
					mouseButtonsPressed.add(button);
					mouseButtonsDown.add(button);
				} else if (action == GLFW_RELEASE) {
					mouseButtonsPressed.remove((Integer)button);
				}
			}
		});
	}
	
	/**
	 * Prepare handling of input before game objects receive input.
	 */
	@Override
	public void input() {
		currentScrollX = scrollX;
		currentScrollY = scrollY;
		scrollX = 0;
		scrollY = 0;
		
		cursorType = CursorType.DEFAULT.getValue();
		cursorBlocker = null;
		
		if (!previousKeysDown.isEmpty()) {
			keysDown.removeAll(previousKeysDown);
			previousKeysDown.clear();
		}
		if (!previousMouseButtonsDown.isEmpty()) {
			mouseButtonsDown.removeAll(previousMouseButtonsDown);
			previousMouseButtonsDown.clear();
		}
	}
	
	/**
	 * Finalize input handling after all game object have received input.
	 */
	@Override
	public void update() {
		long cursor = glfwCreateStandardCursor(cursorType);
		glfwSetCursor(game.getWindow().id, cursor);
		
		previousKeysDown.addAll(keysDown);
		previousMouseButtonsDown.addAll(mouseButtonsDown);
	}
	
	/**
	 * Destroys this input system.
	 */
	@Override
	public void destroy() {
		if (keyCallback != null)
			keyCallback.free();
		if (scrollCallback != null)
			scrollCallback.free();
		if (cursorPosCallback != null)
			cursorPosCallback.free();
		if (mouseButtonCallback != null)
			mouseButtonCallback.free();
	}
	
	/**
	 * Checks whether any key in an array is pressed.
	 * @param keys Keys
	 * @return True if any key is pressed
	 */
	public boolean getAnyKey(Key[] keys) {
		if (keys == null) {
			return false;
		}
		
		boolean anyPressed = false;
		for (Key key : keys) {
			if (getKey(key)) {
				anyPressed = true;
				break;
			}
		}
		return anyPressed;
	}
	
	/**
	 * Checks whether any key in an array is pressed.
	 * @param keys Keys
	 * @return True if any key is pressed
	 */
	public boolean getAnyKey(int[] keys) {
		boolean anyPressed = false;
		for (int key : keys) {
			if (getKey(key)) {
				anyPressed = true;
				break;
			}
		}
		return anyPressed;
	}
	
	/**
	 * Checks whether a combination of keys is pressed.
	 * @param keys Keys
	 * @return True if all keys are pressed
	 */
	public boolean getKeys(Key[] keys) {
		if (keys == null) {
			return false;
		}
		
		boolean allPressed = true;
		for (Key key : keys) {
			if (!getKey(key)) {
				allPressed = false;
				break;
			}
		}
		return allPressed;
	}
	
	/**
	 * Checks whether a combination of keys is pressed.
	 * Returns true in every frame that the key is pressed.
	 * @param keys Keys
	 * @return True if all keys are pressed
	 */
	public boolean getKeys(int[] keys) {
		boolean allPressed = true;
		for (int key : keys) {
			if (!getKey(key)) {
				allPressed = false;
				break;
			}
		}
		return allPressed;
	}
	
	/**
	 * Checks whether a key is pressed.
	 * Returns true in every frame that the key is pressed.
	 * @return True if the key is pressed
	 */
	public boolean getKey(Key key) {
		if (key == null) {
			return false;
		}
		
		return getKey(key.getValue());
	}
	
	/**
	 * Checks whether a key is pressed.
	 * Returns true in every frame that the key is pressed.
	 * @param key GLFW integer value for a key
	 * @return True if the key is pressed.
	 */
	public boolean getKey(int key) {
		return keysPressed.contains(key);
	}
	
	/**
	 * Checks whether a key is down.
	 * Returns true only in the first frame that the key is pressed.
	 * @return True if the key is down in the current frame
	 */
	public boolean getKeyDown(Key key) {
		if (key == null) {
			return false;
		}
		
		return getKeyDown(key.getValue());
	}
	
	/**
	 * Checks whether a key is down.
	 * Returns true only in the first frame that the key is pressed.
	 * @param key GLFW integer value for a key
	 * @return True if the key is down in the current frame
	 */
	public boolean getKeyDown(int key) {
		return keysDown.contains(key);
	}
	
	public boolean getMouseButton(MouseButton button) {
		return getMouseButton(button.getValue());
	}
	
	public boolean getMouseButton(int button) {
		return mouseButtonsPressed.contains(button);
	}
	
	public boolean getMouseButtonDown(MouseButton button) {
		return getMouseButtonDown(button.getValue());
	}
	
	public boolean getMouseButtonDown(int button) {
		return mouseButtonsDown.contains(button);
	}
	
	/**
	 * Returns the horizontal scroll factor.
	 * @return Horizontal scroll
	 */
	public float getScrollX() {
		return currentScrollX;
	}
	
	/**
	 * Returns the vertical scroll factor.
	 * @return Vertical scroll
	 */
	public float getScrollY() {
		return currentScrollY;
	}
	
	/**
	 * Returns the position of the cursor on the screen.
	 * Returns <code>null</code> if the cursor is being blocked.
	 * @return Position of the cursor
	 */
	public Point getCursor() {
		return (cursorBlocker != null) ? null : cursor;
	}
	
	/**
	 * Returns the position of the cursor inside the world.
	 * Returns <code>null</code> if the cursor is being blocked.
	 * @return Position of the cursor
	 */
	public float[] getWorldCursor() {
		if (cursorBlocker != null) {
			return null;
		}
		
		Camera camera = game.currentScene.getCamera();
		return camera.screenToWorldPosition(getCursor());
	}
	
	/**
	 * Setter for the cursor type.
	 * This value resets after every frame.
	 * @param cursorType Cursor type
	 */
	public void setCursorType(CursorType cursorType) {
		this.cursorType = cursorType.getValue();
	}
	
	/**
	 * Setter for the cursor type.
	 * This value resets after every frame.
	 * @param cursorType GLFW cursor type integer
	 */
	public void setCursorType(int cursorType) {
		if (CursorType.isValid(cursorType)) {
			this.cursorType = cursorType;
		} else {
			System.err.println("Invalid cursor type: " + cursorType);
		}
	}
	
	/**
	 * Prevents the cursor from sending input to remaining game objects in the current frame.
	 * @param entity Game object that is blocking the cursor
	 */
	public void blockCursor(Entity entity) {
		if (cursorBlocker != null) {
			return;
		}
		
		cursorBlocker = entity;
	}
	
	/**
	 * Returns the game object that is blocking the cursor in the current frame.
	 */
	public Entity getCursorBlocker() {
		return cursorBlocker;
	}
}
