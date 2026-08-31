package dev.prozilla.pine.core.system.standard.ui.input;

import dev.prozilla.pine.common.math.MathUtils;
import dev.prozilla.pine.common.math.vector.Vector2i;
import dev.prozilla.pine.core.component.ui.Node;
import dev.prozilla.pine.core.component.ui.TextInputNode;
import dev.prozilla.pine.core.component.ui.TextNode;
import dev.prozilla.pine.core.entity.EntityChunk;
import dev.prozilla.pine.core.state.input.Input;
import dev.prozilla.pine.core.state.input.Key;
import dev.prozilla.pine.core.state.input.ModifierKey;
import dev.prozilla.pine.core.state.input.MouseButton;
import dev.prozilla.pine.core.system.input.InputSystem;
import dev.prozilla.pine.core.system.standard.ui.text.TextRenderer;

public final class TextInputInputHandler extends InputSystem {
	
	public TextInputInputHandler() {
		super(TextInputNode.class, Node.class);
	}
	
	@Override
	protected void process(EntityChunk chunk, Input input, float deltaTime) {
		TextInputNode textInputNode = chunk.getComponent(TextInputNode.class);
		TextNode textNode = textInputNode.textNode;
		Node node = chunk.getComponent(Node.class);
		
		if (!node.isFocused()) {
			return;
		}
		
		boolean ctrlKey = input.getModifierKey(ModifierKey.CONTROL);
		boolean shiftKey = input.getModifierKey(ModifierKey.SHIFT);
		
		float nodeX = node.currentPosition.x;
		float nodeWidth = node.currentInnerSize.x;
		
		if (node.cursorHit && input.getMouseButtonDown(MouseButton.LEFT)) {
			Vector2i cursor = input.getCursor(true);
			float position = MathUtils.clamp(cursor.x - nodeX, 0, nodeWidth) - textNode.getOffsetX();
			textInputNode.moveCursorTo(textInputNode.getCursorPosition(position), shiftKey);
			textInputNode.isDragging = true;
		} else if (!input.getMouseButton(MouseButton.LEFT)) {
			textInputNode.isDragging = false;
		} else if (textInputNode.isDragging) {
			float cursor = input.getCursor(true).x - nodeX;
			float visibleTextWidth = Math.min(TextRenderer.getTextWidth(application.getRenderer(), textNode), nodeWidth);
			
			int delta;
			if (cursor <= 0) {
				delta = -1;
			} else if (cursor >= visibleTextWidth) {
				delta = 1;
			} else {
				float position = MathUtils.clamp(cursor, 0, nodeWidth) - textNode.getOffsetX();
				delta = textInputNode.getCursorPosition(position) - textInputNode.cursorPosition;
			}
			
			textInputNode.dragSelection(delta);
		} else if (input.getKeyRepeated(Key.LEFT_ARROW)) {
			if (shiftKey) {
				textInputNode.dragSelection(-1);
			} else {
				textInputNode.moveCursor(-1);
			}
		} else if (input.getKeyRepeated(Key.RIGHT_ARROW)) {
			if (shiftKey) {
				textInputNode.dragSelection(1);
			} else {
				textInputNode.moveCursor(1);
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
