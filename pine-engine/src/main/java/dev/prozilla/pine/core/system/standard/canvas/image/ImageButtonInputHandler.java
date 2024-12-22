package dev.prozilla.pine.core.system.standard.canvas.image;

import dev.prozilla.pine.core.component.canvas.ImageButtonRenderer;
import dev.prozilla.pine.core.component.canvas.RectTransform;
import dev.prozilla.pine.core.entity.EntityChunk;
import dev.prozilla.pine.core.state.input.CursorType;
import dev.prozilla.pine.core.state.input.Input;
import dev.prozilla.pine.core.state.input.MouseButton;
import dev.prozilla.pine.core.system.input.InputSystem;

public class ImageButtonInputHandler extends InputSystem {
	
	public ImageButtonInputHandler() {
		super(ImageButtonRenderer.class, RectTransform.class);
	}
	
	@Override
	protected void process(EntityChunk chunk, Input input, float deltaTime) {
		ImageButtonRenderer imageButtonRenderer = chunk.getComponent(ImageButtonRenderer.class);
		RectTransform rect = chunk.getComponent(RectTransform.class);
		
		imageButtonRenderer.isHovering = rect.cursorHit;
		
		if (rect.cursorHit) {
			input.setCursorType(CursorType.HAND);
			
			if (imageButtonRenderer.clickCallback != null && input.getMouseButtonDown(MouseButton.LEFT)) {
				imageButtonRenderer.click();
			}
		}
	}
}
