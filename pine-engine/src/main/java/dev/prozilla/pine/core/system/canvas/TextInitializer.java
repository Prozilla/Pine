package dev.prozilla.pine.core.system.canvas;

import dev.prozilla.pine.core.component.canvas.TextRenderer;
import dev.prozilla.pine.core.system.InitSystem;

public class TextInitializer extends InitSystem {
	
	public TextInitializer() {
		super(TextRenderer.class);
	}
	
	@Override
	public void init(long window) {
		forEach(match -> {
			TextRenderer textRenderer = match.getComponent(TextRenderer.class);
			
			textRenderer.calculateSize();
		});
	}
}
