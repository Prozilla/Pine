package dev.prozilla.pine.common.property.input;

import dev.prozilla.pine.common.property.mutable.SimpleMutableObjectProperty;
import dev.prozilla.pine.common.util.StringUtils;
import dev.prozilla.pine.core.state.input.Input;
import dev.prozilla.pine.core.state.input.Key;

import java.util.Objects;

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
	public KeyboardKeyProperty toKeyboardKeyProperty(Key value) {
		setValue(value);
		return this;
	}
	
	@Override
	public boolean equals(Object object) {
		return object == this || (object instanceof KeyboardKeyProperty keyboardKeyProperty && equals(keyboardKeyProperty));
	}
	
	public boolean equals(KeyboardKeyProperty keyboardKeyProperty) {
		return keyboardKeyProperty != null && Objects.equals(getValue(), keyboardKeyProperty.getValue());
	}
	
	@Override
	public String toString() {
		return StringUtils.toString(getValue());
	}
	
}
