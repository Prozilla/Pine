package dev.prozilla.pine.core.system.standard.canvas.group;

import dev.prozilla.pine.core.component.canvas.CanvasGroup;
import dev.prozilla.pine.core.component.canvas.RectTransform;
import dev.prozilla.pine.core.entity.EntityChunk;
import dev.prozilla.pine.core.state.input.Input;
import dev.prozilla.pine.core.system.input.InputSystem;

/**
 * Updates the <code>passThrough</code> properties of canvas groups based on their background color.
 */
public class CanvasGroupInputHandler extends InputSystem {
	
	public CanvasGroupInputHandler() {
		super(CanvasGroup.class, RectTransform.class);
	}
	
	@Override
	public void process(EntityChunk chunk, Input input, float deltaTime) {
		CanvasGroup canvasGroup = chunk.getComponent(CanvasGroup.class);
		RectTransform rect = chunk.getComponent(RectTransform.class);
		
		rect.passThrough = (canvasGroup.backgroundColor == null);
	}
}
