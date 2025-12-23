package dev.prozilla.pine.core.system.standard.ui.text;

import dev.prozilla.pine.common.system.Color;
import dev.prozilla.pine.core.component.Transform;
import dev.prozilla.pine.core.component.ui.Node;
import dev.prozilla.pine.core.component.ui.TextInputNode;
import dev.prozilla.pine.core.component.ui.TextNode;
import dev.prozilla.pine.core.entity.EntityChunk;
import dev.prozilla.pine.core.rendering.Renderer;
import dev.prozilla.pine.core.system.render.RenderSystem;

public final class TextInputRenderer extends RenderSystem {
	
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
		
		float cursorX = x;
		
		if (textInputNode.cursorPosition > 0) {
			cursorX += getTextWidth(renderer, textNode, textInputNode.cursorPosition);
		}
		
		// Draw selection
		if (textInputNode.hasSelection()) {
			float selectionX;
			if (textInputNode.selection > 0) {
				selectionX = cursorX;
			} else {
				selectionX = x + getTextWidth(renderer, textNode, textInputNode.getSelectionStart());
			}
			float selectionWidth = getTextWidth(renderer, textNode, textInputNode.getSelectionStart(), textInputNode.getSelectionEnd());
			renderer.drawRect(selectionX, y, transform.getDepth(), selectionWidth, textNode.getFontSize(), Color.cyan().setAlpha(0.5f));
		}
		
		// Draw cursor
		renderer.drawRect(cursorX, y, transform.getDepth(), 2, textNode.getFontSize(), node.color);
	}
	
	private static int getTextWidth(Renderer renderer, TextNode textNode, int length) {
		return getTextWidth(renderer, textNode, 0, length);
	}
	
	private static int getTextWidth(Renderer renderer, TextNode textNode, int start, int end) {
		String text = textNode.text.substring(start, end);
		return textNode.font == null ? renderer.getTextWidth(text) : renderer.getTextWidth(textNode.font, text);
	}
	
}
