package dev.prozilla.pine.core.system.standard.ui.input;

import dev.prozilla.pine.common.property.bindable.BindableFloatProperty;
import dev.prozilla.pine.core.component.ui.Node;
import dev.prozilla.pine.core.component.ui.RangeInputNode;
import dev.prozilla.pine.core.entity.EntityChunk;
import dev.prozilla.pine.core.state.input.CursorType;
import dev.prozilla.pine.core.state.input.Input;
import dev.prozilla.pine.core.state.input.Key;
import dev.prozilla.pine.core.system.input.InputSystem;

public final class RangeInputInputHandler extends InputSystem {
	
	public RangeInputInputHandler() {
		super(RangeInputNode.class, Node.class);
	}
	
	@Override
	protected void process(EntityChunk chunk, Input input, float deltaTime) {
		RangeInputNode rangeInputNode = chunk.getComponent(RangeInputNode.class);
		Node node = chunk.getComponent(Node.class);
		
		if (node.cursorHit) {
			input.setCursorType(CursorType.TEXT);
		}
		
		if (!node.isFocused()) {
			return;
		}
		
		BindableFloatProperty valueProperty = rangeInputNode.getValueProperty();
		
		if (input.getKeyRepeated(Key.RIGHT_ARROW)) {
			valueProperty.set(valueProperty.get() + rangeInputNode.step);
		} else if (input.getKeyRepeated(Key.LEFT_ARROW)) {
			valueProperty.set(valueProperty.get() - rangeInputNode.step);
		}
	}
	
}
