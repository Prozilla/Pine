package dev.prozilla.pine.core.system.standard.canvas.text;

import dev.prozilla.pine.common.system.resource.Color;
import dev.prozilla.pine.common.system.resource.text.Font;
import dev.prozilla.pine.core.component.Transform;
import dev.prozilla.pine.core.component.canvas.RectTransform;
import dev.prozilla.pine.core.component.canvas.TextRenderer;
import dev.prozilla.pine.core.entity.EntityChunk;
import dev.prozilla.pine.core.rendering.Renderer;
import dev.prozilla.pine.core.system.render.RenderSystem;

/**
 * Renders text elements to the screen.
 */
public class TextRenderSystem extends RenderSystem {
	
	public TextRenderSystem() {
		super(TextRenderer.class, RectTransform.class);
	}
	
	@Override
	public void process(EntityChunk chunk, Renderer renderer) {
		Transform transform = chunk.getTransform();
		TextRenderer textRenderer = chunk.getComponent(TextRenderer.class);
		RectTransform rect = chunk.getComponent(RectTransform.class);
		
		if (!rect.readyToRender) {
			return;
		}
		
		renderText(renderer, textRenderer, rect, transform.getDepth());
	}
	
	public static void renderText(Renderer renderer, TextRenderer textRenderer, RectTransform rect, float z) {
		renderText(renderer, textRenderer, rect.currentPosition.x + rect.getPaddingX(), rect.currentPosition.y + rect.getPaddingY(), z, rect.color);
	}
	
	public static void renderText(Renderer renderer, TextRenderer textRenderer, int x, int y, float z, Color color) {
		renderText(renderer, textRenderer.text, textRenderer.font, x, y,
			textRenderer.size.x, textRenderer.size.y, z, color);
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
