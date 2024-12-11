package dev.prozilla.pine.core.system.standard.canvas;

import dev.prozilla.pine.core.component.canvas.RectTransform;
import dev.prozilla.pine.core.component.canvas.TextButtonRenderer;
import dev.prozilla.pine.core.component.canvas.TextRenderer;
import dev.prozilla.pine.core.entity.EntityMatch;
import dev.prozilla.pine.core.system.update.UpdateSystem;

public class TextResizer extends UpdateSystem {
	
	public TextResizer() {
		super(TextRenderer.class, RectTransform.class);
		setExcludedComponentTypes(TextButtonRenderer.class);
	}
	
	@Override
	protected void process(EntityMatch match, float deltaTime) {
		TextRenderer textRenderer = match.getComponent(TextRenderer.class);
		RectTransform rect = match.getComponent(RectTransform.class);
		
		rect.width = textRenderer.width;
		rect.height = textRenderer.height;
	}
}
