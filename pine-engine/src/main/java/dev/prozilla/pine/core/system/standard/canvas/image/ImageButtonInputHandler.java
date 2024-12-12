package dev.prozilla.pine.core.system.standard.canvas.image;

import dev.prozilla.pine.core.component.canvas.ImageButtonRenderer;
import dev.prozilla.pine.core.component.canvas.RectTransform;
import dev.prozilla.pine.core.entity.EntityMatch;
import dev.prozilla.pine.core.state.input.CursorType;
import dev.prozilla.pine.core.state.input.Input;
import dev.prozilla.pine.core.state.input.MouseButton;
import dev.prozilla.pine.core.system.input.InputSystem;

public class ImageButtonInputHandler extends InputSystem {
	
	public ImageButtonInputHandler() {
		super(ImageButtonRenderer.class, RectTransform.class);
	}
	
	@Override
	protected void process(EntityMatch match, Input input, float deltaTime) {
		ImageButtonRenderer imageButtonRenderer = match.getComponent(ImageButtonRenderer.class);
		RectTransform rect = match.getComponent(RectTransform.class);
		
		imageButtonRenderer.isHovering = rect.cursorHit;
		
		if (rect.cursorHit) {
			getInput().setCursorType(CursorType.HAND);
			
			if (imageButtonRenderer.clickCallback != null && getInput().getMouseButtonDown(MouseButton.LEFT)) {
				imageButtonRenderer.clickCallback.run();
			}
		}
	}
}
