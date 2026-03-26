package dev.prozilla.pine.core.system.standard.ui.text;

import dev.prozilla.pine.core.component.ui.Node;
import dev.prozilla.pine.core.component.ui.NodeEvent;
import dev.prozilla.pine.core.component.ui.TextInputNode;
import dev.prozilla.pine.core.component.ui.TextNode;
import dev.prozilla.pine.core.entity.EntityChunk;
import dev.prozilla.pine.core.system.init.InitSystem;

public final class TextInputInitializer extends InitSystem {
	
	public TextInputInitializer() {
		super(TextInputNode.class, TextNode.class, Node.class);
	}
	
	@Override
	protected void process(EntityChunk chunk) {
		TextInputNode textInputNode = chunk.getComponent(TextInputNode.class);
		TextNode textNode = chunk.getComponent(TextNode.class);
		Node node = chunk.getComponent(Node.class);
		
		if (textInputNode.textListener != null) {
			application.getInput().removeTextListener(textInputNode.textListener);
		}
		
		textInputNode.textListener = (character) -> {
			if (node.isFocused()) {
				boolean selectionDeleted = textInputNode.deleteSelection();
				
				boolean textChanged = textInputNode.getTextProperty().buildValue((stringBuilder) -> {
					if (textInputNode.cursorPosition < 0 || textInputNode.cursorPosition > stringBuilder.length()) {
						return null;
					}
					String string = stringBuilder.insert(textInputNode.cursorPosition, character).toString();
					return textInputNode.type.isValid(string) ? string : null;
				});
				
				if (selectionDeleted || textChanged) {
					if (textChanged) {
						textInputNode.moveCursorRight();
					}
					node.invoke(NodeEvent.Type.INPUT);
				}
			}
		};
		textInputNode.textNode = textNode;
		textInputNode.getTextProperty().setValue(textNode.text);
		textInputNode.moveCursorToEnd();
		
		application.getInput().addTextListener(textInputNode.textListener);
	}
}
