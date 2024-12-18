package dev.prozilla.pine.core.system.standard.canvas.text;

import dev.prozilla.pine.core.component.canvas.RectTransform;
import dev.prozilla.pine.core.component.canvas.TextButtonRenderer;
import dev.prozilla.pine.core.entity.EntityChunk;
import dev.prozilla.pine.core.state.input.CursorType;
import dev.prozilla.pine.core.state.input.Input;
import dev.prozilla.pine.core.state.input.MouseButton;
import dev.prozilla.pine.core.system.input.InputSystem;

public class TextButtonInputHandler extends InputSystem {
	
	public TextButtonInputHandler() {
		super(TextButtonRenderer.class, RectTransform.class);
	}
	
	@Override
	protected void process(EntityChunk chunk, Input input, float deltaTime) {
		TextButtonRenderer textButtonRenderer = chunk.getComponent(TextButtonRenderer.class);
		RectTransform rect = chunk.getComponent(RectTransform.class);
		
		textButtonRenderer.isHovering = rect.cursorHit;
		
		if (rect.cursorHit) {
			getInput().setCursorType(CursorType.HAND);
			
			if (textButtonRenderer.clickCallback != null && getInput().getMouseButtonDown(MouseButton.LEFT)) {
				textButtonRenderer.clickCallback.run();
			}
		}
	}
}
