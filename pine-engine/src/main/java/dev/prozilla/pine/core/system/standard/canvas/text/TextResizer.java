package dev.prozilla.pine.core.system.standard.canvas.text;

import dev.prozilla.pine.core.component.canvas.RectTransform;
import dev.prozilla.pine.core.component.canvas.TextButtonRenderer;
import dev.prozilla.pine.core.component.canvas.TextRenderer;
import dev.prozilla.pine.core.entity.EntityChunk;
import dev.prozilla.pine.core.system.update.UpdateSystem;

public class TextResizer extends UpdateSystem {
	
	public TextResizer() {
		super(TextRenderer.class, RectTransform.class);
		setExcludedComponentTypes(TextButtonRenderer.class);
	}
	
	@Override
	protected void process(EntityChunk chunk, float deltaTime) {
		TextRenderer textRenderer = chunk.getComponent(TextRenderer.class);
		RectTransform rect = chunk.getComponent(RectTransform.class);
		
		rect.currentSize.x = textRenderer.size.x;
		rect.currentSize.y = textRenderer.size.y;
	}
}
