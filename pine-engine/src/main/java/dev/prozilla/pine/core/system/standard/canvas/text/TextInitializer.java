package dev.prozilla.pine.core.system.standard.canvas.text;

import dev.prozilla.pine.core.component.canvas.TextRenderer;
import dev.prozilla.pine.core.entity.EntityMatch;
import dev.prozilla.pine.core.system.init.InitSystem;

/**
 * Initializes text elements by calculating their initial size.
 */
public class TextInitializer extends InitSystem {
	
	public TextInitializer() {
		super(TextRenderer.class);
	}
	
	@Override
	protected void process(EntityMatch match) {
		TextRenderer textRenderer = match.getComponent(TextRenderer.class);
		textRenderer.calculateSize();
	}
}
