package dev.prozilla.pine.core.system.standard.canvas.text;

import dev.prozilla.pine.common.system.resource.Color;
import dev.prozilla.pine.core.component.canvas.RectTransform;
import dev.prozilla.pine.core.component.canvas.TextButtonRenderer;
import dev.prozilla.pine.core.component.canvas.TextRenderer;
import dev.prozilla.pine.core.entity.EntityMatch;
import dev.prozilla.pine.core.rendering.Renderer;
import dev.prozilla.pine.core.system.render.RenderSystem;

public class TextButtonRenderSystem extends RenderSystem {
	
	public TextButtonRenderSystem() {
		super(TextButtonRenderer.class, TextRenderer.class, RectTransform.class);
	}
	
	@Override
	protected void process(EntityMatch match, Renderer renderer) {
		TextButtonRenderer textButtonRenderer = match.getComponent(TextButtonRenderer.class);
		TextRenderer textRenderer = match.getComponent(TextRenderer.class);
		RectTransform rect = match.getComponent(RectTransform.class);
		
		// Render background
		if (textRenderer.width != 0 && textRenderer.height != 0 && textButtonRenderer.backgroundColor != null) {
			Color backgroundColor = (textButtonRenderer.isHovering && textButtonRenderer.backgroundHoverColor != null) ? textButtonRenderer.backgroundHoverColor : textButtonRenderer.backgroundColor;
			renderer.drawRect(rect.x, rect.y, rect.width, rect.height, backgroundColor);
		}
		
		// Render text
		Color textColor = (textButtonRenderer.isHovering && textButtonRenderer.hoverColor != null) ? textButtonRenderer.hoverColor : textRenderer.color;
		int textX = rect.x + textButtonRenderer.paddingX;
		int textY = rect.y + textButtonRenderer.paddingY;
		
		TextRenderSystem.renderText(renderer, textRenderer, textX, textY, textColor);
	}
}
