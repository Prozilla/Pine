package dev.prozilla.pine.core.system.standard.canvas.text;

import dev.prozilla.pine.core.component.canvas.RectTransform;
import dev.prozilla.pine.core.component.canvas.TextButtonRenderer;
import dev.prozilla.pine.core.component.canvas.TextRenderer;
import dev.prozilla.pine.core.entity.EntityChunk;
import dev.prozilla.pine.core.system.update.UpdateSystem;

public class TextButtonResizer extends UpdateSystem {
	
	public TextButtonResizer() {
		super(TextButtonRenderer.class, TextRenderer.class, RectTransform.class);
	}
	
	@Override
	protected void process(EntityChunk chunk, float deltaTime) {
		TextButtonRenderer textButtonRenderer = chunk.getComponent(TextButtonRenderer.class);
		TextRenderer textRenderer = chunk.getComponent(TextRenderer.class);
		RectTransform rect = chunk.getComponent(RectTransform.class);
		
		rect.width = textRenderer.width + textButtonRenderer.paddingX * 2;
		rect.height = textRenderer.height + textButtonRenderer.paddingY * 2;
	}
}
