package dev.prozilla.pine.core.system.standard.ui.text;

import dev.prozilla.pine.core.component.ui.Node;
import dev.prozilla.pine.core.component.ui.TextInputNode;
import dev.prozilla.pine.core.component.ui.TextNode;
import dev.prozilla.pine.core.entity.EntityChunk;
import dev.prozilla.pine.core.state.input.CursorType;
import dev.prozilla.pine.core.state.input.Input;
import dev.prozilla.pine.core.state.input.Key;
import dev.prozilla.pine.core.system.input.InputSystem;

public class TextInputInputHandler extends InputSystem {
	
	public TextInputInputHandler() {
		super(TextInputNode.class, TextNode.class, Node.class);
	}
	
	@Override
	protected void process(EntityChunk chunk, Input input, float deltaTime) {
		TextInputNode textInputNode = chunk.getComponent(TextInputNode.class);
		TextNode textNode = chunk.getComponent(TextNode.class);
		Node node = chunk.getComponent(Node.class);
		
		if (node.cursorHit) {
			input.setCursorType(CursorType.TEXT);
		}
		
		if (!node.isFocused()) {
			return;
		}
		
		int max = textNode.text.length();
		
		if (input.getKeyDown(Key.LEFT_ARROW)) {
			textInputNode.cursorPosition--;
		} else if (input.getKeyDown(Key.RIGHT_ARROW)) {
			textInputNode.cursorPosition++;
		} else if (input.getKeyDown(Key.UP_ARROW) || input.getKeyDown(Key.HOME)) {
			textInputNode.cursorPosition = 0;
		} else if (input.getKeyDown(Key.DOWN_ARROW) || input.getKeyDown(Key.END)) {
			textInputNode.cursorPosition = max;
		} else if (input.getKeyDown(Key.BACKSPACE)) {
			if (textInputNode.cursorPosition > 0) {
				textInputNode.cursorPosition--;
				textNode.changeText((stringBuilder) ->
					stringBuilder.deleteCharAt(textInputNode.cursorPosition).toString()
				);
			}
		} else if (input.getKeyDown(Key.DELETE)) {
			if (textInputNode.cursorPosition < max) {
				textNode.changeText((stringBuilder) ->
					stringBuilder.deleteCharAt(textInputNode.cursorPosition).toString()
				);
			}
		}
		
		textInputNode.updateCursor();
	}
	
}
