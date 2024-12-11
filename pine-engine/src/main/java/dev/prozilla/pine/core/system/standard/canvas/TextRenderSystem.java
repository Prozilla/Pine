package dev.prozilla.pine.core.system.standard.canvas;

import dev.prozilla.pine.common.system.resource.Color;
import dev.prozilla.pine.common.system.resource.text.Font;
import dev.prozilla.pine.core.component.canvas.RectTransform;
import dev.prozilla.pine.core.component.canvas.TextButtonRenderer;
import dev.prozilla.pine.core.component.canvas.TextRenderer;
import dev.prozilla.pine.core.entity.EntityMatch;
import dev.prozilla.pine.core.rendering.Renderer;
import dev.prozilla.pine.core.system.render.RenderSystem;

/**
 * Renders text elements to the screen.
 */
public class TextRenderSystem extends RenderSystem {
	
	public TextRenderSystem() {
		super(TextRenderer.class, RectTransform.class);
		setExcludedComponentTypes(TextButtonRenderer.class);
	}
	
	@Override
	public void process(EntityMatch match, Renderer renderer) {
		TextRenderer textRenderer = match.getComponent(TextRenderer.class);
		RectTransform rect = match.getComponent(RectTransform.class);
		renderText(renderer, textRenderer, rect);
	}
	
	public static void renderText(Renderer renderer, TextRenderer textRenderer, RectTransform rect) {
		renderText(renderer, textRenderer, rect.x, rect.y, textRenderer.color);
	}
	
	public static void renderText(Renderer renderer, TextRenderer textRenderer, int x, int y, Color color) {
		renderText(renderer, textRenderer.text, textRenderer.font, x, y,
			textRenderer.width, textRenderer.height, color);
	}
	
	/**
	 * Renders text on the screen on a given position.
	 * @param x X position
	 * @param y Y position
	 */
	public static void renderText(Renderer renderer, String text, Font font, int x, int y, int width, int height, Color color) {
		if (text.isBlank() || width == 0 || height == 0) {
			return;
		}
		
		if (font == null) {
			renderer.drawText(text, x, y, color);
		} else {
			renderer.drawText(font, text, x, y, color);
		}
	}
}
