package dev.prozilla.pine.core.system.standard.canvas.text;

import dev.prozilla.pine.common.system.resource.Color;
import dev.prozilla.pine.core.component.Transform;
import dev.prozilla.pine.core.component.canvas.ButtonData;
import dev.prozilla.pine.core.component.canvas.RectTransform;
import dev.prozilla.pine.core.component.canvas.TextButtonRenderer;
import dev.prozilla.pine.core.component.canvas.TextRenderer;
import dev.prozilla.pine.core.entity.EntityChunk;
import dev.prozilla.pine.core.rendering.Renderer;
import dev.prozilla.pine.core.system.render.RenderSystem;

public class TextButtonRenderSystem extends RenderSystem {
	
	public TextButtonRenderSystem() {
		super(TextButtonRenderer.class, ButtonData.class, TextRenderer.class, RectTransform.class);
	}
	
	@Override
	protected void process(EntityChunk chunk, Renderer renderer) {
		Transform transform = chunk.getTransform();
		TextButtonRenderer textButtonRenderer = chunk.getComponent(TextButtonRenderer.class);
		ButtonData buttonData = chunk.getComponent(ButtonData.class);
		TextRenderer textRenderer = chunk.getComponent(TextRenderer.class);
		RectTransform rect = chunk.getComponent(RectTransform.class);
		
		if (!rect.readyToRender) {
			return;
		}
		
		// Render background
		if (textRenderer.size.x != 0 && textRenderer.size.y != 0 && textButtonRenderer.backgroundColor != null) {
			Color backgroundColor = (buttonData.isHovering && textButtonRenderer.backgroundHoverColor != null) ? textButtonRenderer.backgroundHoverColor : textButtonRenderer.backgroundColor;
			renderer.drawRect(rect.currentPosition.x, rect.currentPosition.y,  transform.getDepth(), rect.currentSize.x, rect.currentSize.y, backgroundColor);
		}
		
		// Render text
		Color textColor = (buttonData.isHovering && textButtonRenderer.hoverColor != null) ? textButtonRenderer.hoverColor : textRenderer.color;
		int textX = rect.currentPosition.x + textButtonRenderer.padding.computeX(rect);
		int textY = rect.currentPosition.y + textButtonRenderer.padding.computeY(rect);

		TextRenderSystem.renderText(renderer, textRenderer, textX, textY, transform.getDepth(), textColor);
	}
}
