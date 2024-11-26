package dev.prozilla.pine.core.state.input;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Handles key bindings for the input system.
 * @param <Action> Enum of actions
 */
public class KeyBindings<Action extends Enum<Action>> {
	
	private final Map<Action, Key> keyBindings;
	private final Map<Action, Key[]> multiKeyBindings;
	
	private final Input input;
	
	/**
	 * Creates new key bindings.
	 * @param input Reference to input system
	 */
	public KeyBindings(Input input) {
		this.input = input;
		
		keyBindings = new HashMap<>();
		multiKeyBindings = new HashMap<>();
	}
	
	/**
	 * Binds a key to an action.
	 * @param action Action
	 * @param key Key
	 */
	public void addAction(Action action, Key key) {
		keyBindings.put(action, key);
		multiKeyBindings.remove(action);
	}
	
	/**
	 * Binds multiple keys to a single action.
	 * @param action Action
	 * @param keys Key
	 */
	public void addAction(Action action, Key[] keys) {
		multiKeyBindings.put(action, keys);
		keyBindings.remove(action);
	}
	
	/**
	 * Unbinds the keys for a given action.
	 * @param action Action
	 */
	public void removeAction(Action action) {
		keyBindings.remove(action);
		multiKeyBindings.remove(action);
	}
	
	/**
	 * Returns the key bound to a given action.
	 * @param action Action
	 * @return Key
	 */
	public Key getActionKey(Action action) {
		if (keyBindings.containsKey(action)) {
			return keyBindings.get(action);
		} else if (multiKeyBindings.containsKey(action)) {
			return multiKeyBindings.get(action)[0];
		}
		
		return null;
	}
	
	/**
	 * Returns the keys bound to a given action.
	 * @param action Action
	 * @return Keys
	 */
	public Key[] getActionKeys(Action action) {
		if (multiKeyBindings.containsKey(action)) {
			return multiKeyBindings.get(action);
		} else if (keyBindings.containsKey(action)) {
			return new Key[] { keyBindings.get(action) };
		}
		
		return null;
	}
	
	/**
	 * Checks whether the key(s) bound to a given action are pressed.
	 * @param action Action
	 * @return True if any key bound to the action is pressed
	 */
	public boolean isActive(Action action) {
		if (keyBindings.containsKey(action)) {
			return input.getKey(keyBindings.get(action));
		} else if (multiKeyBindings.containsKey(action)) {
			return input.getAnyKey(multiKeyBindings.get(action));
		}
		
		return false;
	}
	
	/**
	 * Prints all key bindings.
	 */
	public void print() {
		for (var keyBind : keyBindings.entrySet()) {
			System.out.println(keyBind.getKey().name() + ": " + keyBind.getValue().name());
		}
		for (var multiKeyBind : multiKeyBindings.entrySet()) {
			ArrayList<String> keys = new ArrayList<>();
			for (Key key : multiKeyBind.getValue()) {
				keys.add(key.name());
			}
			System.out.println(multiKeyBind.getKey().name() + ": " + String.join(", ", keys));
		}
	}
}
