package dev.prozilla.pine.core.state.input;

import dev.prozilla.pine.common.asset.image.Image;
import dev.prozilla.pine.common.event.Event;
import dev.prozilla.pine.common.event.EventListener;
import dev.prozilla.pine.common.event.SimpleEventDispatcher;
import dev.prozilla.pine.common.lifecycle.Destructible;
import dev.prozilla.pine.common.lifecycle.Initializable;
import dev.prozilla.pine.common.logging.Logger;
import dev.prozilla.pine.common.lwjgl.GLFWUtils;
import dev.prozilla.pine.common.math.vector.Vector2f;
import dev.prozilla.pine.common.math.vector.Vector2i;
import dev.prozilla.pine.common.util.checks.Checks;
import dev.prozilla.pine.common.util.function.Callback;
import dev.prozilla.pine.core.Application;
import dev.prozilla.pine.core.Window;
import dev.prozilla.pine.core.component.camera.CameraData;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.state.input.gamepad.Gamepad;
import dev.prozilla.pine.core.state.input.gamepad.GamepadEventType;
import dev.prozilla.pine.core.state.input.gamepad.GamepadInput;
import org.lwjgl.glfw.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Handles the GLFW input system.
 */
public class Input implements Initializable, Destructible {
	
	// Keyboard
	/** Array of keys that are currently pressed. */
	private final Set<Integer> keysPressed;
	/** Array of keys that are down in the current frame. */
	private final Set<Integer> keysDown;
	/** Array of keys that are repeated in the current frame. */
	private final Set<Integer> keysRepeated;
	private final Set<Integer> previousKeysDown;
	private final List<TextListener> textListeners;
	
	// Mouse
	private final Set<Integer> mouseButtonsPressed;
	private final Set<Integer> mouseButtonsDown;
	private final Set<Integer> previousMouseButtonsDown;
	
	private final Vector2f scroll;
	private final Vector2f currentScroll;
	
	private final Vector2i cursorPosition;
	private Entity cursorBlocker;
	private int cursorType;
	private Image cursorImage;
	private Image previousCursorImage;
	private final Vector2i cursorImageOffset;
	private long cursorHandle;
	private long previousCursorHandle;
	
	// Gamepad
	private final Gamepad[] gamepads;
	/** Used when no gamepad is connected. */
	private final GamepadInput fallbackGamepad;
	public final SimpleEventDispatcher<GamepadEventType, Integer> gamepadEvents;
	
	// Callbacks
	private GLFWKeyCallback keyCallback;
	private GLFWCharCallback charCallback;
	private GLFWScrollCallback scrollCallback;
	private GLFWCursorPosCallback cursorPosCallback;
	private GLFWMouseButtonCallback mouseButtonCallback;
	private GLFWJoystickCallback joystickCallback;
	
	private final Application application;
	private final Window window;
	private final Logger logger;
	
	// Constants
	public static final int CURSOR_TYPE_DEFAULT = CursorType.DEFAULT.getValue();
	public static final int CURSOR_MODE_DEFAULT = CursorMode.NORMAL.getValue();
	public static final boolean IGNORE_CURSOR_BLOCK_DEFAULT = false;
	public static final boolean STOP_PROPAGATION_DEFAULT = false;
	public static final int DEFAULT_GAMEPAD_ID = 0;
	
	@FunctionalInterface
	public interface TextListener {
		
		void handle(char character);
		
	}
	
	/**
	 * Creates an input system.
	 */
	public Input(Application application) {
		this.application = application;
		window = application.getWindow();
		logger = application.getLogger();
		
		keysPressed = new HashSet<>();
		keysDown = new HashSet<>();
		keysRepeated = new HashSet<>();
		previousKeysDown = new HashSet<>();
		textListeners = new ArrayList<>();
		
		mouseButtonsPressed = new HashSet<>();
		mouseButtonsDown = new HashSet<>();
		previousMouseButtonsDown = new HashSet<>();
		
		scroll = new Vector2f();
		currentScroll = new Vector2f();
		cursorPosition = new Vector2i();
		cursorType = CURSOR_TYPE_DEFAULT;
		cursorImageOffset = new Vector2i();
		
		gamepads = new Gamepad[GLFW_JOYSTICK_LAST + 1];
		fallbackGamepad = new GamepadInput() {
			@Override
			public float getAxis(int axis) {
				return 0;
			}
			
			@Override
			public boolean getButton(int button) {
				return false;
			}
			
			@Override
			public boolean getButtonDown(int button) {
				return false;
			}
		};
		gamepadEvents = new SimpleEventDispatcher<>();
	}
	
	/**
	 * Initializes the input system.
	 */
	@Override
	public void init() {
		glfwSetKeyCallback(window.getId(), keyCallback = new GLFWKeyCallback() {
			@Override
			public void invoke(long window, int key, int scancode, int action, int mods) {
				if (action == GLFW_PRESS) {
					keysPressed.add(key);
					keysDown.add(key);
				} else if (action == GLFW_RELEASE) {
					Integer keyInt = key;
					keysPressed.remove(keyInt);
					keysRepeated.remove(keyInt);
				} else if (action == GLFW_REPEAT) {
					keysRepeated.add(key);
				}
			}
		});
		
		glfwSetCharCallback(window.getId(), charCallback = new GLFWCharCallback() {
			@Override
			public void invoke(long window, int codepoint) {
				char character = (char)codepoint;
				for (TextListener listener : textListeners) {
					listener.handle(character);
				}
			}
		});
		
		glfwSetScrollCallback(window.getId(), scrollCallback = new GLFWScrollCallback() {
			@Override
			public void invoke(long window, double xOffset, double yOffset) {
				scroll.x = (float)xOffset;
				scroll.y = (float)yOffset;
			}
		});
		
		glfwSetCursorPosCallback(window.getId(), cursorPosCallback = new GLFWCursorPosCallback() {
			@Override
			public void invoke(long window, double xPos, double yPos) {
				cursorPosition.x = (int)xPos;
				cursorPosition.y = (int)yPos;
			}
		});
		
		glfwSetMouseButtonCallback(window.getId(), mouseButtonCallback = new GLFWMouseButtonCallback() {
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
		
		glfwSetJoystickCallback(joystickCallback = new GLFWJoystickCallback() {
			@Override
			public void invoke(int gamepadId, int event) {
				if (event == GLFW_CONNECTED) {
					gamepads[gamepadId] = new Gamepad(gamepadId);
					gamepadEvents.invoke(GamepadEventType.CONNECT, gamepadId);
				} else if (event == GLFW_DISCONNECTED) {
					gamepads[gamepadId].destroy();
					gamepads[gamepadId] = null;
					gamepadEvents.invoke(GamepadEventType.DISCONNECT, gamepadId);
				}
			}
		});
		
		for (int i = 0; i < gamepads.length; i++) {
			if (glfwJoystickPresent(i)) {
				gamepads[i] = new Gamepad(i);
			}
		}
		
		logger.log("Input initialized");
	}
	
	/**
	 * Prepare handling of input before input systems.
	 */
	public void input() {
		// Reset scroll
		currentScroll.x = scroll.x;
		currentScroll.y = scroll.y;
		scroll.x = 0;
		scroll.y = 0;
		
		// Reset cursor
		cursorType = CURSOR_TYPE_DEFAULT;
		cursorImage = null;
		cursorBlocker = null;
		
		// Reset keys
		if (!previousKeysDown.isEmpty()) {
			keysDown.removeAll(previousKeysDown);
			previousKeysDown.clear();
		}
		if (!previousMouseButtonsDown.isEmpty()) {
			mouseButtonsDown.removeAll(previousMouseButtonsDown);
			previousMouseButtonsDown.clear();
		}
		
		for (Gamepad gamepad : gamepads) {
			if (gamepad != null) {
				gamepad.input();
			}
		}
	}
	
	/**
	 * Finalize input handling after input systems.
	 */
	public void update() {
		if (cursorImage != null) {
			if (cursorImage != previousCursorImage) {
				cursorHandle = glfwCreateCursor(cursorImage.toGLFWImage(), cursorImageOffset.x, cursorImageOffset.y);
			}
			previousCursorImage = cursorImage;
		} else {
			cursorHandle = glfwCreateStandardCursor(cursorType);
			previousCursorImage = null;
		}
		
		if (cursorHandle != previousCursorHandle) {
			glfwSetCursor(window.getId(), cursorHandle);
			previousCursorHandle = cursorHandle;
		}
		
		previousKeysDown.addAll(keysDown);
		previousMouseButtonsDown.addAll(mouseButtonsDown);
	}
	
	/**
	 * Destroys this input system.
	 */
	@Override
	public void destroy() {
		GLFWUtils.free(keyCallback, charCallback, scrollCallback, cursorPosCallback, mouseButtonCallback, joystickCallback);
		for (Gamepad gamepad : gamepads) {
			if (gamepad != null) {
				gamepad.destroy();
			}
		}
		gamepadEvents.destroy();
		textListeners.clear();
	}
	
	//region --- Keyboard ---
	
	/**
	 * Checks whether any key in an array is pressed.
	 * @param keys Keys
	 * @return True if any key is pressed
	 */
	public boolean getAnyKey(Key... keys) {
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
	public boolean getAnyKey(int... keys) {
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
	public boolean getKeys(Key... keys) {
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
	public boolean getKeys(int... keys) {
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
		return handleKey(keysPressed, key, stopPropagation);
	}
	
	public boolean getKeyRepeated(Key key) {
		return getKeyRepeated(key, STOP_PROPAGATION_DEFAULT);
	}
	
	public boolean getKeyRepeated(Key key, boolean stopPropagation) {
		if (key == null) {
			return false;
		}
		return getKeyRepeated(key.getValue(), stopPropagation);
	}
	
	public boolean getKeyRepeated(int key) {
		return getKeyRepeated(key, STOP_PROPAGATION_DEFAULT);
	}
	
	public boolean getKeyRepeated(int key, boolean stopPropagation) {
		return handleKey(keysDown.contains(key) || keysRepeated.contains(key), key, stopPropagation);
	}
	
	/**
	 * Checks whether a key is down.
	 * Returns {@code true} in the first frame that the key is pressed
	 * and in every frame the key is being repeated.
	 * @return {@code true} if the key is down.
	 */
	public boolean getKeyDown(Key key) {
		return getKeyDown(key, STOP_PROPAGATION_DEFAULT);
	}
	
	/**
	 * Checks whether a key is down.
	 * Returns {@code true} in the first frame that the key is pressed
	 * and in every frame the key is being repeated.
	 * @param stopPropagation Whether to stop this key from affecting other listeners
	 * @return {@code true} if the key is down.
	 */
	public boolean getKeyDown(Key key, boolean stopPropagation) {
		if (key == null) {
			return false;
		}
		
		return getKeyDown(key.getValue(), stopPropagation);
	}
	
	/**
	 * Checks whether a key is down.
	 * Returns {@code true} in the first frame that the key is pressed
	 * and in every frame the key is being repeated.
	 * @param key GLFW integer value for a key
	 * @return {@code true} if the key is down.
	 */
	public boolean getKeyDown(int key) {
		return getKeyDown(key, STOP_PROPAGATION_DEFAULT);
	}
	
	/**
	 * Checks whether a key is down.
	 * Returns {@code true} in the first frame that the key is pressed
	 * and in every frame the key is being repeated.
	 * @param key GLFW integer value for a key
	 * @param stopPropagation Whether to stop this key from affecting other listeners
	 * @return {@code true} if the key is down.
	 */
	public boolean getKeyDown(int key, boolean stopPropagation) {
		return handleKey(keysDown, key, stopPropagation);
	}
	
	private boolean handleKey(Set<Integer> activeKeys, int key, boolean stopPropagation) {
		return handleKey(activeKeys.contains(key), key, stopPropagation);
	}
	
	private boolean handleKey(boolean active, int key, boolean stopPropagation) {
		if (active && stopPropagation) {
			stopKeyPropagation(key);
		}
		return active;
	}
	
	private void stopKeyPropagation(Integer key) {
		keysPressed.remove(key);
		keysDown.remove(key);
		keysRepeated.remove(key);
	}
	
	public void addTextListener(TextListener listener) {
		textListeners.add(listener);
	}
	
	public void removeTextListener(TextListener listener) {
		textListeners.remove(listener);
	}
	
	//endregion Keyboard
	
	//region --- Gamepad ---
	
	/**
	 * Returns the first gamepad if it is connected,
	 * otherwise returns a fallback gamepad which returns {@code 0f} for all axes and {@code false} for all buttons.
	 * @return The first gamepad or a fallback gamepad
	 */
	public GamepadInput getGamepad() {
		return isGamepadConnected(DEFAULT_GAMEPAD_ID) ? getGamepad(DEFAULT_GAMEPAD_ID) : fallbackGamepad;
	}
	
	/**
	 * Returns the gamepad that matches the given ID.
	 * @return The gamepad with the given ID.
	 */
	public GamepadInput getGamepad(int id) {
		checkGamepadId(id);
		GamepadInput gamepad = gamepads[id];
		if (gamepad == null) {
			throw new IllegalArgumentException(String.format("Gamepad %s is not connected", id));
		}
		return gamepad;
	}
	
	/**
	 * Checks if the gamepad that matches the given ID is connected.
	 * @return {@code true} if the gamepad with the given ID is connected.
	 */
	public boolean isGamepadConnected(int id) {
		checkGamepadId(id);
		return gamepads[id] != null;
	}
	
	public EventListener<Event<GamepadEventType, Integer>> onGamepadConnect(int id, Callback callback) {
		return onGamepadEvent(id, callback, GamepadEventType.CONNECT);
	}
	
	public EventListener<Event<GamepadEventType, Integer>> onGamepadDisconnect(int id, Callback callback) {
		return onGamepadEvent(id, callback, GamepadEventType.DISCONNECT);
	}
	
	private EventListener<Event<GamepadEventType, Integer>> onGamepadEvent(int id, Callback callback, GamepadEventType eventType) {
		checkGamepadId(id);
		return gamepadEvents.addTargetedListener(eventType, id, (event) -> callback.run());
	}
	
	private void checkGamepadId(int id) {
		Checks.isInRange(id, 0, gamepads.length - 1, "Invalid gamepad ID: " + id);
	}
	
	//endregion Gamepad
	
	//region --- Mouse ---
	
	/**
	 * Checks whether a mouse button is pressed.
	 * @return True if the key is pressed.
	 */
	public boolean getMouseButton(MouseButton button) {
		return getMouseButton(button, STOP_PROPAGATION_DEFAULT);
	}
	
	/**
	 * Checks whether a mouse button is pressed.
	 * @param stopPropagation Whether to stop this mouse button from affecting other listeners
	 * @return True if the key is pressed.
	 */
	public boolean getMouseButton(MouseButton button, boolean stopPropagation) {
		if (button == null) {
			return false;
		}
		return getMouseButton(button.getValue(), stopPropagation);
	}
	
	/**
	 * Checks whether a mouse button is pressed.
	 * @param button GLFW integer value for a mouse button
	 * @return True if the key is pressed.
	 */
	public boolean getMouseButton(int button) {
		return getMouseButton(button, STOP_PROPAGATION_DEFAULT);
	}
	
	/**
	 * Checks whether a mouse button is pressed.
	 * @param button GLFW integer value for a mouse button
	 * @param stopPropagation Whether to stop this mouse button from affecting other listeners
	 * @return True if the key is pressed.
	 */
	public boolean getMouseButton(int button, boolean stopPropagation) {
		boolean pressed = mouseButtonsPressed.contains(button);
		
		if (pressed && stopPropagation) {
			Integer buttonInt = button;
			mouseButtonsPressed.remove(buttonInt);
			mouseButtonsDown.remove(buttonInt);
		}
		
		return pressed;
	}
	
	/**
	 * Checks whether a mouse button is down.
	 * @return True if the mouse button is down in the current frame
	 */
	public boolean getMouseButtonDown(MouseButton button) {
		return getMouseButtonDown(button, STOP_PROPAGATION_DEFAULT);
	}
	
	/**
	 * Checks whether a mouse button is down.
	 * @param stopPropagation Whether to stop this mouse button from affecting other listeners
	 * @return True if the mouse button is down in the current frame
	 */
	public boolean getMouseButtonDown(MouseButton button, boolean stopPropagation) {
		return getMouseButtonDown(button.getValue(), stopPropagation);
	}
	
	/**
	 * Checks whether a mouse button is down.
	 * @param button GLFW integer value for a mouse button
	 * @return True if the mouse button is down in the current frame
	 */
	public boolean getMouseButtonDown(int button) {
		return getMouseButtonDown(button, STOP_PROPAGATION_DEFAULT);
	}
	
	/**
	 * Checks whether a mouse button is down.
	 * @param button GLFW integer value for a mouse button
	 * @param stopPropagation Whether to stop this mouse button from affecting other listeners
	 * @return True if the mouse button is down in the current frame
	 */
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
	 * @return Horizontal scroll factor.
	 */
	public float getScrollX() {
		return currentScroll.x;
	}
	
	/**
	 * @return Vertical scroll factor.
	 */
	public float getScrollY() {
		return currentScroll.y;
	}
	
	/**
	 * @return Position of the cursor on the screen, or <code>null</code> if the cursor is being blocked.
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
		return (!ignoreBlock && cursorBlocker != null) ? null : cursorPosition;
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
		
		CameraData camera = application.getCurrentScene().getCameraData();
		return camera.screenToWorldPosition(getCursor(ignoreBlock));
	}
	
	/**
	 * Setter for the cursor type.
	 * This value resets after every frame.
	 * @param cursorType Cursor type
	 */
	public void setCursorType(CursorType cursorType) {
		this.cursorType = cursorType.getValue();
		cursorImage = null;
	}
	
	/**
	 * Setter for the cursor type.
	 * This value resets after every frame.
	 * @param cursorType GLFW cursor type integer
	 */
	public void setCursorType(int cursorType) {
		if (CursorType.isValid(cursorType)) {
			this.cursorType = cursorType;
			cursorImage = null;
		} else {
			System.err.println("Invalid cursor type: " + cursorType);
		}
	}
	
	public void setCursorImage(Image image) {
		setCursorImage(image, 0, 0);
	}
	
	public void setCursorImage(Image image, Vector2i offset) {
		setCursorImage(image, offset.x, offset.y);
	}
	
	public void setCursorImage(Image image, int offsetX, int offsetY) {
		cursorImage = image;
		cursorImageOffset.x = offsetX;
		cursorImageOffset.y = offsetY;
	}
	
	/**
	 * Sets the cursor mode to {@link CursorMode#NORMAL}.
	 */
	public void showCursor() {
		setCursorMode(CursorMode.NORMAL);
	}
	
	/**
	 * Sets the cursor mode to {@link CursorMode#HIDDEN}.
	 */
	public void hideCursor() {
		setCursorMode(CursorMode.HIDDEN);
	}
	
	/**
	 * Sets the cursor mode to {@link CursorMode#DISABLED}.
	 */
	public void disableCursor() {
		setCursorMode(CursorMode.DISABLED);
	}
	
	/**
	 * Changes the cursor mode.
	 *
	 * <p>When {@code null} is passed, the cursor mode is set to normal.</p>
	 * @param cursorMode The cursor mode to use
	 */
	public void setCursorMode(CursorMode cursorMode) {
		int value = cursorMode != null ? cursorMode.getValue() : CURSOR_MODE_DEFAULT;
		setInputMode(GLFW_CURSOR, value);
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
		if (entity == null || (!override && cursorBlocker != null) || !entity.isActive()) {
			return;
		}
		
		cursorBlocker = entity;
	}
	
	/**
	 * Checks whether the cursor is being blocked.
	 */
	public boolean isCursorBlocked() {
		return cursorBlocker != null;
	}
	
	/**
	 * Returns the entity that is blocking the cursor in the current frame.
	 */
	public Entity getCursorBlocker() {
		return cursorBlocker;
	}
	
	public void printCursorBlocker() {
		if (cursorBlocker == null) {
			return;
		}
		
		cursorBlocker.print(logger);
	}
	
	//endregion Mouse
	
	private void setInputMode(int mode, int value) {
		glfwSetInputMode(window.getId(), mode, value);
	}
	
}
