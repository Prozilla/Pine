package dev.prozilla.pine.core.system.standard.ui.input;

import dev.prozilla.pine.common.system.Color;
import dev.prozilla.pine.core.component.Transform;
import dev.prozilla.pine.core.component.ui.Node;
import dev.prozilla.pine.core.component.ui.TextInputNode;
import dev.prozilla.pine.core.component.ui.TextNode;
import dev.prozilla.pine.core.entity.EntityChunk;
import dev.prozilla.pine.core.rendering.Renderer;
import dev.prozilla.pine.core.system.render.RenderPass;
import dev.prozilla.pine.core.system.render.RenderSystem;
import dev.prozilla.pine.core.system.standard.ui.text.TextRenderer;

public final class TextInputRenderer extends RenderSystem {
	
	public TextInputRenderer() {
		super(TextInputNode.class, TextNode.class, Node.class);
		setRenderPass(RenderPass.OVERLAY);
	}
	
	@Override
	protected void process(EntityChunk chunk, Renderer renderer) {
		Transform transform = chunk.getTransform();
		TextInputNode textInputNode = chunk.getComponent(TextInputNode.class);
		TextNode textNode = chunk.getComponent(TextNode.class);
		Node node = chunk.getComponent(Node.class);
		
		if (node.controlledRender) {
			return;
		}
		
		float x = node.currentPosition.x + node.getBoxX();
		float y = node.currentPosition.y + node.getBoxY();
		
		if (textInputNode.getTextProperty().isEmpty() && textInputNode.placeholderNode != null) {
			TextRenderer.renderText(renderer, textInputNode.placeholderTextNode, x, y, node.currentInnerSize.x, node.currentInnerSize.y, textInputNode.placeholderNode.color);
		}
		
		if (!node.isFocused()) {
			return;
		}
		
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
			renderer.drawRect(selectionX, y, transform.position.z, selectionWidth, textNode.getFontSize(), Color.cyan().setAlpha(0.5f));
		}
		
		// Draw cursor
		renderer.drawRect(cursorX, y, transform.position.z, 2, textNode.getFontSize(), node.color);
	}
	
	private static int getTextWidth(Renderer renderer, TextNode textNode, int length) {
		return getTextWidth(renderer, textNode, 0, length);
	}
	
	private static int getTextWidth(Renderer renderer, TextNode textNode, int start, int end) {
		start = Math.max(start, 0);
		end = Math.min(end, textNode.text.length());
		if (start >= end) {
			return 0;
		}
		String text = textNode.text.substring(start, end);
		return textNode.font == null ? renderer.getTextWidth(text) : renderer.getTextWidth(textNode.font, text);
	}
	
}
