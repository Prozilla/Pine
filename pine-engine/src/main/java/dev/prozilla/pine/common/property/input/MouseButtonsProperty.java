package dev.prozilla.pine.common.property.input;

import dev.prozilla.pine.common.property.mutable.SimpleMutableObjectProperty;
import dev.prozilla.pine.core.state.input.Input;
import dev.prozilla.pine.core.state.input.MouseButton;

public class MouseButtonsProperty extends SimpleMutableObjectProperty<MouseButton[]> implements InputProperty {
	
	public MouseButtonsProperty() {
		this((MouseButton)null);
	}
	
	public MouseButtonsProperty(MouseButton... defaultButtons) {
		super(defaultButtons);
	}
	
	@Override
	public boolean isPressed(Input input) {
		if (isNull()) {
			return false;
		}
		for (MouseButton button : getValue()) {
			if (!input.getMouseButton(button)) {
				return false;
			}
		}
		return true;
	}
	
	@Override
	public boolean isDown(Input input) {
		if (isNull()) {
			return false;
		}
		for (MouseButton button : getValue()) {
			if (!input.getMouseButtonDown(button)) {
				return false;
			}
		}
		return true;
	}
	
	@Override
	public MouseButtonsProperty adaptValue(MouseButton[] value) {
		setValue(value);
		return this;
	}
	
}
