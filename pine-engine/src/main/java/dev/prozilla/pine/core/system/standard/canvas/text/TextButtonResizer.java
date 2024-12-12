package dev.prozilla.pine.core.system.standard.canvas.text;

import dev.prozilla.pine.core.component.canvas.RectTransform;
import dev.prozilla.pine.core.component.canvas.TextButtonRenderer;
import dev.prozilla.pine.core.component.canvas.TextRenderer;
import dev.prozilla.pine.core.entity.EntityMatch;
import dev.prozilla.pine.core.system.update.UpdateSystem;

public class TextButtonResizer extends UpdateSystem {
	
	public TextButtonResizer() {
		super(TextButtonRenderer.class, TextRenderer.class, RectTransform.class);
	}
	
	@Override
	protected void process(EntityMatch match, float deltaTime) {
		TextButtonRenderer textButtonRenderer = match.getComponent(TextButtonRenderer.class);
		TextRenderer textRenderer = match.getComponent(TextRenderer.class);
		RectTransform rect = match.getComponent(RectTransform.class);
		
		rect.width = textRenderer.width + textButtonRenderer.paddingX * 2;
		rect.height = textRenderer.height + textButtonRenderer.paddingY * 2;
	}
}
