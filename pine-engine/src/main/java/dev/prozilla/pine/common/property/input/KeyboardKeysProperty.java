package dev.prozilla.pine.common.property.input;

import dev.prozilla.pine.common.property.mutable.SimpleMutableObjectProperty;
import dev.prozilla.pine.core.state.input.Input;
import dev.prozilla.pine.core.state.input.Key;

public class KeyboardKeysProperty extends SimpleMutableObjectProperty<Key[]> implements InputProperty {
	
	public KeyboardKeysProperty() {
		this((Key)null);
	}
	
	public KeyboardKeysProperty(Key... defaultKeys) {
		super(defaultKeys);
	}
	
	@Override
	public boolean isPressed(Input input) {
		if (!isNotNull()) {
			return false;
		}
		return input.getKeys(getValue());
	}
	
	@Override
	public boolean isDown(Input input) {
		if (!isNotNull()) {
			return false;
		}
		boolean anyDown = false;
		for (Key key : getValue()) {
			if (input.getKeyDown(key)) {
				anyDown = true;
			} else if (!input.getKey(key)) {
				return false;
			}
		}
		return anyDown;
	}
	
	@Override
	public KeyboardKeysProperty adaptValue(Key[] value) {
		setValue(value);
		return this;
	}
	
}
