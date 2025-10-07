package dev.prozilla.pine.common.property.input;

import dev.prozilla.pine.common.property.mutable.SimpleMutableObjectProperty;
import dev.prozilla.pine.common.util.ArrayUtils;
import dev.prozilla.pine.core.state.input.Input;
import dev.prozilla.pine.core.state.input.MouseButton;

import java.util.Arrays;

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
	public MouseButtonsProperty toMouseButtonsProperty(MouseButton[] value) {
		setValue(value);
		return this;
	}
	
	@Override
	public boolean equals(Object object) {
		return object == this || (object instanceof MouseButtonsProperty mouseButtonsProperty && equals(mouseButtonsProperty));
	}
	
	public boolean equals(MouseButtonsProperty mouseButtonsProperty) {
		return mouseButtonsProperty != null && Arrays.equals(getValue(), mouseButtonsProperty.getValue());
	}
	
	@Override
	public String toString() {
		if (isNull()) {
			return "null";
		} else {
			return ArrayUtils.toString(getValue(), INPUT_ELEMENT_SEPARATOR);
		}
	}
	
}
