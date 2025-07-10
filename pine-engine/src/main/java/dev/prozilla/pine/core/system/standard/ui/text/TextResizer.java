package dev.prozilla.pine.core.system.standard.ui.text;

import dev.prozilla.pine.core.component.ui.Node;
import dev.prozilla.pine.core.component.ui.TextInputNode;
import dev.prozilla.pine.core.component.ui.TextNode;
import dev.prozilla.pine.core.entity.EntityChunk;
import dev.prozilla.pine.core.system.update.UpdateSystem;

public class TextResizer extends UpdateSystem {
	
	public TextResizer() {
		super(TextNode.class, Node.class);
		setExcludedComponentTypes(TextInputNode.class);
	}
	
	@Override
	protected void process(EntityChunk chunk, float deltaTime) {
		TextNode textNode = chunk.getComponent(TextNode.class);
		Node node = chunk.getComponent(Node.class);
		
		if (node.size.isZero(node)) {
			node.currentInnerSize.x = textNode.size.x + node.getPaddingX() * 2;
			node.currentInnerSize.y = textNode.size.y + node.getPaddingY() * 2;
		}
	}
}
