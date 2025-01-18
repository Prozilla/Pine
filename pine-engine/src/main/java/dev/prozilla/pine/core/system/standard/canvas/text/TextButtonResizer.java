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
		
		rect.currentSize.x = textRenderer.size.x + textButtonRenderer.padding.computeX(rect) * 2;
		rect.currentSize.y = textRenderer.size.y + textButtonRenderer.padding.computeY(rect) * 2;
	}
}
