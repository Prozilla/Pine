package dev.prozilla.pine.common.property.input;

import dev.prozilla.pine.common.property.mutable.SimpleMutableObjectProperty;
import dev.prozilla.pine.core.state.input.Input;
import dev.prozilla.pine.core.state.input.Key;

public class KeyboardKeyProperty extends SimpleMutableObjectProperty<Key> implements InputProperty {
	
	public KeyboardKeyProperty() {
		this(null);
	}
	
	public KeyboardKeyProperty(Key defaultKey) {
		super(defaultKey);
	}
	
	@Override
	public boolean isPressed(Input input) {
		if (isNull()) {
			return false;
		}
		return input.getKey(getValue());
	}
	
	@Override
	public boolean isDown(Input input) {
		if (isNull()) {
			return false;
		}
		return input.getKeyDown(getValue());
	}
	
	@Override
	public KeyboardKeyProperty adaptValue(Key value) {
		setValue(value);
		return this;
	}
	
}
