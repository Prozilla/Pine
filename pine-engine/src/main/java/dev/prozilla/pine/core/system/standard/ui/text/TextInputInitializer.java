package dev.prozilla.pine.core.system.standard.ui.text;

import dev.prozilla.pine.common.math.MathUtils;
import dev.prozilla.pine.core.component.ui.Node;
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
		
		textInputNode.cursorPosition = textNode.text.length();
		textInputNode.textListener = (character) -> {
			if (node.isFocused()) {
				boolean changed = textNode.changeText((stringBuilder) -> {
					String newText = stringBuilder.insert(textInputNode.cursorPosition, character).toString();
					
					if (textInputNode.type != TextInputNode.Type.NUMBER || MathUtils.isValidInteger(newText)) {
						return newText;
					}
					
					return null;
				});
				
				if (changed) {
					textInputNode.cursorPosition++;
				}
			}
		};
		textInputNode.textNode = textNode;
		
		application.getInput().addTextListener(textInputNode.textListener);
	}
}
