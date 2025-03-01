package dev.prozilla.pine.core.state.input;

import dev.prozilla.pine.common.Lifecycle;
import dev.prozilla.pine.common.math.vector.Vector2f;
import dev.prozilla.pine.common.math.vector.Vector2i;
import dev.prozilla.pine.core.Application;
import dev.prozilla.pine.core.component.camera.CameraData;
import dev.prozilla.pine.core.entity.Entity;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWScrollCallback;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Handles the GLFW input system.
 */
public class Input implements Lifecycle {
	
	/** Array of keys that are currently pressed. */
	private final List<Integer> keysPressed;
	/** Array of keys that are down in the current frame. */
	private final List<Integer> keysDown;
	private final List<Integer> previousKeysDown;
	
	private final List<Integer> mouseButtonsPressed;
	private final List<Integer> mouseButtonsDown;
	private final List<Integer> previousMouseButtonsDown;
	
	private Vector2f scroll;
	private Vector2f currentScroll;
	
	private final Vector2i cursor;
	private int cursorType;
	private Entity cursorBlocker;
	
	private GLFWKeyCallback keyCallback;
	private GLFWScrollCallback scrollCallback;
	private GLFWCursorPosCallback cursorPosCallback;
	private GLFWMouseButtonCallback mouseButtonCallback;
	
	private final Application application;
	
	private static final int CURSOR_TYPE_DEFAULT = CursorType.DEFAULT.getValue();
	private static final boolean IGNORE_CURSOR_BLOCK_DEFAULT = false;
	private static final boolean STOP_PROPAGATION_DEFAULT = false;
	
	/**
	 * Creates an input system.
	 */
	public Input(Application application) {
		this.application = application;
		
		keysPressed = new ArrayList<>();
		keysDown = new ArrayList<>();
		previousKeysDown = new ArrayList<>();
		
		mouseButtonsPressed = new ArrayList<>();
		mouseButtonsDown = new ArrayList<>();
		previousMouseButtonsDown = new ArrayList<>();
		
		scroll = new Vector2f();
		currentScroll = new Vector2f();
		cursor = new Vector2i();
		cursorType = CURSOR_TYPE_DEFAULT;
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
				scroll.x = (float)xOffset;
				scroll.y = (float)yOffset;
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
	 * Prepare handling of input before input systems.
	 */
	@Override
	public void input() {
		currentScroll.x = scroll.x;
		currentScroll.y = scroll.y;
		scroll.x = 0;
		scroll.y = 0;
		
		cursorType = CURSOR_TYPE_DEFAULT;
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
	 * Finalize input handling after input systems.
	 */
	@Override
	public void update() {
		long cursor = glfwCreateStandardCursor(cursorType);
		glfwSetCursor(application.getWindow().id, cursor);
		
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
		return getKey(key, STOP_PROPAGATION_DEFAULT);
	}
	
	/**
	 * Checks whether a key is pressed.
	 * Returns true in every frame that the key is pressed.
	 * @param stopPropagation Whether to stop this key from affecting other listeners
	 * @return True if the key is pressed
	 */
	public boolean getKey(Key key, boolean stopPropagation) {
		if (key == null) {
			return false;
		}
		
		return getKey(key.getValue(), stopPropagation);
	}
	
	/**
	 * Checks whether a key is pressed.
	 * Returns true in every frame that the key is pressed.
	 * @param key GLFW integer value for a key
	 * @return True if the key is pressed.
	 */
	public boolean getKey(int key) {
		return getKey(key, STOP_PROPAGATION_DEFAULT);
	}
	
	/**
	 * Checks whether a key is pressed.
	 * Returns true in every frame that the key is pressed.
	 * @param key GLFW integer value for a key
	 * @param stopPropagation Whether to stop this key from affecting other listeners
	 * @return True if the key is pressed.
	 */
	public boolean getKey(int key, boolean stopPropagation) {
		boolean pressed = keysPressed.contains(key);
		
		if (pressed && stopPropagation) {
			Integer keyInt = key;
			keysPressed.remove(keyInt);
			keysDown.remove(keyInt);
		}
		
		return pressed;
	}
	
	/**
	 * Checks whether a key is down.
	 * Returns true only in the first frame that the key is pressed.
	 * @return True if the key is down in the current frame
	 */
	public boolean getKeyDown(Key key) {
		return getKeyDown(key, STOP_PROPAGATION_DEFAULT);
	}
	
	/**
	 * Checks whether a key is down.
	 * Returns true only in the first frame that the key is pressed.
	 * @param stopPropagation Whether to stop this key from affecting other listeners
	 * @return True if the key is down in the current frame
	 */
	public boolean getKeyDown(Key key, boolean stopPropagation) {
		if (key == null) {
			return false;
		}
		
		return getKeyDown(key.getValue(), stopPropagation);
	}
	
	/**
	 * Checks whether a key is down.
	 * Returns true only in the first frame that the key is pressed.
	 * @param key GLFW integer value for a key
	 * @return True if the key is down in the current frame
	 */
	public boolean getKeyDown(int key) {
		return getKeyDown(key, STOP_PROPAGATION_DEFAULT);
	}
	
	/**
	 * Checks whether a key is down.
	 * Returns true only in the first frame that the key is pressed.
	 * @param key GLFW integer value for a key
	 * @param stopPropagation Whether to stop this key from affecting other listeners
	 * @return True if the key is down in the current frame
	 */
	public boolean getKeyDown(int key, boolean stopPropagation) {
		boolean down = keysDown.contains(key);
		
		if (down && stopPropagation) {
			Integer keyInt = key;
			keysPressed.remove(keyInt);
			keysDown.remove(keyInt);
		}
		
		return down;
	}
	
	public boolean getMouseButton(MouseButton button) {
		return getMouseButton(button, STOP_PROPAGATION_DEFAULT);
	}
	
	public boolean getMouseButton(MouseButton button, boolean stopPropagation) {
		return getMouseButton(button.getValue(), stopPropagation);
	}
	
	public boolean getMouseButton(int button) {
		return getMouseButton(button, STOP_PROPAGATION_DEFAULT);
	}
	
	public boolean getMouseButton(int button, boolean stopPropagation) {
		boolean pressed = mouseButtonsPressed.contains(button);
		
		if (pressed && stopPropagation) {
			Integer buttonInt = button;
			mouseButtonsPressed.remove(buttonInt);
			mouseButtonsDown.remove(buttonInt);
		}
		
		return pressed;
	}
	
	public boolean getMouseButtonDown(MouseButton button) {
		return getMouseButtonDown(button, STOP_PROPAGATION_DEFAULT);
	}
	
	public boolean getMouseButtonDown(MouseButton button, boolean stopPropagation) {
		return getMouseButtonDown(button.getValue(), stopPropagation);
	}
	
	public boolean getMouseButtonDown(int button) {
		return getMouseButtonDown(button, STOP_PROPAGATION_DEFAULT);
	}
	
	public boolean getMouseButtonDown(int button, boolean stopPropagation) {
		boolean down = mouseButtonsDown.contains(button);
		
		if (down && stopPropagation) {
			Integer buttonInt = button;
			mouseButtonsPressed.remove(buttonInt);
			mouseButtonsDown.remove(buttonInt);
		}
		
		return down;
	}
	
	/**
	 * Returns the horizontal scroll factor.
	 * @return Horizontal scroll
	 */
	public float getScrollX() {
		return currentScroll.x;
	}
	
	/**
	 * Returns the vertical scroll factor.
	 * @return Vertical scroll
	 */
	public float getScrollY() {
		return currentScroll.y;
	}
	
	/**
	 * Returns the position of the cursor on the screen.
	 * Returns <code>null</code> if the cursor is being blocked.
	 * @return Position of the cursor
	 */
	public Vector2i getCursor() {
		return getCursor(IGNORE_CURSOR_BLOCK_DEFAULT);
	}
	
	/**
	 * Returns the position of the cursor on the screen.
	 * Returns <code>null</code> if the cursor is being blocked, unless blocks are being ignored.
	 * @param ignoreBlock Whether to ignore blocks.
	 * @return Position of the cursor
	 */
	public Vector2i getCursor(boolean ignoreBlock) {
		return (!ignoreBlock && cursorBlocker != null) ? null : cursor;
	}
	
	/**
	 * Returns the position of the cursor inside the world.
	 * Returns <code>null</code> if the cursor is being blocked.
	 * @return Position of the cursor
	 */
	public Vector2f getWorldCursor() {
		return getWorldCursor(IGNORE_CURSOR_BLOCK_DEFAULT);
	}
	
	/**
	 * Returns the position of the cursor inside the world.
	 * Returns <code>null</code> if the cursor is being blocked, unless blocks are being ignored.
	 * @param ignoreBlock Whether to ignore blocks.
	 * @return Position of the cursor
	 */
	public Vector2f getWorldCursor(boolean ignoreBlock) {
		if (!ignoreBlock && cursorBlocker != null) {
			return null;
		}
		
		CameraData camera = application.currentScene.getCameraData();
		return camera.screenToWorldPosition(getCursor(ignoreBlock));
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
	 * Prevents the cursor from sending input to remaining entities in the current frame.
	 * @param entity Entity that is blocking the cursor
	 */
	public void blockCursor(Entity entity) {
		blockCursor(entity, false);
	}
	
	/**
	 * Prevents the cursor from sending input to remaining entities in the current frame.
	 * @param entity Entity that is blocking the cursor
	 * @param override Whether to override any existing blocking entity.
	 */
	public void blockCursor(Entity entity, boolean override) {
		if ((!override && cursorBlocker != null) || !entity.isActive()) {
			return;
		}
		
		cursorBlocker = entity;
	}
	
	public boolean isCursorBlocked() {
		return cursorBlocker != null;
	}
	
	/**
	 * Returns the entity that is blocking the cursor in the current frame.
	 */
	public Entity getCursorBlocker() {
		return cursorBlocker;
	}
}
