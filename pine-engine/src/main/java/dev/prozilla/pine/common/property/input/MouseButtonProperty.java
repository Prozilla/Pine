package dev.prozilla.pine.common.property.input;

import dev.prozilla.pine.common.property.mutable.SimpleMutableObjectProperty;
import dev.prozilla.pine.common.util.StringUtils;
import dev.prozilla.pine.core.state.input.Input;
import dev.prozilla.pine.core.state.input.MouseButton;

import java.util.Objects;

public class MouseButtonProperty extends SimpleMutableObjectProperty<MouseButton> implements InputProperty {
	
	public MouseButtonProperty() {
		this(null);
	}
	
	public MouseButtonProperty(MouseButton defaultButton) {
		super(defaultButton);
	}
	
	@Override
	public boolean isPressed(Input input) {
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
	public MouseButtonProperty toMouseButtonProperty(MouseButton value) {
		setValue(value);
		return this;
	}
	
	@Override
	public boolean equals(Object object) {
		return object == this || (object instanceof MouseButtonProperty mouseButtonProperty && equals(mouseButtonProperty));
	}
	
	public boolean equals(MouseButtonProperty mouseButtonProperty) {
		return mouseButtonProperty != null && Objects.equals(getValue(), mouseButtonProperty.getValue());
	}
	
	@Override
	public String toString() {
		return StringUtils.toString(getValue());
	}
	
}
