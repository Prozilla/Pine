package dev.prozilla.pine.core.system.standard.ui.text;

import dev.prozilla.pine.core.component.Transform;
import dev.prozilla.pine.core.component.ui.Node;
import dev.prozilla.pine.core.component.ui.TextInputNode;
import dev.prozilla.pine.core.component.ui.TextNode;
import dev.prozilla.pine.core.entity.EntityChunk;
import dev.prozilla.pine.core.rendering.Renderer;
import dev.prozilla.pine.core.system.render.RenderSystem;

public class TextInputRenderer extends RenderSystem {
	
	public TextInputRenderer() {
		super(TextInputNode.class, TextNode.class, Node.class);
	}
	
	@Override
	protected void process(EntityChunk chunk, Renderer renderer) {
		Transform transform = chunk.getTransform();
		TextInputNode textInputNode = chunk.getComponent(TextInputNode.class);
		TextNode textNode = chunk.getComponent(TextNode.class);
		Node node = chunk.getComponent(Node.class);
		
		if (!node.isFocused()) {
			return;
		}
		
		float x = node.currentPosition.x + node.getPaddingX();
		float y = node.currentPosition.y + node.getPaddingY();
		
		if (textInputNode.cursorPosition > 0) {
			if (textNode.font == null) {
				x += renderer.getTextWidth(textNode.text.substring(0, textInputNode.cursorPosition));
			} else {
				x += renderer.getTextWidth(textNode.font, textNode.text.substring(0, textInputNode.cursorPosition));
			}
		}
		
		renderer.drawRect(x, y, transform.getDepth(), 2, textNode.getFontSize(), node.color);
	}
	
}
