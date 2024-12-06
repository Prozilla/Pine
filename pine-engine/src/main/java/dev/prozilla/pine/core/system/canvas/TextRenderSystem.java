package dev.prozilla.pine.core.system.canvas;

import dev.prozilla.pine.core.component.canvas.TextRenderer;
import dev.prozilla.pine.core.rendering.Renderer;
import dev.prozilla.pine.core.system.RenderSystem;

public class TextRenderSystem extends RenderSystem {
	
	public TextRenderSystem() {
		super(TextRenderer.class);
	}
	
	@Override
	public void render(Renderer renderer) {
		forEach(match -> {
			TextRenderer textRenderer = match.getComponent(TextRenderer.class);
			
			// Ignore invisible text
			if (textRenderer.text.isBlank() || textRenderer.width == 0 || textRenderer.height == 0) {
				return;
			}
			
			if (textRenderer.font == null) {
				renderer.drawText(textRenderer.text, textRenderer.x, textRenderer.y, textRenderer.color);
			} else {
				renderer.drawText(textRenderer.font, textRenderer.text, textRenderer.x, textRenderer.y, textRenderer.color);
			}
		});
	}
}
