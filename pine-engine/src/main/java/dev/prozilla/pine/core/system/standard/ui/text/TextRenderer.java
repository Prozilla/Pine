package dev.prozilla.pine.core.system.standard.ui.text;

import dev.prozilla.pine.common.system.resource.Color;
import dev.prozilla.pine.common.system.resource.text.Font;
import dev.prozilla.pine.core.component.Transform;
import dev.prozilla.pine.core.component.ui.Node;
import dev.prozilla.pine.core.component.ui.TextNode;
import dev.prozilla.pine.core.entity.EntityChunk;
import dev.prozilla.pine.core.rendering.Renderer;
import dev.prozilla.pine.core.system.render.RenderSystem;

/**
 * Renders text elements to the screen.
 */
public class TextRenderer extends RenderSystem {
	
	public TextRenderer() {
		super(TextNode.class, Node.class);
	}
	
	@Override
	public void process(EntityChunk chunk, Renderer renderer) {
		Transform transform = chunk.getTransform();
		TextNode textNode = chunk.getComponent(TextNode.class);
		Node node = chunk.getComponent(Node.class);
		
		if (!node.readyToRender) {
			return;
		}
		
		renderText(renderer, textNode, node, transform.getDepth());
	}
	
	public static void renderText(Renderer renderer, TextNode textNode, Node node, float z) {
		renderText(renderer, textNode, node.currentPosition.x + node.getPaddingX(), node.currentPosition.y + node.getPaddingY(), z, node.color);
	}
	
	public static void renderText(Renderer renderer, TextNode textNode, int x, int y, float z, Color color) {
		renderText(renderer, textNode.text, textNode.font, x, y,
			textNode.size.x, textNode.size.y, z, color);
	}
	
	/**
	 * Renders text on the screen on a given position.
	 * @param x X position
	 * @param y Y position
	 */
	public static void renderText(Renderer renderer, String text, Font font, int x, int y, int width, int height, float z, Color color) {
		if (text.isBlank() || width == 0 || height == 0 || color == null) {
			return;
		}
		
		if (font == null) {
			renderer.drawText(text, x, y, z, color);
		} else {
			renderer.drawText(font, text, x, y, z, color);
		}
	}
}
