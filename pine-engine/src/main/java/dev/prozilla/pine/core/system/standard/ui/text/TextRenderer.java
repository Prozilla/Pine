package dev.prozilla.pine.core.system.standard.ui.text;

import dev.prozilla.pine.common.asset.text.Font;
import dev.prozilla.pine.common.system.Color;
import dev.prozilla.pine.common.util.StringUtils;
import dev.prozilla.pine.core.component.ui.Node;
import dev.prozilla.pine.core.component.ui.TextNode;
import dev.prozilla.pine.core.entity.EntityChunk;
import dev.prozilla.pine.core.rendering.Renderer;
import dev.prozilla.pine.core.system.render.RenderPass;
import dev.prozilla.pine.core.system.render.RenderSystem;

/**
 * Renders text elements to the screen.
 */
public final class TextRenderer extends RenderSystem {
	
	public TextRenderer() {
		super(TextNode.class, Node.class);
		setRenderPass(RenderPass.OVERLAY);
	}
	
	@Override
	public void process(EntityChunk chunk, Renderer renderer) {
		TextNode textNode = chunk.getComponent(TextNode.class);
		Node node = chunk.getComponent(Node.class);
		
		if (!node.canBeRendered()) {
			return;
		}
		
		renderText(renderer, textNode, node);
	}
	
	public static void renderText(Renderer renderer, TextNode textNode, Node node) {
		float x = node.currentPosition.x + node.getBoxX();
		float y = node.currentPosition.y + node.getBoxY();
		float width = node.currentInnerSize.x;
		float height = node.currentInnerSize.y;
		
		if (textNode.offset != null) {
			x += textNode.offset.x;
			y += textNode.offset.y;
		}
		
		renderText(renderer, textNode, x, y, width, height, node.color);
	}
	
	public static void renderText(Renderer renderer, TextNode textNode, float x, float y, float width, float height, Color color) {
		renderText(renderer, textNode.text, textNode.font, x, y, textNode.getTransform().position.z, width, height, color);
	}
	
	/**
	 * Renders text on the screen on a given position.
	 * @param x X position
	 * @param y Y position
	 */
	public static void renderText(Renderer renderer, String text, Font font, float x, float y,  float z, float width, float height, Color color) {
		if (text.isBlank() || width == 0 || height == 0 || color == null) {
			return;
		}
		
		if (renderer.getConfig().snapText.get()) {
			int roundedX = Math.round(x);
			int roundedY = Math.round(y);
			int roundedWidth = Math.round(width);
			int roundedHeight = Math.round(height);
			
//			renderer.setRegion(roundedX, roundedY, roundedWidth, roundedHeight);
			
			x = roundedX;
			y = roundedY;
		} else {
//			renderer.setRegion(x, y, width, height);
		}
		
		if (font == null) {
			renderer.drawText(text, x, y, z, color);
		} else {
			renderer.drawText(font, text, x, y, z, color);
		}
		
		renderer.resetRegion();
	}
	
	public static int getTextWidth(Renderer renderer, TextNode textNode) {
		return getTextWidth(renderer, textNode, StringUtils.lengthOf(textNode.text));
	}
	
	public static int getTextWidth(Renderer renderer, TextNode textNode, int length) {
		return getTextWidth(renderer, textNode, 0, length);
	}
	
	public static int getTextWidth(Renderer renderer, TextNode textNode, int start, int end) {
		start = Math.max(start, 0);
		end = Math.min(end, textNode.text.length());
		if (start >= end) {
			return 0;
		}
		String text = textNode.text.substring(start, end);
		return textNode.font == null ? renderer.getTextWidth(text) : renderer.getTextWidth(textNode.font, text);
	}
	
}
