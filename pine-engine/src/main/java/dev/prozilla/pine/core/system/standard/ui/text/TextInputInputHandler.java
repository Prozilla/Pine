package dev.prozilla.pine.core.system.standard.ui.text;

import dev.prozilla.pine.core.component.ui.Node;
import dev.prozilla.pine.core.component.ui.TextInputNode;
import dev.prozilla.pine.core.entity.EntityChunk;
import dev.prozilla.pine.core.state.input.CursorType;
import dev.prozilla.pine.core.state.input.Input;
import dev.prozilla.pine.core.state.input.Key;
import dev.prozilla.pine.core.state.input.ModifierKey;
import dev.prozilla.pine.core.system.input.InputSystem;

public final class TextInputInputHandler extends InputSystem {
	
	public TextInputInputHandler() {
		super(TextInputNode.class, Node.class);
	}
	
	@Override
	protected void process(EntityChunk chunk, Input input, float deltaTime) {
		TextInputNode textInputNode = chunk.getComponent(TextInputNode.class);
		Node node = chunk.getComponent(Node.class);
		
		if (node.cursorHit) {
			input.setCursorType(CursorType.TEXT);
		}
		
		if (!node.isFocused()) {
			return;
		}
		
		if (input.getKeyDown(Key.LEFT_ARROW)) {
			if (input.getModifierKey(ModifierKey.SHIFT)) {
				textInputNode.expandSelectionLeft();
			} else {
				textInputNode.moveCursorLeft();
			}
		} else if (input.getKeyDown(Key.RIGHT_ARROW)) {
			if (input.getModifierKey(ModifierKey.SHIFT)) {
				textInputNode.expandSelectionRight();
			} else {
				textInputNode.moveCursorRight();
			}
		} else if (input.getKeyDown(Key.UP_ARROW) || input.getKeyDown(Key.HOME)) {
			textInputNode.moveCursorToStart();
		} else if (input.getKeyDown(Key.DOWN_ARROW) || input.getKeyDown(Key.END)) {
			textInputNode.moveCursorToEnd();
		} else if (input.getKeyDown(Key.BACKSPACE)) {
			textInputNode.deleteText(true);
		} else if (input.getKeyDown(Key.DELETE)) {
			textInputNode.deleteText(false);
		}
	}
	
}
