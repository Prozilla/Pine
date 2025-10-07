package dev.prozilla.pine.common.property.input;

import dev.prozilla.pine.common.property.mutable.SimpleMutableObjectProperty;
import dev.prozilla.pine.core.state.input.Input;
import dev.prozilla.pine.core.state.input.MouseButton;

public class MouseButtonProperty extends SimpleMutableObjectProperty<MouseButton> implements InputProperty {
	
	public MouseButtonProperty() {
		this(null);
	}
	
	public MouseButtonProperty(MouseButton defaultButton) {
		super(defaultButton);
	}
	
	@Override
	public boolean isPressed(Input input) {
		if (isNull()) {
			return false;
		}
		return input.getMouseButton(getValue());
	}
	
	@Override
	public boolean isDown(Input input) {
		if (isNull()) {
			return false;
		}
		return input.getMouseButtonDown(getValue());
	}
	
	@Override
	public MouseButtonProperty adaptValue(MouseButton value) {
		setValue(value);
		return this;
	}
	
}
