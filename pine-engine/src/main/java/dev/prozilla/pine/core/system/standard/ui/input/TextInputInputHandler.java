package dev.prozilla.pine.core.system.standard.ui.input;

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
		
		boolean ctrlKey = input.getModifierKey(ModifierKey.CONTROL);
		boolean shiftKey = input.getModifierKey(ModifierKey.SHIFT);
		
		if (input.getKeyRepeated(Key.LEFT_ARROW)) {
			if (shiftKey) {
				textInputNode.expandSelectionLeft();
			} else {
				textInputNode.moveCursorLeft();
			}
		} else if (input.getKeyRepeated(Key.RIGHT_ARROW)) {
			if (shiftKey) {
				textInputNode.expandSelectionRight();
			} else {
				textInputNode.moveCursorRight();
			}
		} else if (input.getKeyRepeated(Key.UP_ARROW) || input.getKeyRepeated(Key.HOME)) {
			textInputNode.moveCursorToStart(shiftKey);
		} else if (input.getKeyRepeated(Key.DOWN_ARROW) || input.getKeyRepeated(Key.END)) {
			textInputNode.moveCursorToEnd(shiftKey);
		} else if (input.getKeyRepeated(Key.BACKSPACE)) {
			textInputNode.deleteText(true);
		} else if (input.getKeyRepeated(Key.DELETE)) {
			textInputNode.deleteText(false);
		} else if (input.getKeyDown(Key.A) && ctrlKey) {
			textInputNode.selectAll();
		} else if (input.getKeyDown(Key.C) && ctrlKey) {
			String selectedText = textInputNode.getSelectedText();
			if (selectedText != null) {
				input.setClipboard(selectedText);
			}
		} else if (input.getKeyDown(Key.X) && ctrlKey) {
			String selectedText = textInputNode.getSelectedText();
			if (selectedText != null) {
				input.setClipboard(selectedText);
			}
			textInputNode.deleteSelection();
		} else if (input.getKeyDown(Key.V) && ctrlKey) {
			textInputNode.insert(input.getClipboard());
		}
	}
	
}
