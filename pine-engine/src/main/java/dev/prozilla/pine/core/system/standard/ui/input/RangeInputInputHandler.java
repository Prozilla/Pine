package dev.prozilla.pine.core.system.standard.ui.input;

import dev.prozilla.pine.common.math.MathUtils;
import dev.prozilla.pine.common.math.vector.Vector2i;
import dev.prozilla.pine.common.property.bindable.BindableFloatProperty;
import dev.prozilla.pine.core.component.ui.Node;
import dev.prozilla.pine.core.component.ui.RangeInputNode;
import dev.prozilla.pine.core.entity.EntityChunk;
import dev.prozilla.pine.core.state.input.Input;
import dev.prozilla.pine.core.state.input.Key;
import dev.prozilla.pine.core.state.input.MouseButton;
import dev.prozilla.pine.core.system.input.InputSystem;

public final class RangeInputInputHandler extends InputSystem {
	
	public RangeInputInputHandler() {
		super(RangeInputNode.class, Node.class);
	}
	
	@Override
	protected void process(EntityChunk chunk, Input input, float deltaTime) {
		RangeInputNode rangeInputNode = chunk.getComponent(RangeInputNode.class);
		Node node = chunk.getComponent(Node.class);
		
		BindableFloatProperty valueProperty = rangeInputNode.getValueProperty();
		
		if (node.cursorHit && input.getMouseButtonDown(MouseButton.LEFT)) {
			rangeInputNode.isDragging = true;
		} else if (!input.getMouseButton(MouseButton.LEFT)) {
			rangeInputNode.isDragging = false;
		}
		
		if (rangeInputNode.isDragging) {
			Vector2i cursor = input.getCursor(true);
			float position = MathUtils.remap(MathUtils.clamp(cursor.x - node.currentPosition.x, 0, node.currentInnerSize.x), 0, node.currentInnerSize.x, rangeInputNode.getMin(), rangeInputNode.getMax());
			valueProperty.set(position);
		}
		
		if (node.isFocused()) {
			if (input.getKeyRepeated(Key.RIGHT_ARROW)) {
				valueProperty.set(valueProperty.get() + rangeInputNode.step);
			} else if (input.getKeyRepeated(Key.LEFT_ARROW)) {
				valueProperty.set(valueProperty.get() - rangeInputNode.step);
			}
		}
	}
	
}
