package dev.prozilla.pine.core.system.standard.ui.text;

import dev.prozilla.pine.common.asset.text.Font;
import dev.prozilla.pine.common.system.Color;
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
		float x = node.currentPosition.x + node.getPaddingX();
		float y = node.currentPosition.y + node.getPaddingY();
		float width = node.currentInnerSize.x;
		float height = node.currentInnerSize.y;
		
		renderText(renderer, textNode, x, y, z, width, height, node.color);
	}
	
	public static void renderText(Renderer renderer, TextNode textNode, float x, float y, float z, float width, float height, Color color) {
		renderText(renderer, textNode.text, textNode.font, x, y, z, width, height, color);
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
		
		renderer.flush();
		if (renderer.getConfig().snapText.get()) {
			int roundedX = Math.round(x);
			int roundedY = Math.round(y);
			int roundedWidth = Math.round(width);
			int roundedHeight = Math.round(height);
			
			renderer.setRegion(roundedX, roundedY, roundedWidth, roundedHeight);
			
			x = roundedX;
			y = roundedY;
		} else {
			renderer.setRegion(x, y, width, height);
		}
		
		if (font == null) {
			renderer.drawText(text, x, y, z, color);
		} else {
			renderer.drawText(font, text, x, y, z, color);
		}
		
		renderer.flush();
		renderer.resetRegion();
	}
}
