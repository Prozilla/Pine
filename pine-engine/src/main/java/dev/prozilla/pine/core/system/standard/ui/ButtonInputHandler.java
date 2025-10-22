package dev.prozilla.pine.core.system.standard.ui;

import dev.prozilla.pine.core.component.ui.ButtonNode;
import dev.prozilla.pine.core.component.ui.Node;
import dev.prozilla.pine.core.entity.EntityChunk;
import dev.prozilla.pine.core.state.input.CursorType;
import dev.prozilla.pine.core.state.input.Input;
import dev.prozilla.pine.core.state.input.Key;
import dev.prozilla.pine.core.state.input.MouseButton;
import dev.prozilla.pine.core.system.input.InputSystem;

public final class ButtonInputHandler extends InputSystem {
	
	public ButtonInputHandler() {
		super(ButtonNode.class, Node.class);
	}
	
	@Override
	protected void process(EntityChunk chunk, Input input, float deltaTime) {
		ButtonNode buttonNode = chunk.getComponent(ButtonNode.class);
		Node node = chunk.getComponent(Node.class);
		
		buttonNode.isHovering = node.cursorHit;

		if (node.cursorHit) {
			input.setCursorType(CursorType.POINTER);

			if (buttonNode.clickCallback != null && input.getMouseButtonDown(MouseButton.LEFT)) {
				buttonNode.click();
			}
		}
		
		if (buttonNode.clickCallback != null && input.getKeyDown(Key.ENTER) && node.isFocused()) {
			buttonNode.click();
		}
	}
}
