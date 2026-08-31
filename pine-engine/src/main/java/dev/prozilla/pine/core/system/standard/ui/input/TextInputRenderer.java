package dev.prozilla.pine.core.system.standard.ui.input;

import dev.prozilla.pine.common.math.MathUtils;
import dev.prozilla.pine.common.math.vector.Vector2f;
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
		
		float cursorOffset = 0;
		if (textInputNode.cursorPosition > 0) {
			cursorOffset = TextRenderer.getTextWidth(renderer, textNode, textInputNode.cursorPosition);
		}
		float cursorX = x + cursorOffset;
		
		// Clamp cursor position
		float visibleTextWidth = node.currentInnerSize.x - node.getBoxX() * 2f;
		if (cursorOffset + textNode.getOffsetX() > visibleTextWidth) {
			if (textNode.offset == null) {
				textNode.offset = new Vector2f();
			}
			textNode.offset.x = visibleTextWidth - cursorOffset;
		} else if (cursorOffset + textNode.getOffsetX() < 0) {
			if (textNode.offset == null) {
				textNode.offset = new Vector2f();
			}
			textNode.offset.x = -cursorOffset;
		}
		
		// Draw selection
		if (textInputNode.hasSelection()) {
			int anchorIndex = textInputNode.cursorPosition + textInputNode.selection;
			float anchorScreenX = x + TextRenderer.getTextWidth(renderer, textNode, anchorIndex) + textNode.getOffsetX();
			float cursorScreenX = cursorX + textNode.getOffsetX();
			
			float visibleEnd = x + visibleTextWidth;
			float clampedAnchorX = MathUtils.clamp(anchorScreenX, x, visibleEnd);
			
			float selectionLeft = Math.min(cursorScreenX, clampedAnchorX);
			float selectionWidth = Math.abs(cursorScreenX - clampedAnchorX);
			
			renderer.drawRect(selectionLeft, y, transform.position.z, selectionWidth, textNode.getFontSize(), new Color(0, 96, 223).setAlpha(0.5f));
		}
		
		// Draw cursor
		renderer.drawRect(cursorX + textNode.getOffsetX(), y, transform.position.z, 2, textNode.getFontSize(), node.color);
	}
	
}
