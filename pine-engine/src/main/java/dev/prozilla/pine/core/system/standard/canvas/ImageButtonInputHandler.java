package dev.prozilla.pine.core.system.standard.canvas;

import dev.prozilla.pine.core.component.canvas.ImageButtonRenderer;
import dev.prozilla.pine.core.entity.EntityMatch;
import dev.prozilla.pine.core.state.input.CursorType;
import dev.prozilla.pine.core.state.input.Input;
import dev.prozilla.pine.core.state.input.MouseButton;
import dev.prozilla.pine.core.system.input.InputSystem;

import java.awt.*;

public class ImageButtonInputHandler extends InputSystem {
	
	public ImageButtonInputHandler() {
		super(ImageButtonRenderer.class);
	}
	
	@Override
	protected void process(EntityMatch match, Input input, float deltaTime) {
		ImageButtonRenderer imageButtonRenderer = match.getComponent(ImageButtonRenderer.class);
		
		Point cursor = getInput().getCursor();
		int canvasHeight = imageButtonRenderer.getCanvas().getHeight();
		if (cursor != null && imageButtonRenderer.isInside(cursor.x, canvasHeight - cursor.y, imageButtonRenderer.x, imageButtonRenderer.y - imageButtonRenderer.paddingY * 2, imageButtonRenderer.width, imageButtonRenderer.height)) {
			getInput().setCursorType(CursorType.HAND);
			getInput().blockCursor(imageButtonRenderer.entity);
			imageButtonRenderer.isHovering = true;
			
			if (imageButtonRenderer.clickCallback != null && getInput().getMouseButtonDown(MouseButton.LEFT)) {
				imageButtonRenderer.clickCallback.run();
			}
		} else {
			imageButtonRenderer.isHovering = false;
		}
	}
}
